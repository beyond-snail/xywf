package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityAddZhangDan extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivitySetting";
	private RelativeLayout ll_email;
	private RelativeLayout ll_hand;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_add_zhangdan);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("导入账单");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		ll_email = relativeLayout(R.id.ll_email);
		ll_email.setOnClickListener(this);
		ll_hand = relativeLayout(R.id.ll_hand);
		ll_hand.setOnClickListener(this);


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
			case R.id.ll_hand:
				startActivity(new Intent(mContext, ActivityHandCredit.class));
				break;
			case R.id.ll_email:
				startActivity(new Intent(mContext, ActivityHandCredit.class));
				break;
		}
	}
}
