package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tool.utils.utils.ToastUtils;
import com.yywf.R;
import com.yywf.model.BankCardInfo;
import com.yywf.util.MyActivityManager;

public class ActivitySmartCreditNow extends BaseActivity implements View.OnClickListener {

	private static final String TAG = "ActivitySmartCreditNow";
	private BankCardInfo vo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_smart_credit_now);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("立即还款");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		vo = (BankCardInfo) getIntent().getSerializableExtra("BankCardInfo");
		if (vo == null){
			ToastUtils.CustomShow(mContext, "数据错误");
			onBackPressed();
			return;
		}

		initView();

	}


	private void initView() {

		relativeLayout(R.id.ll_wdh).setOnClickListener(this);
		relativeLayout(R.id.ll_smart_credit).setOnClickListener(this);
		relativeLayout(R.id.ll_credit).setOnClickListener(this);

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
			case R.id.ll_wdh:
//				startActivity(new Intent(mContext, ActivitySecuritySetting.class));
				break;
			case R.id.ll_smart_credit:
				startActivity(new Intent(mContext, ActivitySmartCreditPlan.class).putExtra("BankCardInfo", vo));
				break;
			case R.id.ll_credit:
				startActivity(new Intent(mContext, ActivityCredit.class));
				break;
		}
	}
}
