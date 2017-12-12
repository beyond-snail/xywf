package com.yywf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityFollowUs extends BaseActivity  {

	private final String TAG = "ActivityFollowUs";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_follow_us);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("关注我们");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

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
