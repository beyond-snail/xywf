package com.yywf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.sortlistview.SortModel;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.adapter.BankListAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityBankCardList extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityBankCardList";

    private PullToRefreshListView listview;
    private BankListAdapter bankAdapter;
    private List<BankCardInfo> bankList = new ArrayList<BankCardInfo>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_bankcard_list);
		MyActivityManager.getInstance().addActivity(this);
        if (textView(R.id.tv_header) != null) {
            textView(R.id.tv_header).setText("银行卡列表");
        }
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent();
                    // 设置结果，并进行传送
                    ActivityBankCardList.this.setResult(4, mIntent);
                    finish();
                }
            });
        }
        if (findViewById(R.id.img_right_add) != null) {
            findViewById(R.id.img_right_add).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right_add).setOnClickListener(this);
        }
		initView();
	}


	private void initView() {


        listview = (PullToRefreshListView)findViewById(R.id.pull_refresh_listView);
        bankAdapter = new BankListAdapter((Activity) mContext, bankList, 3);
        listview.setAdapter(bankAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemClick: "+i);
                Intent mIntent = new Intent();
                mIntent.putExtra("bankInfo", bankList.get(i-1));
                // 设置结果，并进行传送
                ActivityBankCardList.this.setResult(2, mIntent);
                finish();
            }
        });

        // 下拉刷新、上拉加载更多
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        // TODO:必须先设置Mode后再设置刷新文本
        ILoadingLayout startLabels = listview.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("释放立即刷新...");// 下来达到一定距离时，显示的提示
        // TODO:必须先设置Mode后再设置刷新文本
        ILoadingLayout endLabels = listview.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("释放立即加载...");// 下来达到一定距离时，显示的提示
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于：" + label);
                //请求
                reloadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉加载更多
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("更新于：" + label);

                handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showErrorMsg("没有更多了");
                            listview.onRefreshComplete();
                        }
                    }, 1000);

            }
        });

	}

    private void reloadData() {
        bankList.clear();
        loadData(true);
    }


    private void loadData(boolean showProgress) {
        if (showProgress) {
            showProgress("加载中...");
        }
        String url = ConfigXy.XY_BANK_INFO_LIST;
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("type", "2");
        params.put("token", UtilPreference.getStringValue(mContext, "token"));

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
                        // 下拉刷新完成
                        listview.onRefreshComplete();
                        return;
                    }

                    JSONObject obj = result.getJSONObject("data");
                    String bank_list = obj.optString("bank_list");
                    if (!StringUtils.isBlank(bank_list)) {

                        Gson gson = new Gson();
                        List<BankCardInfo> bankCardInfoList = gson.fromJson(bank_list, new TypeToken<List<BankCardInfo> >() {
                        }.getType());
                        if (bankCardInfoList.size() > 0) {
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            bankList.addAll(bankList.size(), bankCardInfoList);

                        } else {
                            if (bankList.size() > 0){
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            }else{
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        if (bankList.size() > 0){
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                        }else{
                            linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                        }
                    }



                    bankAdapter.notifyDataSetChanged();
                    // 下拉刷新完成
                    listview.onRefreshComplete();

                } catch (Exception e) {
                    e.getMessage();
                    listview.onRefreshComplete();
                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showE404();
                listview.onRefreshComplete();
            }
        });
    }



	@Override
	protected void onResume() {
		super.onResume();
        reloadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

            case R.id.img_right_add:
                startActivity(new Intent(mContext, ActivityCreditAdd.class));
                break;
		default:
			break;
		}
	}



}
