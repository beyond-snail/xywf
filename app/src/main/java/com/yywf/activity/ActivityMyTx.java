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
	private TextView id_tx_detail;


	private MoneyEditText etTradMoney;


	int amount = 0;
	int type = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_tx);
		MyActivityManager.getInstance().addActivity(this);

		type = getIntent().getIntExtra("type", 0);






		initView();

	}


	private void initView() {

		etTradMoney = findViewById(R.id.et_amt);
		etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)}); //即限定最大输入字符数为20

		tv_tx_balance_amt = textView(R.id.tv_tx_balance_amt);

		all_tx = textView(R.id.all_tx);
		all_tx.setOnClickListener(this);


		if (type == 1){
			initTitle("奖励金提现");
			amount = getIntent().getIntExtra("JljBalance", 0);
			textView(R.id.text_balance).setText("奖励金余额");
			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：0.1%+1元/笔");
		}else if (type == 2){
			initTitle("返利金提现");
			amount = getIntent().getIntExtra("FlBalance", 0);
			textView(R.id.text_balance).setText("返利金余额");
			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：6.8%+2元/笔");
		}else if (type == 3){
			initTitle("分润提现");
			amount = getIntent().getIntExtra("FrBalance", 0);
			textView(R.id.text_balance).setText("分润余额");
			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：6.8%+2元/笔");
		}


		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}


		tv_tx_balance_amt.setText(StringUtils.formatIntMoney(amount));



		textView(R.id.id_tx_detail).setOnClickListener(this);

		button(R.id.btn_save).setOnClickListener(this);


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
			case R.id.id_tx_detail:
				Intent intent = new Intent(mContext, ActivityReadTxt.class).putExtra("type", 5);
				startActivity(intent);
				break;
			case R.id.btn_save:
				String amt = StringUtils.changeY2F(etTradMoney.getText().toString().trim());
//				Double dAmt = StringUtils.getDouble(etTradMoney.getText().toString().trim());
				LogUtils.e(TAG, amt + " " );
				break;

		}
	}
}
