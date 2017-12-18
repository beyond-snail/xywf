package com.yywf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.adapter.AdapterWallet;
import com.yywf.adapter.BankListAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.model.WalletInfo;
import com.yywf.model.WalletListInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyBenefit extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMyBenefit";

    private PullToRefreshListView listview;
    private AdapterWallet adapterWallet;
    private List<WalletListInfo> walletInfos = new ArrayList<WalletListInfo>();

    private RelativeLayout ll_yesterday_benefit;
    private LinearLayout ll_accumulate_benefit;
    private TextView tv_yesterday_benefit;
    private TextView tv_accumulate_benefit;
    private TextView tv_yab_amt;
    private TextView tv_hab_amt;


    private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_my_fr);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("我的分润");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();
        //加载储蓄卡的信息
//        loadDebitCardInfo();
	}


	private void initView() {

        tv_yesterday_benefit = textView(R.id.tv_yesterday_benefit);
        tv_yesterday_benefit.setOnClickListener(this);
        tv_accumulate_benefit = textView(R.id.tv_accumulate_benefit);
        tv_accumulate_benefit.setOnClickListener(this);

        ll_yesterday_benefit = relativeLayout(R.id.ll_yesterday_benefit);
        ll_accumulate_benefit = linearLayout(R.id.ll_accumulate_benefit);

        tv_yab_amt = textView(R.id.tv_yab_amt);
        tv_hab_amt = textView(R.id.tv_hab_amt);



        //测试数据
        for (int i =0; i < 2; i++){
            WalletListInfo info = new WalletListInfo();
            info.setAmt(222222);
            info.setTime("2018-1-1 12:12:22");
            info.setName("吴从鹏");
            walletInfos.add(info);
        }



        listview = (PullToRefreshListView)findViewById(R.id.pull_refresh_listView);
        adapterWallet = new AdapterWallet( mContext, walletInfos);
        listview.setAdapter(adapterWallet);

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

                if (walletInfos.size() == 0) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showErrorMsg("没有更多了");
                            listview.onRefreshComplete();
                        }
                    }, 1000);

                } else {
                    page++;
                    loadData(false);
                }
            }
        });

	}

    private void reloadData() {
        page = 1;
        walletInfos.clear();
        loadData(true);
    }


    private void loadData(boolean showProgress) {
        if (showProgress) {
            showProgress("加载中...");
        }
        String url = ConfigXy.XY_BANK_INFO_LIST;
        RequestParams params = new RequestParams();
//        params.add("memberId", UtilPreference.getStringValue(mContext, "zf_member_id"));
//        params.add("groupId", UtilPreference.getStringValue(mContext, "zf_group_id"));
//        params.add("pageNo", page + "");
//        params.add("pageSize", "10");
//        params.add("token", UtilPreference.getStringValue(mContext, "token"));

        HttpUtil.get(url, params, new HttpUtil.RequestListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void success(String response) {
                disShowProgress();
                try {

                    JSONObject result = new JSONObject(response);

                    if (!result.optBoolean("status")) {
                        showErrorMsg(result.getString("message"));
                        // 下拉刷新完成
                        listview.onRefreshComplete();
                        return;
                    }

                    JSONArray obj = result.getJSONArray("data");
                    if (obj.length() <= 0){
                        adapterWallet.notifyDataSetChanged();
                        listview.onRefreshComplete();
                        return;
                    }

                    Gson gson = new Gson();
                    WalletInfo walletInfo = gson.fromJson(obj.toString(), new TypeToken<WalletInfo>() {
                    }.getType());

                    if (walletInfo.getWalletListInfo().size() > 0) {
                        linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                        walletInfos.addAll(walletInfos.size(), walletInfo.getWalletListInfo());

                    } else {
                        if (walletInfos.size() > 0){
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                        }else{
                            linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                        }
                    }


                    adapterWallet.notifyDataSetChanged();
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
//        reloadData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_yesterday_benefit:
                setUI(1);
				break;
			case R.id.tv_accumulate_benefit:
                setUI(2);
				break;
		default:
			break;
		}
	}


    /**
     * @Description 切换 基本信息 详细信息 评论信息
     * @param i
     */
    private void setUI(int i) {
        tv_yesterday_benefit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_yesterday_benefit.setBackgroundResource(R.drawable.btn_yellow_selector2);
        tv_accumulate_benefit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_accumulate_benefit.setBackgroundResource(R.drawable.btn_yellow_selector2);
        ll_yesterday_benefit.setVisibility(View.GONE);
        ll_accumulate_benefit.setVisibility(View.GONE);

        switch (i) {
            case 1:// 切换到昨日分润
                tv_yesterday_benefit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_yesterday_benefit.setBackgroundResource(R.drawable.btn_yellow_selector);
                ll_yesterday_benefit.setVisibility(View.VISIBLE);
                break;
            case 2:// 切换到累计分润
                tv_accumulate_benefit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_accumulate_benefit.setBackgroundResource(R.drawable.btn_yellow_selector);
                ll_accumulate_benefit.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }





}
