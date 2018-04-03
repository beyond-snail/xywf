package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.AgentInfo;
import com.yywf.model.MyWalletInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

public class ActivityAgent extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityAgent";


	private LinearLayout ll_agent;
	private MoneyEditText et_amt;
	private EditText et_fr;


	private TextView tv_vip;
	private TextView tv_fl;
	private TextView tv_fr;
	private TextView tv_name;

	private AgentInfo vo;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_agent);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("代理商专属通道");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		if (findViewById(R.id.img_right) != null){
			findViewById(R.id.img_right).setVisibility(View.VISIBLE);
			findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 9));
				}
			});
		}

		initView();



	}




	private void initView(){

		linearLayout(R.id.ll_agent).setOnClickListener(this);
		button(R.id.btn_commit).setOnClickListener(this);

//		tv_vip = textView(R.id.tv_vip);
		tv_fl = textView(R.id.tv_fl);
		tv_fr = textView(R.id.tv_fr);
		tv_name = textView(R.id.tv_name);


		et_fr = editText(R.id.et_fr);
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

		String url = ConfigXy.XY_AGENT_INFO;
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


					JSONObject obj = result.getJSONObject("data");
					String profitratio = String.valueOf(obj.optInt("profitratio"));
					String cashback = String.valueOf(obj.optInt("cashback"));


					tv_fr.setText(StringUtils.isBlank(profitratio) ? "0" : profitratio);
					tv_fl.setText(StringUtils.isBlank(StringUtils.formatMoney(cashback)) ? "0.00" : StringUtils.formatMoney(cashback));


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
			case R.id.ll_agent:
				startActivityForResult(new Intent(mContext, ActivityAgentSearch.class), 1);
				break;
			case R.id.btn_commit:
				doCommit();
				break;
		}
	}

	private void doCommit() {

		if (StringUtils.isBlank(et_amt.getMoneyText().trim())){
			ToastUtils.showShort(mContext, "请输入返利金额");
			return;
		}
		if (StringUtils.isBlank(et_fr.getText().toString().trim())){
			ToastUtils.showShort(mContext, "请输入分润比例");
			return;
		}


		showProgress("加载中...");

		String url = ConfigXy.XY_AGENT_UPDATE_INFO;
		RequestParams params = new RequestParams();
		params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.put("token", UtilPreference.getStringValue(mContext, "token"));
		params.put("agentId", vo.getAgentId());
		params.put("twoProfit", et_fr.getText().toString().trim());
		params.put("twoRebate", StringUtils.changeY2F(et_amt.getMoneyText().trim()));

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
					tv_name.setText("请选择代理商");
					et_fr.setText("");
					et_amt.setText("");

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 2){
			vo = (AgentInfo) data.getSerializableExtra("agentInfo");
			if (!StringUtils.isBlank(vo.getName())) {
				tv_name.setText(vo.getName());
			}
			if (vo.getTwoProfit() != 0){
				et_fr.setText(vo.getTwoProfit()+"");
			}
			if (vo.getTwoRebate() != 0){
				et_amt.setText(StringUtils.formatIntMoney(vo.getTwoRebate()));
			}
		}
	}
}
