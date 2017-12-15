package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivitySetting extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivitySetting";
	private RelativeLayout ll_set;
	private RelativeLayout ll_about_us;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_setting);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("设置");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		ll_set = relativeLayout(R.id.ll_set);
		ll_set.setOnClickListener(this);
		ll_about_us = relativeLayout(R.id.ll_about_us);
		ll_about_us.setOnClickListener(this);

		button(R.id.btn_logout);

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
			case R.id.ll_set:
				startActivity(new Intent(mContext, ActivitySecuritySetting.class));
				break;
			case R.id.ll_about_us:
				startActivity(new Intent(mContext, ActivityAboutUs.class));
				break;
			case R.id.btn_logout: //退出登录

				break;
		}
	}
}
