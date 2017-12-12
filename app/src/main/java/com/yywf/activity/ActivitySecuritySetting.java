package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivitySecuritySetting extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivitySecuritySetting";
	private RelativeLayout ll_edit_login_pwd;
	private RelativeLayout ll_edit_pay_pwd;
	private RelativeLayout ll_forgot_pwd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_security_setting);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("安全设置");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		ll_edit_login_pwd = relativeLayout(R.id.ll_edit_login_pwd);
		ll_edit_login_pwd.setOnClickListener(this);
		ll_edit_pay_pwd = relativeLayout(R.id.ll_edit_pay_pwd);
		ll_edit_pay_pwd.setOnClickListener(this);
		ll_forgot_pwd = relativeLayout(R.id.ll_forgot_pwd);
		ll_forgot_pwd.setOnClickListener(this);

	}




	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ll_edit_login_pwd:
				startActivity(new Intent(mContext, ActivityEditLoginPwd.class));
				break;
			case R.id.ll_edit_pay_pwd:
				startActivity(new Intent(mContext, ActivityEditPayPwd.class));
				break;
			case R.id.ll_forgot_pwd:
				startActivity(new Intent(mContext, ActivityForgotPayPwd.class));
				break;
		}
	}
}
