package com.yywf.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.business.Update;
import com.business.UpdateManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.http.HttpUtilKey;
import com.tool.utils.utils.GsonUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.adapter.AdapterLoginHistory;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.LoginHistoryVO;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class ActivityLogin extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private final String TAG = "ActivityLogin";
	private Context mContext;
	private AutoCompleteTextView atv_userName;
	private EditText et_password;
	private String username, password;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		// 带绿色背景的activity_login_bg 常见的布局activity_login
		setContentView(R.layout.activity_login_bg);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("登录");
		initView();



		//权限检测
		checkPermission();
	}







	private void initView() {
		// 登录账号
		atv_userName = (AutoCompleteTextView) findViewById(R.id.et_userName);
		// 登录密码
		et_password = (EditText) findViewById(R.id.et_password);
		// 登录
		button(R.id.btn_login).setOnClickListener(this);
		// 注册
		button(R.id.btn_register).setOnClickListener(this);
		textView(R.id.tv_right).setText("注册");
		textView(R.id.tv_right).setOnClickListener(this);

		// 忘记密码
		findViewById(R.id.btn_findPassword).setOnClickListener(this);

		((CheckBox) findViewById(R.id.cb_look)).setOnCheckedChangeListener(this);


	}

	private void refresh() {
		// 获取记住的密码
		username = UtilPreference.getStringValue(mContext, "userName");
		password = UtilPreference.getStringValue(mContext, "password");

		atv_userName.setText(username);
		et_password.setText(password);
	}

	@Override
	protected void onResume() {
		refresh();
//		initAutoComplete(atv_userName);
//		HttpUtilKey.appSid = "";
//		UtilPreference.saveString(mContext, "appSid","");
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(receiveBroadCast);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:// 登录
			doLogin();
//			showRegisterSuccess();
//			startActivity(new Intent(mContext, ActivityHome.class));
			break;
		case R.id.btn_register:
			startActivity(new Intent(mContext, ActivityRegister.class));
//			startActivity(new Intent(mContext, ActivityTest.class));
//			showRegisterSuccess();
			break;
		case R.id.btn_findPassword:// 密码找回
//			showRegisterSuccess();
//			showChoiceCredit();
			startActivity(new Intent(mContext, ActivityForgotPwd.class));
			break;
//		case R.id.iv_qq:// 第三方qq登录
//			mLoginAuth.doQQLogin();
//			// startActivity(new Intent(mContext, ActivityLoginAuth.class));
//			break;
//		case R.id.iv_weixin:// 第三方微信登录
//			mLoginAuth.weChatAuth();
//			// startActivity(new Intent(mContext, ActivityLoginAuth.class));
//			break;
		default:
			break;
		}
	}

	/**
	 * 执行登录
	 */
	private void doLogin() {
		username = atv_userName.getText().toString().trim();
		password = et_password.getText().toString().trim();

		if (!StringUtils.isCellPhone(username)) {
			atv_userName.setError("手机号码格式不正确");
			atv_userName.requestFocus();
			return;
		}
		if (StringUtils.isBlank(password)) {

			et_password.setError("密码不能为空");
			et_password.requestFocus();
			return;
		}
		
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("userName", username);
		params.add("password", password);
		HttpUtil.get(ConfigXy.XY_LOGIN, params, requestListener);

	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {
				JSONObject result = new JSONObject(response);

				if (result.optInt("code") == -2){
					UtilPreference.clearNotKeyValues(mContext);
					// 退出账号 返回到登录页面
					MyActivityManager.getInstance().logout(mContext);
					return;
				}
				if (!result.optBoolean("status")) {
					showErrorMsg(result.getString("message"));
					return;
				}

//				JSONObject data = obj.getJSONObject("obj");


//				UtilPreference.saveLoginInfo(mContext, username, password, data);
//				saveHistory("");// 记住登录过的账号

				JSONObject dataStr = result.getJSONObject("data");
				String token = dataStr.optString("token");
				String memberId = dataStr.optString("memberId");
				int approve_status = dataStr.optInt("approve_status");
				int isGrade = dataStr.optInt("is_getGrade");
				UtilPreference.saveString(mContext, "token", token);
				UtilPreference.saveString(mContext, "userName", username);
				UtilPreference.saveString(mContext, "password", password);
				UtilPreference.saveString(mContext, "memberId", memberId);
				UtilPreference.saveInt(mContext, "approve_status", approve_status);
				UtilPreference.saveInt(mContext, "isGrade", isGrade);


				startActivity(new Intent(mContext, ActivityHome.class));
				finish();

			} catch (Exception e) {
				Log.e(TAG, "doLogin() Exception: " + e.getMessage());
			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};
	
	
	

	/**
	 * 缓存最近登录过的账号
	 * 
	 * @param avatar
	 */
	@SuppressWarnings("unchecked")
	private void saveHistory(String avatar) {
		String loginhistory = UtilPreference.getStringValue(mContext, "login_history");
		Log.i(TAG, "本次登录前已经登录过的账号：" + loginhistory);

		JsonArray array = new JsonArray();

		if (StringUtils.isBlank(loginhistory)) {
			JsonObject json = new JsonObject();
			json.addProperty("username", username);
			json.addProperty("password", password);
			json.addProperty("avatar", avatar);
			array.add(json);

		} else {
			String historyArr = getHistoryArr(loginhistory);
			if (StringUtils.isBlank(historyArr)) {
				return;
			}

			// json字符串转换成List
			List<LoginHistoryVO> list = (List<LoginHistoryVO>) GsonUtil.getInstance()
					.convertJsonStringToList(historyArr, new TypeToken<List<LoginHistoryVO>>() {
					}.getType());

			boolean ishas = false;
			for (LoginHistoryVO vo : list) {
				if (vo.getUsername().equals(username)) {// 如果当前帐号存在历史记录就用当前密码替换原来密码，否则修改密码后历史记录中的密码未更新
					vo.setPassword(password);
					ishas = true;
				}
				JsonObject json = new JsonObject();
				json.addProperty("username", vo.getUsername());
				json.addProperty("password", vo.getPassword());
				json.addProperty("avatar", avatar);
				array.add(json);
			}
			// 当前登录账号在本机上没有历史登录记录
			if (!ishas) {
				JsonObject json = new JsonObject();
				json.addProperty("username", username);
				json.addProperty("password", password);
				json.addProperty("avatar", avatar);
				array.add(json);
			}
		}
		// json对象转换成字符串并缓存在本地
		UtilPreference.saveString(mContext, "login_history", GsonUtil.getInstance().convertObjectToJsonString(array));
	}

	/**
	 * 显示已登录过的账号列表
	 * 
	 * @param auto
	 */
	@SuppressWarnings("unchecked")
	private void initAutoComplete(AutoCompleteTextView auto) {
		String loginhistory = UtilPreference.getStringValue(mContext, "login_history");
		Log.i(TAG, "最近登录过的账号：" + loginhistory);

		if (StringUtils.isBlank(loginhistory) || !loginhistory.contains("elements")) {
			UtilPreference.saveString(mContext, "login_history", "");
			return;
		}

		String historyArr = getHistoryArr(loginhistory);
		if (StringUtils.isBlank(historyArr)) {
			return;
		}

		// json字符串转换成List
		final List<LoginHistoryVO> list = (List<LoginHistoryVO>) GsonUtil.getInstance()
				.convertJsonStringToList(historyArr, new TypeToken<List<LoginHistoryVO>>() {
				}.getType());

		AdapterLoginHistory adapter = new AdapterLoginHistory(mContext, R.layout.item_login_history, list);
		auto.setAdapter(adapter);
		auto.setThreshold(1);

		// 点击显示最近登录过的账号
		auto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				atv_userName.showDropDown();
				initAutoComplete(atv_userName);
			}
		});

		// 设置对应账号的密码
		auto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String username = list.get(position).getUsername();
				atv_userName.setText(username);
				String pwd = list.get(position).getPassword();
				et_password.setText(pwd);
				atv_userName.setSelection(atv_userName.length());
				// 隐藏软键盘
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null) {
						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
								Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}
		});

	}

	/**
	 * 根据缓存在本地的登录历史记录字符串 获取登录历史记录的数组
	 * 
	 * @param loginhistory
	 */
	private String getHistoryArr(String loginhistory) {
		String historyArr = "";
		try {
			JSONObject json = new JSONObject(loginhistory);
			if (json.has("elements")) {
				historyArr = json.getString("elements");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return historyArr;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		if (isChecked) {
			et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		et_password.setSelection(et_password.getText().length());
	}

//	private class LoginCallBack implements InterfaceLoginAuth {
//		@Override
//		public void onThirdLoginCallBack(JSONObject object, String openId, String loginType) {
//			/**
//			 * 第三方授权登录获取昵称头像等信息成功后回调方法
//			 */
//			Log.i(TAG, "onThirdLoginCallBack()");
//			// Log.i("onQQLoginCallBack", "onQQLoginCallBack:" +
//			// object.toString() + ",openId:" + openId);
//			onBackPressed();
//			startActivity(new Intent(mContext, ActivityAuthBind.class).putExtra("value", "next")
//					.putExtra("openId", openId).putExtra("type", loginType));
//		}
//	}



	public void showRegisterSuccess(){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_register_success, null);
		final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
				R.style.MyDialogStyleBottom);
		customBuilder.setCancelable(false);
		customBuilder.setCanceledOnTouchOutside(false);
		customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
		customBuilder.setContentView(view);
		// 2线在下方
		customBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
								int which) {
				dialog.dismiss();
			}
		});
		customBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
								int which) {
				dialog.dismiss();
			}
		});
		MyCustomDialog dialog = customBuilder.create();
		dialog.show();
	}


	public void showChoiceCredit(){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_credit_choice, null);
		final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
				R.style.MyDialogStyleBottom);
//		customBuilder.setCancelable(false);
		customBuilder.setCanceledOnTouchOutside(false);
		customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
		customBuilder.setContentView(view);
		customBuilder.setDisBottomButton(true);
		MyCustomDialog dialog = customBuilder.create();
		dialog.show();
	}


}
