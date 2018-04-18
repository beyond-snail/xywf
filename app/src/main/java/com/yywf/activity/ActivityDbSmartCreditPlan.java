package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterPlanList;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.model.PlanInfo;
import com.yywf.model.PlanList;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityDbSmartCreditPlan extends BaseActivity{

	private final String TAG = "ActivitySmartCreditPlan";


	private TextView tv_total_amt;
	private TextView tv_total_fee;
	private TextView tv_sigle_amt;

	private LinearLayout ll_plan_amt;


	private List<PlanList> list = new ArrayList<PlanList>();
	private AdapterPlanList adapter;
	private MyListView myListView;
	private int page = 1;



	private BankCardInfo vo;

	private PlanInfo info;


	private long planId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_db_credit_plan);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("还款计划列表");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}



		planId = getIntent().getLongExtra("planId", 0);


		initView();


	}





	private void initView() {

		tv_total_amt = textView(R.id.id_tv_total_amt);
		tv_total_fee = textView(R.id.id_tv_total_fee);
		tv_sigle_amt = textView(R.id.id_tv_sigle_amt);









		myListView = findViewById(R.id.listview);
		adapter = new AdapterPlanList(mContext, list);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});

	}










	private void errorDialog(String message){
		MyDialog.Builder builder = new MyDialog.Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	private void loadData() {


		showProgress("加载中...");
		String url = ConfigXy.XY_PREVIEW_PLAN;
		RequestParams params = new RequestParams();
		params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.put("token", UtilPreference.getStringValue(mContext, "token"));
		params.put("planId", planId);

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
						errorDialog(result.getString("message"));
						return;
					}
					if (!StringUtils.isBlank(result.getString("data"))) {
						String str = result.getString("data");

						//再次解析
						if (StringUtils.isBlank(str)){
							errorDialog("数据返回为空");
							return;
						}

						JSONObject obj = new JSONObject(str);
						if (StringUtils.isBlank(obj.optString("data"))){
							errorDialog("数据返回为空");
							return;
						}

						Gson gson = new Gson();
						// json数据转换成List
						info = gson.fromJson(obj.optString("data"), new TypeToken<PlanInfo>() {}.getType());

						if (info == null){
							errorDialog("数据解析失败");
							return;
						}
						if (info.getPlan() == null){
							errorDialog("无预览计划");
							return;
						}
						list.clear();
						//组装数据
						for (int i = 0; i < info.getPlan().size(); i++){
							PlanList planList = new PlanList();
							planList.setFeeAmt(info.getPlan().get(i).getConsume().getReal_payment());
							planList.setTime2(info.getPlan().get(i).getConsume().getConsume_at());
							planList.setAmt(info.getPlan().get(i).getRepay().getReal_payment());
							planList.setTime1(info.getPlan().get(i).getRepay().getRepay_at());
							list.add(planList);
						}

						adapter.notifyDataSetChanged();
						if (list.size() > 0) {
							linearLayout(R.id.id_no_data).setVisibility(View.GONE);
							linearLayout(R.id.ll_plan_amt).setVisibility(View.VISIBLE);

							//总金额
//							tv_total_amt.setText(MoneyUtil.formatMoney(et_amt.getMoneyText()));
//							tv_total_fee.setText("万85+1*10");
							//卡内最低余额（还款总额*10% + 手续费）
							// tv_sigle_amt.setText("计划执行需要"+MoneyUtil.formatMoney(minBalanceAmt)+"元");
						} else {
							linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
							linearLayout(R.id.ll_plan_amt).setVisibility(View.GONE);
							startActivityForResult(new Intent(mContext, ActivityCreditSupply.class), 0);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable error) {
				showE404();
				disShowProgress();
			}
		});
	}








	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}










}
