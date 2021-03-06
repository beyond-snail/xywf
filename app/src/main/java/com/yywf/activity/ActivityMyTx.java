package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.TxQueryInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

public class ActivityMyTx extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyTx";
//	private TextView text_balance;
	private TextView all_tx;
	private TextView tv_amt;
	private TextView tv_no_amt;
	private TxQueryInfo txRecordDetail;

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
		getData();
	}


	private void initView() {

		etTradMoney = findViewById(R.id.et_amt);
//		etTradMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)}); //即限定最大输入字符数为20

//		text_balance = textView(R.id.text_balance);
		tv_amt = textView(R.id.tv_amt);
		tv_no_amt = textView(R.id.tv_no_amt);

		all_tx = textView(R.id.all_tx);
		all_tx.setOnClickListener(this);


		if (type == 1){
			initTitle("奖励金提现");
//			amount = getIntent().getIntExtra("JljBalance", 0);
//			textView(R.id.text_balance).setText("奖励金余额");
//			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：0.1%+1元/笔");
		}else if (type == 2){
			initTitle("返利金提现");
//			amount = getIntent().getIntExtra("FlBalance", 0);
//			textView(R.id.text_balance).setText("返利金余额");
//			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：6.8%+2元/笔");
		}else if (type == 3){
			initTitle("分润提现");
//			amount = getIntent().getIntExtra("FrBalance", 0);
//			textView(R.id.text_balance).setText("分润余额");
//			textView(R.id.fee_text).setText("尾号 "+"6789"+" | 提现手续费：6.8%+2元/笔");
		}


		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
					Intent intent = new Intent(mContext, ActivityReadTxt.class).putExtra("type", 5);
					startActivity(intent);
                }
            });
        }


//		text_balance.setText(StringUtils.formatIntMoney(amount));



//		textView(R.id.id_tx_detail).setOnClickListener(this);

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

				etTradMoney.setText(tv_amt.getText().toString().trim());
				etTradMoney.setSelection(etTradMoney.getText().length());
				break;
//			case R.id.id_tx_detail:
//				Intent intent = new Intent(mContext, ActivityReadTxt.class).putExtra("type", 5);
//				startActivity(intent);
//				break;
			case R.id.btn_save:
				doCommit();
//				String amt = StringUtils.changeY2F(etTradMoney.getText().toString().trim());
//				LogUtils.e(TAG, amt + " " );
				break;

		}
	}


	private void getData() {

		showProgress("加载中...");
		String url = ConfigXy.XY_MY_CAN_TX;
		RequestParams params = new RequestParams();
		params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.put("token", UtilPreference.getStringValue(mContext, "token"));
		params.put("type", type);

		HttpUtil.get(url, params, new HttpUtil.RequestListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void success(String response) {
				disShowProgress();
				try {

					JSONObject result = new JSONObject(response);
					if (result.optInt("code") == -2){
						UtilPreference.clearNotKeyValues(mContext);
						// 退出账号 返回到登录页面
						MyActivityManager.getInstance().logout(mContext);
						return;
					}
					if (!result.optBoolean("status")) {
						showErrorMsg(result.getString("message"));
						return;
					}

					txRecordDetail = new Gson().fromJson(result.optString("data"), new TypeToken<TxQueryInfo>() {
					}.getType());
					if (txRecordDetail != null) {
						tv_amt.setText(StringUtils.formatIntMoney(txRecordDetail.getTranAmt()));
						tv_no_amt.setText("不可提现余额: " + StringUtils.formatIntMoney(txRecordDetail.getNotranAmt()));
						textView(R.id.fee_text).setText(txRecordDetail.getDescription());
						textView(R.id.tv_gz).setText("提现金额(提现手续费: "+txRecordDetail.getFeeDescription()+")");
					}



				} catch (Exception e) {
					e.getMessage();

				}
			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();

			}
		});
	}

	private void doCommit() {
		if (StringUtils.isBlank(etTradMoney.getMoneyText().trim())){
			ToastUtils.showShort(mContext, "请输入金额");
			return;
		}



		showProgress("加载中...");

		String url = ConfigXy.XY_MY_TX;
		RequestParams params = new RequestParams();
		params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.put("token", UtilPreference.getStringValue(mContext, "token"));
		params.put("tranAmt", StringUtils.changeY2F(etTradMoney.getMoneyText().trim()));
		params.put("type", type);

		HttpUtil.get(url, params, new HttpUtil.RequestListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void success(String response) {
				disShowProgress();
				try {

					JSONObject result = new JSONObject(response);
					if (result.optInt("code") == -2){
						UtilPreference.clearNotKeyValues(mContext);
						// 退出账号 返回到登录页面
						MyActivityManager.getInstance().logout(mContext);
						return;
					}
					if (!result.optBoolean("status")) {
						showErrorMsg(result.getString("message"));
						return;
					}


					showErrorMsg(result.getString("message"));
					finish();


				} catch (Exception e) {
					e.getMessage();

				}
			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();

			}
		});
	}
}
