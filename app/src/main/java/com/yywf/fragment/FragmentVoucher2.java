package com.yywf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.activity.ActivityBankCardManager;
import com.yywf.activity.ActivityFollowUs;
import com.yywf.activity.ActivityMine;
import com.yywf.activity.ActivityMyWallet;
import com.yywf.activity.ActivitySaoMaShouKuan;
import com.yywf.activity.ActivitySetting;
import com.yywf.activity.ActivitySmrz;
import com.yywf.activity.ActivityVoucherTab;
import com.yywf.adapter.AdapterVoucher;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.Menu;
import com.yywf.model.VoucherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentVoucher2 extends AbstractFragment{
    private static final String TAG = "FragmentVoucher2";
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;


    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<VoucherInfo> voucherList = new ArrayList<VoucherInfo>();// 信息通知
    private AdapterVoucher adapter;
    private MyListView myListView;
    private int page = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newsLayout = inflater.inflate(R.layout.activity_voucher1,
                container, false);
        return newsLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Log.d("TAG", "fragment->setUserVisibleHint");
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        Log.d("TAG", getClass().getName() + "->initData()");
        initData();
        isFirst = false;
    }

    private void initData() {

        for (int i = 0; i < 3; i++){
            VoucherInfo info = new VoucherInfo();
            info.setAmt(1000);
            info.setUseStatus(2);
            info.setUseTime("2017-12-15 18:03:12");
            info.setUseText("VIP购买");
            voucherList.add(info);
        }


        myListView = fragment.findViewById(R.id.listview);
        adapter = new AdapterVoucher(mContext, voucherList);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        mPullRefreshScrollView =  fragment.findViewById(R.id.pull_refresh_scrollview);
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

                if (voucherList.size() == 0) {
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
        voucherList.clear();
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
                        return;
                    }
                    if (result.getString("data") != null) {
                        String str = result.getString("data");

                        Gson gson = new Gson();
                        // json数据转换成List
                        List<VoucherInfo> datas = gson.fromJson(str, new TypeToken<List<VoucherInfo>>() {
                        }.getType());
                        voucherList.addAll(datas);
                        adapter.notifyDataSetChanged();
                        if (voucherList.size() > 0) {
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




    //do something
    protected void onInvisible() {


    }




}
