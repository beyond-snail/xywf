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
		if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
        }

		initView();

	}


	private void initView() {
		tv_balance_amt = textView(R.id.tv_balance_amt);

		tv_fr_amt = textView(R.id.tv_fr_amt);
		tv_jlj_amt = textView(R.id.tv_jlj_amt);
		tv1 = textView(R.id.tv1);
		tv2 = textView(R.id.tv2);

		button(R.id.btn_tx).setOnClickListener(this);


		tv_balance_amt.setText("123456789012");

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
			case R.id.btn_tx:
				startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("balance", tv_balance_amt.getText().toString().trim()));
				break;

		}
	}
}
