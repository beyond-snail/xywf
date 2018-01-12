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
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.model.PlanList;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
			//总金额 转成分
			int amount = Integer.valueOf(StringUtils.changeY2F(et_amt.getMoneyText().trim()));
			//手续费
			int feeAmt = (amount * 85)/10000;
			textView(R.id.tv_tx_balance_amt).setText(StringUtils.formatIntMoney(feeAmt));
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

		//账单日 10天内设置计划
		int zdDay = Integer.valueOf(vo.getZdDay());
		int hkDay = Integer.valueOf(vo.getHkDay());
		int curDay = Integer.valueOf(StringUtils.getCurDay());
		String dateDay="";
		String dateMonth="";


		//当前天数 小于账单日，那么设置后在账单日后一天开始执行计划
		if (curDay < zdDay){
			dateDay = StringUtils.getCurrentDate("yyyy-MM") + "-" + vo.getZdDay();
		}else{
			//从当前天数开始往后 10天，大于还款日则到下一个月的账单日后一天执行
			if (curDay + 10 > hkDay){
//				ToastUtils.CustomShow(mContext, "无法设置计划");
//				return;
				dateDay = StringUtils.monthCalculate(StringUtils.getCurrentDate("yyyy-MM"), 1) + "-" + vo.getZdDay();
			}else{
				DecimalFormat df = new DecimalFormat("00");
				String str_m = df.format(curDay);
				dateDay = StringUtils.getCurrentDate("yyyy-MM") + "-" + str_m;
			}
		}


		linearLayout(R.id.ll_plan_amt).setVisibility(View.VISIBLE);

		//总金额 转成分
		int amount = Integer.valueOf(StringUtils.changeY2F(moneyText.trim()));
		tv_total_amt.setText(StringUtils.formatIntMoney(amount));

		//手续费
		int feeAmt = (amount * 85)/10000;
		tv_total_fee.setText(StringUtils.formatIntMoney(feeAmt));

		//卡内最低余额(还款总额*10% + 手续费)
		int minBalanceAmt = (amount*10)/100 + feeAmt;
		tv_sigle_amt.setText("执行计划需要"+StringUtils.formatIntMoney(minBalanceAmt)+"元");

		//分10期，前9次一样
		list.clear();
		for (int i = 0; i < 10; i++){
			PlanList planList = new PlanList();
			planList.setAmt(amount/10);
			planList.setFeeAmt(amount/10);
			planList.setTime1(StringUtils.dayCalculate(dateDay, i+1)+" "+"09:30:00");
			planList.setTime2(StringUtils.dayCalculate(dateDay, i+1)+" "+"14:59:00");
			list.add(planList);
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
