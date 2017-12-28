package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityMyWallet extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyWallet";
	private TextView tv_balance_amt;
	private TextView tv_fr_amt;
	private TextView tv_fh_amt;
	private TextView tv_jlj_amt;
	private TextView tv1;
	private TextView tv2;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_mywallet);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("我的钱包");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}


		initView();

	}


	private void initView() {
		tv_balance_amt = textView(R.id.tv_balance_amt);

		tv_fr_amt = textView(R.id.tv_fr_amt);
		tv_fh_amt = textView(R.id.tv_fh_amt);
		tv_jlj_amt = textView(R.id.tv_jlj_amt);

		relativeLayout(R.id.ll_fr).setOnClickListener(this);
		relativeLayout(R.id.ll_fh).setOnClickListener(this);
		relativeLayout(R.id.ll_jlj).setOnClickListener(this);

		textView(R.id.btn_tx).setOnClickListener(this);
		textView(R.id.btn_sale).setOnClickListener(this);


//		tv_balance_amt.setText("123456789012");

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
			case R.id.ll_fr:
				startActivity(new Intent(mContext, ActivityMyBenefit.class));
				break;
			case R.id.ll_fh:
				startActivity(new Intent(mContext, ActivityMyFh.class));
				break;
			case R.id.ll_jlj:
				startActivity(new Intent(mContext, ActivityMyJlj.class));
				break;
			case R.id.btn_tx:
				startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("balance", tv_balance_amt.getText().toString().trim()));
				break;
			case R.id.btn_sale:
				startActivity(new Intent(mContext, ActivityVipGrade.class));
				break;

		}
	}
}
