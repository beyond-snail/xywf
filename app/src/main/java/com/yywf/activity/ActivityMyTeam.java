package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.AdInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.ADCommonView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyTeam extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityMyTeam";
	private TextView tv_t_count_person;
	private TextView tv_activate_count;
	private TextView tv_no_activate_count;
	private RelativeLayout ll_team;
	private TextView id_team_person_count;
	private LinearLayout ll_advertis;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_myteam);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("我的团队");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {


		tv_t_count_person = textView(R.id.tv_t_count_person);
		tv_activate_count = textView(R.id.tv_activate_count);
		tv_no_activate_count = textView(R.id.tv_no_activate_count);
		id_team_person_count = textView(R.id.id_team_person_count);
		relativeLayout(R.id.ll_team).setOnClickListener(this);
		button(R.id.btn_next).setOnClickListener(this);


		// 广告栏开始
		ll_advertis = (LinearLayout) findViewById(R.id.advertis);
		// 设置宽度高度一致
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_advertis.getLayoutParams();
		linearParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2);// 640-370
		// 750-434
		ll_advertis.setLayoutParams(linearParams);

//		List<AdInfo> infos = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			AdInfo info = new AdInfo();
//			info.setPhoto_url("https://m.tourongjia.com/escrowwap/channelTemplateRed ");
//			infos.add(info);
//		}
//		ADCommonView adCommonView = new ADCommonView(mContext, infos, false);
//		ll_advertis.addView(adCommonView);

		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(R.drawable.mygroup_banner);
		ll_advertis.addView(imageView);

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


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ll_team:
				startActivity(new Intent(mContext, ActivityMyTeamDetail.class));
				break;
			case R.id.btn_next:
				finish();
				break;


		}
	}


	public void loadData(){
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.get(ConfigXy.XY_MY_TEAM, params, new HttpUtil.RequestListener() {
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
						ToastUtils.CustomShow(mContext, result.optString("message"));
					}else{
						JSONObject dataStr = result.getJSONObject("data");
						String proxyAmount = dataStr.optString("proxyAmount");
						String memberAmount = dataStr.optString("memberAmount");
						String commonAmount = dataStr.optString("commonAmount");
						String personCount = dataStr.optString("personCount");

						tv_t_count_person.setText(StringUtils.isBlank(proxyAmount) ? "0" : proxyAmount);
						tv_activate_count.setText(StringUtils.isBlank(memberAmount) ? "0" : memberAmount);
						tv_no_activate_count.setText(StringUtils.isBlank(commonAmount) ? "0" : commonAmount);
						id_team_person_count.setText(StringUtils.isBlank(personCount) ? "0人" : personCount+"人");
					}



				} catch (Exception e) {
					Log.e(TAG, "doCommit() Exception: " + e.getMessage());
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
