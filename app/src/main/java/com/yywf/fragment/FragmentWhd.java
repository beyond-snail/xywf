package com.yywf.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.activity.ActivityChangeDebitInfo;
import com.yywf.activity.ActivityCreditAdd;
import com.yywf.adapter.BankListAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class FragmentWhd extends AbstractFragment implements
        OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView listview;
    private BankListAdapter bankAdapter;
    private List<BankCardInfo> bankList = new ArrayList<BankCardInfo>();

    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newsLayout = inflater.inflate(R.layout.fragment_whd, container, false);
        return newsLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        Button btn = fragment.findViewById(R.id.img_right_add);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ActivityCreditAdd.class));
            }
        });


        //测试数据
        for (int i =0; i < 2; i++){
            BankCardInfo info = new BankCardInfo();
            info.setAmt(222222);
            info.setBank_name("交通银行");
            info.setHkDay(6);
            info.setZdDay(20);
            info.setCard_num("3333");
            info.setMember_name("吴从鹏");
            bankList.add(info);
        }



        listview = (PullToRefreshListView)fragment.findViewById(R.id.pull_refresh_listView);
        bankAdapter = new BankListAdapter((Activity) mContext, bankList, 1);
        listview.setAdapter(bankAdapter);

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

                if (bankList.size() == 0) {
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
        bankList.clear();
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
                        bankAdapter.notifyDataSetChanged();
                        listview.onRefreshComplete();
                        return;
                    }

                    Gson gson = new Gson();
                    List<BankCardInfo> bankCardInfoList = gson.fromJson(obj.toString(), new TypeToken<List<BankCardInfo> >() {
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
    public void onResume() {
        super.onResume();
//        reloadData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
