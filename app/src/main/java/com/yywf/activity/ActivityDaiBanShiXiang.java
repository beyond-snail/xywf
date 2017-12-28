package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.passwordView.KeyBoardDialog;
import com.tool.utils.passwordView.PayPasswordView;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterMessageNoticeNew;
import com.yywf.adapter.AdapterPlanList;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.Message;
import com.yywf.model.PlanList;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityDaiBanShiXiang extends BaseActivity {

	private final String TAG = "ActivityDaiBanShiXiang";






	private PullToRefreshScrollView mPullRefreshScrollView;
	private List<Message> messageList = new ArrayList<Message>();// 信息通知
	private AdapterMessageNoticeNew adapter;
	private MyListView myListView;
	private int page = 1;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_dbsx);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("待办事项");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();


	}

	private void initView() {

//		for (int i = 0; i < 3; i++){
//			Message message = new Message();
//			message.setHasRead(true);
//			message.setMemo("fdsjlfjsfjjkjfksdjfksjflsdjfkdsjfl");
//			message.setGmtCreate("2017-12-15 18:03:12");
//			messageList.add(message);
//		}
//
//		for (int i = 0; i < 3; i++){
//			Message message = new Message();
//			message.setHasRead(false);
//			message.setMemo("fdsjlfjsfjjkjfksdjfksjflsdjfkdsjfl");
//			message.setGmtCreate("2017-12-15 18:03:12");
//			messageList.add(message);
//		}



		myListView = findViewById(R.id.listview);
		adapter = new AdapterMessageNoticeNew(mContext, messageList);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			}
		});


		mPullRefreshScrollView =  findViewById(R.id.pull_refresh_scrollview);
		// 下拉刷新、上拉加载更多
		mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
		// TODO:必须先设置Mode后再设置刷新文本
		ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在载入...");// 刷新时
		startLabels.setReleaseLabel("释放立即刷新...");// 下来达到一定距离时，显示的提示
		// TODO:必须先设置Mode后再设置刷新文本
		ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("释放立即加载...");// 下来达到一定距离时，显示的提示
		mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 下拉刷新
				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于：" + label);
				reloadData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 上拉加载更多
				String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("更新于：" + label);

				if (messageList.size() == 0) {
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							showErrorMsg("没有更多了");
							// ListScrollUtil.setGridViewHeightBasedOnChildren(gridView);
							mPullRefreshScrollView.onRefreshComplete();
						}
					}, 1000);

				} else {
					page++;
					loadData(false);
				}
			}
		});
	}





	/**
	 * 重新加载数据
	 */
	private void reloadData() {
		Log.d(TAG, "reloadData()");
		page = 1;
		messageList.clear();
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
					if (!result.optBoolean("status")) {
						// showErrorMsg(result.getString("message"));
						mPullRefreshScrollView.onRefreshComplete();
						return;
					}
					if (result.getString("data") != null) {
						String str = result.getString("data");

						Gson gson = new Gson();
						// json数据转换成List
						List<Message> datas = gson.fromJson(str, new TypeToken<List<Message>>() {
						}.getType());
						messageList.addAll(datas);
						adapter.notifyDataSetChanged();
						if (messageList.size() > 0) {
							linearLayout(R.id.id_no_data).setVisibility(View.GONE);
							 mPullRefreshScrollView.setVisibility(View.VISIBLE);
						} else {
							linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
							 mPullRefreshScrollView.setVisibility(View.GONE);
						}
					}
					// 刷新完成
					mPullRefreshScrollView.onRefreshComplete();

				} catch (JSONException e) {
					e.printStackTrace();
					mPullRefreshScrollView.onRefreshComplete();
				}
			}

			@Override
			public void failed(Throwable error) {
				mPullRefreshScrollView.onRefreshComplete();
				showE404();
				disShowProgress();
			}
		});
	}







	@Override
	protected void onResume() {
		super.onResume();
//		reloadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}










}
