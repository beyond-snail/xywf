package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.utils.L;
import com.tool.utils.passwordView.KeyBoardDialog;
import com.tool.utils.passwordView.PayPasswordView;
import com.tool.utils.utils.ArithUtil;
import com.tool.utils.utils.KeyBoardUtils;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.MoneyUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyListView;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xyzlf.share.library.util.ToastUtil;
import com.yywf.R;
import com.yywf.adapter.AdapterPlanList;
import com.yywf.adapter.BankListAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.model.PlanList;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitySmartCreditPlan extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivitySmartCreditPlan";

	private MoneyEditText et_amt;

	private TextView tv_do_text;
	private TextView tv_total_amt;
	private TextView tv_total_fee;
	private TextView tv_sigle_amt;

	private LinearLayout ll_plan_amt;


	private Button btn;


	private View view;
	private RadioGroup radioGroup;
	private RadioButton btn1;
	private RadioButton btn2;

	private  MyCustomDialog myCustomDialog;

//	private ScrollView mPullRefreshScrollView;
	private List<PlanList> list = new ArrayList<PlanList>();
	private AdapterPlanList adapter;
	private MyListView myListView;
	private int page = 1;


	private KeyBoardDialog keyboard;

	private BankCardInfo vo;

	private boolean isAction = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_setting_credit_plan);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("设置还款计划");
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

		initListPlan();

	}

	private void initListPlan() {

//		for (int i = 0; i < 12; i++){
//			PlanList planList = new PlanList();
//			planList.setAmt(500);
//			planList.setFeeAmt(21);
//			planList.setTime1("2017-12-15 13:59:00");
//			planList.setTime2("2017-12-15 13:59:00");
//			list.add(planList);
//		}


//		linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);





//		mPullRefreshScrollView =  findViewById(R.id.pull_refresh_scrollview);
		// 下拉刷新、上拉加载更多
//		mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
//		// TODO:必须先设置Mode后再设置刷新文本
//		ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
//		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
//		startLabels.setRefreshingLabel("正在载入...");// 刷新时
//		startLabels.setReleaseLabel("释放立即刷新...");// 下来达到一定距离时，显示的提示
//		// TODO:必须先设置Mode后再设置刷新文本
//		ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
//		endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
//		endLabels.setRefreshingLabel("正在载入...");// 刷新时
//		endLabels.setReleaseLabel("释放立即加载...");// 下来达到一定距离时，显示的提示
//		mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				// 下拉刷新
//				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//				refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于：" + label);
//				reloadData();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				// 上拉加载更多
//				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//				refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("更新于：" + label);
//
//				if (list.size() == 0) {
//					handler.postDelayed(new Runnable() {
//
//						@Override
//						public void run() {
//							showErrorMsg("没有更多了");
//							// ListScrollUtil.setGridViewHeightBasedOnChildren(gridView);
//							mPullRefreshScrollView.onRefreshComplete();
//						}
//					}, 1000);
//
//				} else {
//					page++;
//					loadData(false);
//				}
//			}
//		});
	}


	private Handler handler = new Handler();
	/**
	 * 延迟线程，看是否还有下一个字符输入
	 */
	private Runnable delayRun = new Runnable() {

		@Override
		public void run() {
//			//总金额 转成分
//			int amount = Integer.valueOf(StringUtils.changeY2F(et_amt.getMoneyText().trim()));
//			//手续费
//			int feeAmt = (amount * ConstApp.FEE)/10000;
//			textView(R.id.tv_tx_balance_amt).setText(StringUtils.formatIntMoney(feeAmt));

			//总金额
//			tv_total_amt.setText(MoneyUtil.formatMoney(et_amt.getMoneyText().trim()));
			if (StringUtils.isBlank(et_amt.getMoneyText())){
				textView(R.id.tv_tx_balance_amt).setText("0.00");
				return;
			}

			//手续费
			String feeAmt = MoneyUtil.moneydiv(MoneyUtil.moneyMul(et_amt.getMoneyText().trim(), ConstApp.FEE+""), "10000");
			textView(R.id.tv_tx_balance_amt).setText(feeAmt);

		}
	};

	private void initView() {

		tv_do_text = textView(R.id.tv_do_text);
		tv_total_amt = textView(R.id.id_tv_total_amt);
		tv_total_fee = textView(R.id.id_tv_total_fee);
		tv_sigle_amt = textView(R.id.id_tv_sigle_amt);


		et_amt = (MoneyEditText) editText(R.id.et_amt);
		et_amt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(delayRun!=null){
					//每次editText有变化的时候，则移除上次发出的延迟线程
					handler.removeCallbacks(delayRun);
				}


				if (linearLayout(R.id.ll_plan_amt).getVisibility() == View.VISIBLE){
					linearLayout(R.id.ll_plan_amt).setVisibility(View.GONE);
					reset();
				}

				//延迟800ms，如果不再输入字符，则执行该线程的run方法
				handler.postDelayed(delayRun, 500);
			}
		});
