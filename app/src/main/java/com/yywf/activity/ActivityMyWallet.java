package com.yywf.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.MyWalletInfo;
import com.yywf.model.WalletInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActivityMyWallet extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyWallet";
	private TextView tv_balance_amt;
	private TextView tv_fl_amt;
	private TextView tv_fr_amt;
	private TextView tv_jlj_amt;
	private TextView tv_amount;
	private TextView tv_count;


	private TextView tv_yesterday_trans;
	private TextView tv_accumulate_trans;

	private String totalRatio;
	private String totalAmt;
	private String yesterdayTotalRatio;
	private String yerterdayTotalAmt;


	private View view;
	private RadioGroup radioGroup;
	private RadioButton btn1;
	private RadioButton btn2;
	private RadioButton btn3;

	private MyCustomDialog myCustomDialog;




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
		tv_fl_amt = textView(R.id.tv_fl_amt);
		tv_jlj_amt = textView(R.id.tv_jlj_amt);

		tv_amount = textView(R.id.tv_amount);
		tv_count = textView(R.id.tv_count);



		tv_yesterday_trans = textView(R.id.tv_yesterday_trans);
		tv_yesterday_trans.setOnClickListener(this);
		tv_accumulate_trans = textView(R.id.tv_accumulate_trans);
		tv_accumulate_trans.setOnClickListener(this);

		relativeLayout(R.id.ll_fr).setOnClickListener(this);
		relativeLayout(R.id.ll_fh).setOnClickListener(this);
		relativeLayout(R.id.ll_jlj).setOnClickListener(this);
		relativeLayout(R.id.ll_mx).setOnClickListener(this);

		textView(R.id.btn_tx).setOnClickListener(this);
		textView(R.id.btn_sale).setOnClickListener(this);


//		tv_balance_amt.setText("123456789012");


		//加载模式框
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.dialog_tx_choice, null);
		radioGroup = view.findViewById(R.id.radioGroup1);
		btn1 = view.findViewById(R.id.button1);
		btn2 = view.findViewById(R.id.button2);
		btn3 = view.findViewById(R.id.button3);

		//默认手动还款
//		btn1.setChecked(true);

		final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
				R.style.MyDialogStyleBottom);
		customBuilder.setCanceledOnTouchOutside(true);
		customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
		customBuilder.setContentView(view);
		customBuilder.setDisBottomButton(true);
		myCustomDialog = customBuilder.create();


		//当改变的时候
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
				if (i == btn1.getId()){
					int jljAmt = Integer.valueOf(StringUtils.changeY2F(tv_jlj_amt.getText().toString().trim()));
					startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("type", 1).putExtra("JljBalance", jljAmt));
				}else if (i == btn2.getId()){
					int flAmt = Integer.valueOf(StringUtils.changeY2F(tv_fl_amt.getText().toString().trim()));
					startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("type", 2).putExtra("FlBalance", flAmt));
				}else if (i == btn3.getId()){
					int frAmt = Integer.valueOf(StringUtils.changeY2F(tv_fr_amt.getText().toString().trim()));
					startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("type", 3).putExtra("FrBalance", frAmt));
				}
				myCustomDialog.dismiss();
				btn1.setChecked(false);
				btn2.setChecked(false);
				btn3.setChecked(false);

			}
		});

	}




	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}


	private void loadData() {

		showProgress("加载中...");

		String url = ConfigXy.XY_WALLET_INFO;
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));

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



					Gson gson = new Gson();
					MyWalletInfo walletInfo = gson.fromJson(result.optString("data"), new TypeToken<MyWalletInfo>() {
					}.getType());

					tv_balance_amt.setText(walletInfo.getBalance());
					tv_jlj_amt.setText(walletInfo.getRewardAmt());
					tv_fl_amt.setText(walletInfo.getProfitAmt());
					tv_fr_amt.setText(walletInfo.getRebateAmt());

					tv_amount.setText(walletInfo.getYerterdayTotalAmt());
					tv_count.setText(walletInfo.getYesterdayTotalRatio());

					totalRatio = walletInfo.getTotalRatio();
					totalAmt = walletInfo.getTotalAmt();
					yesterdayTotalRatio = walletInfo.getYesterdayTotalRatio();
					yerterdayTotalAmt = walletInfo.getYerterdayTotalAmt();


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
			case R.id.ll_mx:
				startActivity(new Intent(mContext, ActivityMyMx.class));
				break;
			case R.id.tv_yesterday_trans:
				setUI(1);

				break;
			case R.id.tv_accumulate_trans:
				setUI(2);
				break;
			case R.id.btn_tx:
//				startActivity(new Intent(mContext, ActivityMyTx.class).putExtra("balance", tv_balance_amt.getText().toString().trim()));
				myCustomDialog.show();
				break;
			case R.id.btn_sale:
				startActivity(new Intent(mContext, ActivityVipGrade3.class));
				break;

		}
	}


	/**
	 * @Description 切换 基本信息 详细信息 评论信息
	 * @param i
	 */
	private void setUI(int i) {
		tv_yesterday_trans.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
		tv_yesterday_trans.setBackgroundResource(R.drawable.btn_yellow_selector2);
		tv_accumulate_trans.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
		tv_accumulate_trans.setBackgroundResource(R.drawable.btn_yellow_selector2);

		switch (i) {
			case 1:// 切换到昨日分润
				tv_yesterday_trans.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
				tv_yesterday_trans.setBackgroundResource(R.drawable.btn_yellow_selector);

				tv_amount.setText(yerterdayTotalAmt);
				tv_count.setText(yesterdayTotalRatio);

				break;
			case 2:// 切换到累计分润
				tv_accumulate_trans.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
				tv_accumulate_trans.setBackgroundResource(R.drawable.btn_yellow_selector);

				tv_amount.setText(totalAmt);
				tv_count.setText(totalRatio);
				break;
			default:
				break;
		}



	}
}
