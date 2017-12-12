package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityMyTx extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyTx";
	private TextView tv_tx_balance_amt;
	private TextView all_tx;

	private MoneyEditText etTradMoney;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_tx);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("提现");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {

		etTradMoney = findViewById(R.id.et_amt);
		etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)}); //即限定最大输入字符数为20

		tv_tx_balance_amt = textView(R.id.tv_tx_balance_amt);

		all_tx = textView(R.id.all_tx);
		all_tx.setOnClickListener(this);

		button(R.id.btn_save).setOnClickListener(this);

		String balanceAmt = getIntent().getStringExtra("balance");
		if (!StringUtils.isBlank(balanceAmt)){
			tv_tx_balance_amt.setText(balanceAmt);
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


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.all_tx:
				etTradMoney.setText(tv_tx_balance_amt.getText().toString().trim());
				etTradMoney.setSelection(etTradMoney.getText().length());
				break;
			case R.id.btn_save:
				String amt = StringUtils.changeY2F(etTradMoney.getText().toString().trim());
//				Double dAmt = StringUtils.getDouble(etTradMoney.getText().toString().trim());
				LogUtils.e(TAG, amt + " " );
				break;

		}
	}
}
