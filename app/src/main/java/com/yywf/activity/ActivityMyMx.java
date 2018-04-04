package com.yywf.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterWallet;
import com.yywf.adapter.AdapterZhangDan;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.WalletInfo;
import com.yywf.model.WalletListInfo;
import com.yywf.model.ZhangDan;
import com.yywf.util.MyActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyMx extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMyJlj";

    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<ZhangDan> zhangDanList = new ArrayList<ZhangDan>();// 信息通知
    private AdapterZhangDan adapter;
    private MyListView myListView;
//    private int page = 1;


    private RelativeLayout ll_mx;
    private TextView tv_sale;
    private TextView tv_hk;
    private int type = 1;

    private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_my_mx);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("交易明细");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();
        //加载储蓄卡的信息
//        loadDebitCardInfo();
        setUI(type);
	}


	private void initView() {

        tv_sale = textView(R.id.tv_sale);
        tv_sale.setOnClickListener(this);
        tv_hk = textView(R.id.tv_hk);
        tv_hk.setOnClickListener(this);

        ll_mx = relativeLayout(R.id.ll_mx);




        //测试数据
//        for (int i =0; i < 2; i++){
//            WalletListInfo info = new WalletListInfo();
//            info.setAmt(222222);
//            info.setTime("2018-1-1 12:12:22");
//            info.setName("吴从鹏");
//            walletInfos.add(info);
//        }


        myListView = findViewById(R.id.listview);
        adapter = new AdapterZhangDan(mContext, zhangDanList);
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


                if (zhangDanList.size() == 0) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showErrorMsg("没有更多了");
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

//    private void reloadData() {
//        page = 1;
//        walletInfos.clear();
////        loadData(true);
//    }
//
//
//    private void loadData(boolean showProgress) {
//        if (showProgress) {
//            showProgress("加载中...");
//        }
//
//        String url = ConfigXy.XY_BANK_INFO_LIST;
//        RequestParams params = new RequestParams();
////        params.add("memberId", UtilPreference.getStringValue(mContext, "zf_member_id"));
////        params.add("groupId", UtilPreference.getStringValue(mContext, "zf_group_id"));
////        params.add("pageNo", page + "");
////        params.add("pageSize", "10");
////        params.add("token", UtilPreference.getStringValue(mContext, "token"));
//
//        HttpUtil.get(url, params, new HttpUtil.RequestListener() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void success(String response) {
//                disShowProgress();
//                try {
//
//                    JSONObject result = new JSONObject(response);
//                    if (result.optInt("code") == -2){
//                        UtilPreference.clearNotKeyValues(mContext);
//                        // 退出账号 返回到登录页面
//                        MyActivityManager.getInstance().logout(mContext);
//                        return;
//                    }
//                    if (!result.optBoolean("status")) {
//                        showErrorMsg(result.getString("message"));
//                        // 下拉刷新完成
//                        listview.onRefreshComplete();
//                        return;
//                    }
//
//                    JSONArray obj = result.getJSONArray("data");
//                    if (obj.length() <= 0){
//                        adapterWallet.notifyDataSetChanged();
//                        listview.onRefreshComplete();
//                        return;
//                    }
//
//                    Gson gson = new Gson();
//                    WalletInfo walletInfo = gson.fromJson(obj.toString(), new TypeToken<WalletInfo>() {
//                    }.getType());
//
//                    if (walletInfo.getWalletListInfo().size() > 0) {
//                        linearLayout(R.id.id_no_data).setVisibility(View.GONE);
//                        walletInfos.addAll(walletInfos.size(), walletInfo.getWalletListInfo());
//
//                    } else {
//                        if (walletInfos.size() > 0){
//                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
//                        }else{
//                            linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
//                        }
//                    }
//
//
//                    adapterWallet.notifyDataSetChanged();
//                    // 下拉刷新完成
//                    listview.onRefreshComplete();
//
//                } catch (Exception e) {
//                    e.getMessage();
//                    listview.onRefreshComplete();
//                }
//            }
//
//            @Override
//            public void failed(Throwable error) {
//                disShowProgress();
//                showE404();
//                listview.onRefreshComplete();
//            }
//        });
//    }

    /**
     * 重新加载数据
     */
    private void reloadData() {
        Log.d(TAG, "reloadData()");
        page = 1;
        zhangDanList.clear();
        loadData(true);
    }


    private void loadData(boolean showProgress) {

        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        params.put("type", type);
        params.put("pageNo", page+"");
        params.put("pageSize", "15");


        if (showProgress) {
            showProgress("加载中...");
        }

        HttpUtil.get(ConfigXy.QUERY_BILL, params, new HttpUtil.RequestListener() {

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
                        // 刷新完成
                        mPullRefreshScrollView.onRefreshComplete();
                        return;
                    }


                    JSONObject obj = result.getJSONObject("data");
                    String bank_list = obj.optString("comsume_list");
                    if (!StringUtils.isBlank(bank_list)) {

                        Gson gson = new Gson();
                        // json数据转换成List
                        List<ZhangDan> datas = gson.fromJson(obj.getString("comsume_list"), new TypeToken<List<ZhangDan>>() {
                        }.getType());
                        if (datas.size() > 0) {
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            zhangDanList.addAll(zhangDanList.size(), datas);

                        } else {
                            if (zhangDanList.size() > 0){
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            }else{
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        if (zhangDanList.size() > 0){
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    showErrorMsg("没有更多了");
                                }
                            }, 1000);
                        }else{
                            linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    // 下拉刷新完成
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
//        reloadData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_sale:
                setUI(1);
				break;
			case R.id.tv_hk:
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
        tv_sale.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_sale.setBackgroundResource(R.drawable.btn_yellow_selector2);
        tv_hk.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_hk.setBackgroundResource(R.drawable.btn_yellow_selector2);

        switch (i) {
            case 1:// 切换到昨日分润
                tv_sale.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_sale.setBackgroundResource(R.drawable.btn_yellow_selector);
                break;
            case 2:// 切换到累计分润
                tv_hk.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_hk.setBackgroundResource(R.drawable.btn_yellow_selector);
                break;
            default:
                break;
        }

        type = i;

        reloadData();

    }





}
