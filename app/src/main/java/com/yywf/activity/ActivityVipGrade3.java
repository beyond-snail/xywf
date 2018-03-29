package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterVipGrade;
import com.yywf.adapter.AdapterVipGrade3;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.VipGrade;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActivityVipGrade3 extends BaseActivity implements OnClickListener {
    private final String TAG = "ActivityVipGrade";

//    private PullToRefreshScrollView mPullRefreshScrollView;
    private MyListView myListView;

    private VipGrade vo;
    private List<VipGrade> list = new ArrayList<VipGrade>();

    private AdapterVipGrade3 adapter;





    private MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_purchase_grade3);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("购买等级");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 10));
                }
            });
        }
        initView();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {


        myListView = findViewById(R.id.listview);
        adapter = new AdapterVipGrade3(mContext, list);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ToastUtils.CustomShow(mContext, ""+i);
//                startActivity(new Intent(mContext, ActivityOrderDetail.class));
            }
        });

//        mPullRefreshScrollView = findViewById(R.id.pull_refresh_scrollview);
//        // 下拉刷新、上拉加载更多
//        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
//        // TODO:必须先设置Mode后再设置刷新文本
//        ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
//        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
//        startLabels.setRefreshingLabel("正在载入...");// 刷新时
//        startLabels.setReleaseLabel("释放立即刷新...");// 下来达到一定距离时，显示的提示
//        // TODO:必须先设置Mode后再设置刷新文本
//        ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
//        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("正在载入...");// 刷新时
//        endLabels.setReleaseLabel("释放立即加载...");// 下来达到一定距离时，显示的提示
//        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                // 下拉刷新
//                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于：" + label);
////                reloadData();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                // 上拉加载更多
//                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("更新于：" + label);
//
//
//
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showErrorMsg("没有更多了");
//                        mPullRefreshScrollView.onRefreshComplete();
//                    }
//                }, 1000);
//
//
//            }
//        });


        button(R.id.btn_commit).setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_grade_msg:
                startActivity( new Intent(mContext, ActivityReadWord.class));
                break;
            case R.id.btn_commit:
                final String phone = "0571-8858-6888";
                DialogUtils.alert("拨打 " + phone, null, mContext, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                break;
        }
    }


    private void ShowAccount(final VipGrade vo){

        dialog = DialogUtils.showDialog2(mContext, "温馨提示", "取消", "确定", "您将付款"+StringUtils.formatIntMoney(vo.getPurchasePrice())+"元", "信易（杭州）互联网科技有限公司\r\n账号: 19020101040041032\r\n开户行: 中国农业银行杭州市城西支行", new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        loadList();
    }

    private void loadList() {

        showProgress("正在发送");
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.GRADE_LIST, params, new HttpUtil.RequestListener() {

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
                        // 刷新完成
//                        mPullRefreshScrollView.onRefreshComplete();
                        return;
                    }

                    JSONObject obj = result.getJSONObject("data");
                    String grade_list = obj.optString("grade_list");
                    if (!StringUtils.isBlank(grade_list)) {

                        Gson gson = new Gson();
                        List<VipGrade> vipGradeList = gson.fromJson(grade_list, new TypeToken<List<VipGrade> >() {
                        }.getType());

                        if (vipGradeList.size() > 0) {
                            list.addAll(vipGradeList);
                            if (list.size() > 0) {
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }

                        } else {
                            if (list.size() > 0) {

                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
//                                handler.postDelayed(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        showErrorMsg("没有更多数据");
//                                    }
//                                }, 1000);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }
                        }

//                        if (vipGradeList.size() > 0) {
//                            //去掉前三个
//                            List<VipGrade> tempList = new ArrayList<>();
//                            for (int i = 3; i < vipGradeList.size(); i++){
//                                tempList.add(vipGradeList.get(i));
//                            }
//
//                            list.clear();
//                            list.addAll(list.size(), tempList);
//
//                            vo = list.get(list.size()-1);
//                            vo.setDefault(true);
//
//                        } else {
//                           ToastUtils.CustomShow(mContext, "返回套餐为空");
//                        }
                    }else{
                        ToastUtils.CustomShow(mContext, "返回数据错误");
                    }
                    // 刷新完成
//                    mPullRefreshScrollView.onRefreshComplete();
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
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
