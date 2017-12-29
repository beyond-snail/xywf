package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.AlertUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyCountDownTimer;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册用户(2014-11-18代码重构)
 * 
 * @author morton
 */
public class ActivityRegister extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private static final String TAG = "ActivityRegister";

	private EditText et_telephone, et_code, et_pwd;
	private TextView tv_getcode;
	private Button btn_next;
	private LinearLayout ll_user_protocol;

	private String validCode;
	private CheckBox cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		// 带绿色背景的activity_register_bg 常见的布局activity_register
		setContentView(R.layout.activity_register_bg);

		initTitle("注册");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 手机号码
		et_telephone = editText(R.id.et_telephone);
		et_telephone.addTextChangedListener(txtWatch);

		// 获取验证码
		tv_getcode = (TextView) findViewById(R.id.tv_getcode);
		tv_getcode.setOnClickListener(this);
		// 验证码
		et_code = editText(R.id.et_code);
		et_pwd = editText(R.id.et_pwd);

		cb = (CheckBox) findViewById(R.id.cb_read_agreement);

		// 注册按钮
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		//查看密码
		((CheckBox) findViewById(R.id.cb_look)).setOnCheckedChangeListener(this);
		// 注册协议
		findViewById(R.id.ll_user_protocol).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ActivityReadTxt.class).putExtra("type", 1);
				startActivity(intent);
			}
		});

	}

	/**
	 * 验证改手机号是否已经注册
	 * 
	 * @param phone
	 */
	private void validPhone(String phone) {
		String url = ConfigXy.XY_VERITYTELEPHONE;
		RequestParams params = new RequestParams();
		params.add("account", phone);
		// 验证改手机号是否已经注册过程中不能点击下一步，不能发送短信
		btn_next.setEnabled(false);
		tv_getcode.setEnabled(false);

		HttpUtil.get(url, params, new HttpUtil.RequestListener() {

			@Override
			public void success(String response) {
				try {
					JSONObject result = new JSONObject(response);
					if (result.optInt("code") == -2){
						UtilPreference.clearNotKeyValues(mContext);
						// 退出账号 返回到登录页面
						MyActivityManager.getInstance().logout(mContext);
						return;
					}
					// 提示账号已经存在不能再注册
					if (!result.getString("status").equals("success")) {
						et_telephone.requestFocus();
						et_telephone.setError("该帐号已注册不能再注册！");
					} else {
						btn_next.setEnabled(true);
						tv_getcode.setEnabled(true);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable error) {
				showE404();
			}
		});
	}

	private String curPhone;

	/**
	 * 从服务器获取验证码
	 * 
	 * @param phone
	 */
	private void getValidCode(final String phone) {
		MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS, 1000, tv_getcode,
				getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
		countDowntimer.start();

		RequestParams params = new RequestParams();
		params.add("phone", phone);
		showProgress("正在发送");
		HttpUtil.get(ConfigXy.XY_SMSVALDATE, params, new HttpUtil.RequestListener() {

			@Override
			public void success(String response) {
				disShowProgress();
				try {
					JSONObject result = new JSONObject(response);
					if (!result.optBoolean("status")) {
						ToastUtils.CustomShow(mContext, result.optString("message"));
					}else {
//						validCode = result.getString("result");
//						Log.i("注册码：", validCode);
//						curPhone = phone;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();
			}

		});
	}

	/**
	 * 获取提交参数
	 * 
	 * @return
	 */
	private RequestParams getParams() {
		RequestParams params = new RequestParams();
		String userName = et_telephone.getText().toString();
		if (!StringUtils.isCellPhone(userName)) {
			et_telephone.requestFocus();
			et_telephone.setError("输入手机号格式不正确！");
			return null;
		}
		String password = et_pwd.getText().toString();
		if (StringUtils.isBlank(password)) {
			et_pwd.requestFocus();
			et_pwd.setError("密码不能为空！");
			return null;
		}
		if (password.length() < 6) {
			et_pwd.requestFocus();
			et_pwd.setError("密码位数不能低于6！");
			return null;
		}
		String code = et_code.getText().toString().trim();
		if (StringUtils.isBlank(code)) {
			et_code.requestFocus();
			et_code.setError("输入验证码错误！");
			return null;
		}

		if (!cb.isChecked()) {
			showErrorMsg("尚未同意条款及政策");
			return null;
		}

		params.add("phone", et_telephone.getText().toString().trim());
		params.add("password", et_pwd.getText().toString().trim());
		params.add("code", code.trim());

		return params;
	}

	/**
	 * 提交注册
	 * 
	 * @param params
	 */
	private void doSubmit(RequestParams params) {
		String url = ConfigXy.XY_REGISTER;
//		final String phone = et_telephone.getText().toString();
//		final String pwd = et_pwd.getText().toString();
		showProgress("注册中...");
		HttpUtil.post(url, params, new HttpUtil.RequestListener() {

			@Override
			public void success(String response) {
				Log.i(TAG, response);
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
						ToastUtils.CustomShow(mContext, result.optString("message"));
					}else {
						JSONObject dataStr = result.getJSONObject("data");
						String token = dataStr.optString("token");
						String memberId = dataStr.optString("memberId");
						int approve_status = dataStr.optInt("approve_status");
						int isGrade = dataStr.optInt("is_getGrade");
						UtilPreference.saveString(mContext, "token", token);
						UtilPreference.saveString(mContext, "userName", et_telephone.getText().toString().trim());
						UtilPreference.saveString(mContext, "memberId", memberId);
						UtilPreference.saveInt(mContext, "approve_status", approve_status);
						UtilPreference.saveInt(mContext, "isGrade", isGrade);
						startActivity(new Intent(mContext, ActivityHome.class));
						finish();
					}



//					String result = data.getString("message");
//
//					if (result.contains("成功")) {
//						result = "注册成功" + "\n帐号:" + phone;
//						AlertUtils.alert(result, mContext, new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								dialog.dismiss();
//								// 记住注册的账号和密码
//								UtilPreference.saveString(mContext, "userName", phone);
//								UtilPreference.saveString(mContext, "password", pwd);
//
//								onBackPressed();
//							}
//						});
//					} else {
//						AlertUtils.alert(result, mContext, new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								dialog.dismiss();
//							}
//						});
//					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();
			}
		});
	}

	/**
	 * 监听输入的手机号码，判断该号码是否已注册
	 */
	private TextWatcher txtWatch = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String phone = s.toString();
			if (phone.length() == 11) {
//				validPhone(phone);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String phone = s.toString();
			if (!phone.equals(curPhone)) {// 获取验证码后 返回来修改电话 重置当前获取的验证码
				validCode = "";
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_getcode:
			// 获取验证码
			String phone = et_telephone.getText().toString();
			if (StringUtils.isCellPhone(phone)) {
				et_code.requestFocus();
				getValidCode(phone);// 从服务器获取验证码
				et_code.setTextColor(mContext.getResources().getColor(R.color.gray));
			} else {
				et_telephone.requestFocus();
				et_telephone.setError("输入手机号格式不正确！");
			}

			break;
		case R.id.btn_next:
			// 提交
			RequestParams params = getParams();
			if (params != null) {
				doSubmit(params);
			}
			break;
		case R.id.btn_login:
			startActivity(new Intent(mContext, ActivityLogin.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		/*
		 * baiduMap.onDestory(); baiduMap = null;
		 */
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		} else {
			et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		et_pwd.setSelection(et_pwd.getText().length());
	}

}
