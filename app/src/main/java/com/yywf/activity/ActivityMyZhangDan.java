package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityMyZhangDan extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyZhangDan";
	private RelativeLayout ll_sale;
	private RelativeLayout ll_credit;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_my_zhangdan);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("我的账单");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		ll_sale = relativeLayout(R.id.ll_sale);
		ll_sale.setOnClickListener(this);
		ll_credit = relativeLayout(R.id.ll_credit);
		ll_credit.setOnClickListener(this);


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
			case R.id.ll_sale:
				startActivity(new Intent(mContext, ActivitySecuritySetting.class));
				break;
			case R.id.ll_credit:
				startActivity(new Intent(mContext, ActivityAboutUs.class));
				break;

		}
	}
}