//		KeyBoardUtils.closeKeybord(et_amt, mContext);





		relativeLayout(R.id.ll_do_model).setOnClickListener(this);
		btn = button(R.id.btn_commit);
		btn.setText("预览计划");
		btn.setOnClickListener(this);


		//加载模式框
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.dialog_credit_choice, null);
		radioGroup = view.findViewById(R.id.radioGroup1);
		btn1 = view.findViewById(R.id.button1);
		btn2 = view.findViewById(R.id.button2);

		//默认手动还款
//		btn1.setChecked(true);
//		tv_do_text.setText(btn1.getText().toString());

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
					tv_do_text.setText(btn1.getText().toString());
					myCustomDialog.dismiss();
				}else if (i == btn2.getId()){
					tv_do_text.setText(btn2.getText().toString());
					myCustomDialog.dismiss();
				}
			}
		});

		myListView = findViewById(R.id.listview);
		adapter = new AdapterPlanList(mContext, list);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});

		isAction = false;
	}



	/**
	 * 重新加载数据
	 */
	private void reloadData() {
		Log.d(TAG, "reloadData()");
		page = 1;
		list.clear();
		loadData(true);
	}


	private void loadData(boolean showProgress) {



		// String url = ConfigApp.HC_GET_STORE_GOODS;
		String url = ConfigXy.PLAN_LIST;
		RequestParams params = new RequestParams();

//		String id = UtilPreference.getStringValue(mContext, "zf_member_id");
//		params.add("memberId", id);
//		params.add("token", UtilPreference.getStringValue(mContext, "token"));
//		params.add("page", page + "");// 当前页
//		params.add("key", editText(R.id.query).getText().toString());
//		params.add("sortType", sort_qb + "");
//		if (cId != 0) {
//			params.add("categroyId", cId + "");
//		}
//		if (showProgress) {
//			showProgress("加载中...");
//		}

		HttpUtil.get(url, params, new HttpUtil.RequestListener() {

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
						// showErrorMsg(result.getString("message"));
						return;
					}
					if (result.getString("data") != null) {
						String str = result.getString("data");

						Gson gson = new Gson();
						// json数据转换成List
						List<PlanList> datas = gson.fromJson(str, new TypeToken<List<PlanList>>() {
						}.getType());
						list.addAll(datas);
						adapter.notifyDataSetChanged();
						if (list.size() > 0) {
							linearLayout(R.id.id_no_data).setVisibility(View.GONE);
							// mPullRefreshScrollView.setVisibility(View.VISIBLE);
							btn.setText("执行计划");
						} else {
							linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
							// mPullRefreshScrollView.setVisibility(View.GONE);
							startActivityForResult(new Intent(mContext, ActivityCreditSupply.class), 0);
						}
					}
					// 刷新完成
//					mPullRefreshScrollView.onRefreshComplete();

				} catch (JSONException e) {
					e.printStackTrace();
//					mPullRefreshScrollView.onRefreshComplete();
				}
			}

			@Override
			public void failed(Throwable error) {
//				mPullRefreshScrollView.onRefreshComplete();
				showE404();
				disShowProgress();
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		if (requestCode == 0) {
			btn.setText("执行计划");
		}


	}


	private void reset(){
		isAction = false;
		list.clear();
		adapter.notifyDataSetChanged();
		btn.setText("预览计划");
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
			case R.id.btn_commit:

				if (!isAction) {
					if (StringUtils.isBlank(vo.getId())) {
						//补全信息
						startActivityForResult(new Intent(mContext, ActivityCreditSupply.class).putExtra("bankbillId", vo.getBankbillId()), 0);
						return;
					}

					//预览计划
					if (StringUtils.isBlank(et_amt.getText().toString().trim())) {
						ToastUtils.CustomShow(mContext, "请输入金额");
						return;
					}

					//设置模式
					if (StringUtils.isBlank(tv_do_text.getText().toString().trim())) {
						ToastUtils.CustomShow(mContext, "请设置模式");
						return;
					}

					//预览计划
					setPlanList(et_amt.getMoneyText());
					isAction = true;
				}else {

					//执行计划
					ToastUtils.CustomShow(mContext, "执行计划");

				}



				break;
			case R.id.ll_do_model:
				myCustomDialog.show();
				break;

		}
	}

	private void setPlanList(String moneyText) {

		//账单日 6天内设置计划
		int zdDay = Integer.valueOf(vo.getZdDay());
		int hkDay = Integer.valueOf(vo.getHkDay());
		int curDay = Integer.valueOf(StringUtils.getCurDay());
		String dateDay="";

		linearLayout(R.id.ll_plan_amt).setVisibility(View.VISIBLE);



		//当前天数 小于账单日，那么设置后在账单日后一天开始执行计划
		if (curDay < zdDay){
			dateDay = StringUtils.getCurrentDate("yyyy-MM") + "-" + vo.getZdDay();
		}else{
			//还款日前-5天内，无法设置计划
			if (curDay + 5 > hkDay){
				ToastUtils.CustomShow(mContext, "还款日前5天无法执行计划");
				return;
			}else{
				DecimalFormat df = new DecimalFormat("00");
				String str_m = df.format(curDay);
				dateDay = StringUtils.getCurrentDate("yyyy-MM") + "-" + str_m;
			}
		}



		//总金额
		tv_total_amt.setText(MoneyUtil.formatMoney(moneyText.trim()));

		//手续费
		String feeAmt = MoneyUtil.moneydiv(MoneyUtil.moneyMul(moneyText, ConstApp.FEE+""), "10000");
		tv_total_fee.setText(MoneyUtil.formatMoney(feeAmt));

		//卡内最低余额（还款总额*10% + 手续费）
		String minBalanceAmt = MoneyUtil.moneyAdd(MoneyUtil.moneydiv(MoneyUtil.moneyMul(moneyText, "10"), "100"), feeAmt);
		tv_sigle_amt.setText("执行计划需要"+MoneyUtil.formatMoney(minBalanceAmt)+"元");



		/**
		 * 假设我这笔的消费系数是9%，那么我消费出来的金额=5000*9%+5000*9%*万八五（这个是手续费）+1元清算费；而还款金额只有5000*9%
		 */
		//分10期，前9次随机系数计算
		list.clear();

		String lastSaleAmt = "";
		String lastCreditAmt = "";

		for (int i = 0; i < 9; i++){
			PlanList planList = new PlanList();

			//还款金额
			String creditAmt = MoneyUtil.moneydiv(MoneyUtil.moneyMul(moneyText,  StringUtils.getRandIntNum(ConstApp.COFFICIENT)+""), "1000");
			planList.setAmt(creditAmt);

			//消费金额
			String saleAmt = MoneyUtil.moneyAdd(creditAmt, MoneyUtil.moneydiv(MoneyUtil.moneyMul(creditAmt, ConstApp.FEE+""), "10000"));
			planList.setFeeAmt(saleAmt);

			//前9次消费金额的总和
			lastSaleAmt = MoneyUtil.moneyAdd(MoneyUtil.formatMoney(lastSaleAmt), saleAmt);
			//前9次还款金额的总和
			lastCreditAmt = MoneyUtil.moneyAdd(MoneyUtil.formatMoney(lastCreditAmt), creditAmt);

			list.add(planList);
		}

		//计算 最后一笔
		PlanList planList1 = new PlanList();
		planList1.setAmt(MoneyUtil.moneySub(moneyText, lastCreditAmt));
		planList1.setFeeAmt(MoneyUtil.moneySub(moneyText, lastSaleAmt));
		list.add(planList1);





		//当前时间是否大于17:30，如果大于那么第一笔消费，会在第二天的下午17:31~21：00执行，其他的计划不变
		//当天执行只执行消费1
		//07:00~10:30 还 1、3、5、7、9
		//10:00~14:00 消 2、4、6、8、10
		//14:01~17:30 还 2、4、6、8、10
		//17:31~21:00 消 3、5、7、9

		int index = 0;
		//还款
		for (int i= 0; i < list.size(); i++){
			if (i % 2 != 0){
				//还款
				Date randCreditTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay, index)+" "+"14:01:00", StringUtils.dayCalculate(dateDay, index)+" "+"17:30:00");
				list.get(i).setTime1(StringUtils.getStringFromDate(randCreditTime, "yyyy-MM-dd HH:mm:ss"));
			}else{
				index++;
				//还款
				Date randCreditTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay, index)+" "+"07:00:00", StringUtils.dayCalculate(dateDay, index)+" "+"10:30:00");
				list.get(i).setTime1(StringUtils.getStringFromDate(randCreditTime, "yyyy-MM-dd HH:mm:ss"));
			}
			LogUtils.e("还款"+(i+1)+"="+list.get(i).getTime1());
		}


		//判断消费时间
		int type = StringUtils.compareDate(StringUtils.getFormatCurTime(), StringUtils.getCurDate() + "173000", "yyyyMMddHHmmss");
		if (type == 1){//当前时间大于17:30
			//消费1 在隔天下午执行
			Date randSaleTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay,  1) + " " + "17:30:00", StringUtils.dayCalculate(dateDay, + 1) + " " + "21:00:00");
			list.get(0).setTime2(StringUtils.getStringFromDate(randSaleTime, "yyyy-MM-dd HH:mm:ss"));
		}else{
			Date randSaleTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay,  0) + " " + "17:30:00", StringUtils.dayCalculate(dateDay, + 1) + " " + "21:00:00");
			list.get(0).setTime2(StringUtils.getStringFromDate(randSaleTime, "yyyy-MM-dd HH:mm:ss"));
		}
		LogUtils.e("消费"+(1)+"="+list.get(0).getTime2());
		index = 0;
		for (int i = 0; i < list.size()-1; i++){
			if (i % 2 != 0){
				Date randSaleTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay, index)+" "+"17:31:00", StringUtils.dayCalculate(dateDay, index)+" "+"21:00:00");
				list.get(i+1).setTime2(StringUtils.getStringFromDate(randSaleTime, "yyyy-MM-dd HH:mm:ss"));
			}else {
				index++;
				//消费
				Date randSaleTime = StringUtils.randomDate(StringUtils.dayCalculate(dateDay, index) + " " + "10:00:00", StringUtils.dayCalculate(dateDay, index) + " " + "14:00:00");
				list.get(i+1).setTime2(StringUtils.getStringFromDate(randSaleTime, "yyyy-MM-dd HH:mm:ss"));
			}

			LogUtils.e("消费"+(i+2)+"="+list.get(i+1).getTime2());
		}




		adapter.notifyDataSetChanged();

		btn.setText("执行计划");



	}


	private void doCommit(){
		keyboard = new KeyBoardDialog((Activity) mContext, getDecorViewDialog());
		keyboard.show();
	}



	protected View getDecorViewDialog() {

		return PayPasswordView.getInstance("", mContext, new PayPasswordView.OnPayListener() {

			@Override
			public void onSurePay(final String password) {// 这里调用验证密码是否正确的请求

				// TODO Auto-generated method stub
				keyboard.dismiss();
				keyboard = null;

				showProgress("加载中...");
				RequestParams params = new RequestParams();
//		params.add("account", username);
//		params.add("password", password);
				HttpUtil.get(ConfigXy.XY_SMRZ, params, requestListener);

			}

			@Override
			public void onForgotPwd() {
				startActivity(new Intent(mContext, ActivityForgotPayPwd.class));
			}

			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				keyboard.dismiss();
				keyboard = null;
				ToastUtils.showShort(getApplicationContext(), "交易已取消");
			}
		}).getView();
	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {
				JSONObject obj = new JSONObject(response);


			} catch (Exception e) {
				Log.e(TAG, "doCommit() Exception: " + e.getMessage());
			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};



}
