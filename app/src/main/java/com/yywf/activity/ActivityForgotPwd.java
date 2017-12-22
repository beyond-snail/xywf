package com.yywf.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册用户(2014-11-18代码重构)
 * 
 * @author morton
 */
public class ActivityForgotPwd extends BaseActivity implements OnClickListener, OnCheckedChangeListener {

	private static final String TAG = "ActivityRegister";

	private EditText et_telephone, et_code, et_pwd, et_comfirm_pwd;
	private TextView tv_getcode;
	private Button btn_next, btn_commit;

	private LinearLayout ll_step_1, ll_step_2;

	private String validCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		// 带绿色背景的activity_register_bg 常见的布局activity_register
		setContentView(R.layout.activity_forgot_pwd);

		initTitle("忘记密码");
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

		// 获取验证码
		tv_getcode = textView(R.id.tv_getcode);
		tv_getcode.setOnClickListener(this);

		// 验证码
		et_code = editText(R.id.et_code);

		// 新密码
		et_pwd = editText(R.id.et_new_pwd);
		// 确认密码
		et_comfirm_pwd = editText(R.id.et_comfirm_pwd);




		// 下一步
		btn_next = button(R.id.btn_next);
		btn_next.setOnClickListener(this);

		//提交
		btn_commit = button(R.id.btn_commit);
		btn_commit.setOnClickListener(this);


		ll_step_1 = linearLayout(R.id.id_forgot_step_1);
		ll_step_2 = linearLayout(R.id.id_forgot_step_2);


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
	 * 重置密码
	 */
	private void resetPwd() {
		final String phone = et_telephone.getText().toString();
		final String pwd = et_pwd.getText().toString();
		String pwd_repeat = et_comfirm_pwd.getText().toString();

		if (StringUtils.isBlank(pwd) || StringUtils.isBlank(pwd_repeat)) {
			et_pwd.requestFocus();
			et_pwd.setError("密码不能为空，请输入密码");
			return;
		}

		if (pwd.length() < 6 || pwd_repeat.length() < 6) {
			et_comfirm_pwd.requestFocus();
			et_comfirm_pwd.setError("密码长度为6-18位，请重新输入");
			et_pwd.setText("");
			et_comfirm_pwd.setText("");
			return;
		}

		if (!pwd.equalsIgnoreCase(pwd_repeat)) {
			et_pwd.requestFocus();
			et_pwd.setError("两次输入密码不一致，请重新输入");
			et_pwd.setText("");
			et_comfirm_pwd.setText("");
			return;
		}


		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		params.add("phone", phone);
		params.add("password", et_comfirm_pwd.getText().toString().trim());
		params.add("code", validCode);
		params.add("type", "1");

		HttpUtil.get(ConfigXy.XY_FORGOT_PAY_PWD, params, new HttpUtil.RequestListener() {

			@Override
			public void success(String response) {
				disShowProgress();
				try {
					JSONObject result = new JSONObject(response);
					if (!result.optBoolean("status")) {
						ToastUtils.CustomShow(mContext, result.optString("message"));
					}else{
						ToastUtils.CustomShow(mContext, result.optString("message"));
						finish();
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
			String etPhone = et_telephone.getText().toString();
			String code = et_code.getText().toString().replace(" ", "");
			if (!StringUtils.isCellPhone(etPhone)) {
				et_telephone.setError("手机号码格式不正确");
				et_telephone.requestFocus();
				return;
			}
			if (StringUtils.isBlank(code)) {
				et_code.setError("验证码不能为空");
				et_code.requestFocus();
				return;
			}
			validCode = et_code.getText().toString().trim();
			ll_step_1.setVisibility(View.GONE);
			ll_step_2.setVisibility(View.VISIBLE);
			initTitle("设置密码");
			break;
		case R.id.btn_commit:
			resetPwd();
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
