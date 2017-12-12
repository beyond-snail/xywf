package com.yywf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityAboutUs extends BaseActivity  {

	private final String TAG = "ActivityAboutUs";
	private TextView tv_web;
	private TextView tv_phone;
	private TextView tv_wxgzh;
	private TextView tv_current_version;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_aboutus);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("关于我们");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		try {
			initView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void initView() throws Exception {
		tv_web = textView(R.id.tv_web);
		tv_phone = textView(R.id.tv_phone);
		tv_wxgzh = textView(R.id.tv_wxgzh);
		tv_current_version = textView(R.id.tv_current_version);

		tv_web.setText("www.xyk11.com");
		tv_phone.setText("0571-8858 6888");
		tv_wxgzh.setText("信易沃富");

		String appname = getResources().getString(R.string.app_name);
		tv_current_version.setText(appname+" V" + getVersionName());

	}


	/**
	 * 获取版本名称
	 *
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() throws Exception {
		return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}






}
