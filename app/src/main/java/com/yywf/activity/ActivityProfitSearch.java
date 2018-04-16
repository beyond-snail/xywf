package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterTransRecord;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.TransRecordInfo;
import com.yywf.model.TransRecordList;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Carson_Ho on 17/8/11.
 */

public class ActivityProfitSearch extends BaseActivity {

    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_src)
    TextView tvSrc;
    @BindView(R.id.rl_src)
    RelativeLayout rlSrc;
    /**
     * 数据展示
     */
    private PullToRefreshScrollView mPullRefreshScrollView;
    private MyListView myListView;
    /**
     * 适配器
     */
    private AdapterTransRecord mAdapter;

    /**
     * 编辑框
     */
    private EditText mEditText;
    /**
     * 清除按钮
     */
    private ImageView mClearImg;
    /**
     * 搜索图标
     */
    private ImageView mSearchBarImg;


    private int page = 1;

    private String year;

    private String name;

    private List<TransRecordList> agentInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_record_search);
        ButterKnife.bind(this);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("收益明细");
        findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        setTvText(R.id.tv_right, "日期");
        textView(R.id.tv_right).setTextSize(13);
        showView(R.id.tv_right, true);

        initView();
    }


    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            loadData();
        }
    };


    /**
     * 初始化view
     */
    private void initView() {


        rlSrc.setVisibility(View.GONE);

        mEditText = (EditText) findViewById(com.tool.R.id.et_search);
        mClearImg = (ImageView) findViewById(com.tool.R.id.iv_search_clear);
        mSearchBarImg = (ImageView) findViewById(com.tool.R.id.iv_search_icon);

        // 清空按钮处理事件
        mClearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                mClearImg.setVisibility(View.GONE);
            }
        });
        // 搜索栏处理事件
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0 && !charSequence.toString().equals("")) {
                    mClearImg.setVisibility(View.VISIBLE);
                } else {
                    mClearImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
        });


        myListView = findViewById(R.id.listview);
        mAdapter = new AdapterTransRecord(mContext, agentInfos);
        myListView.setAdapter(mAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent();
//                mIntent.putExtra("name", agentInfos.get(i).getName());
//                mIntent.putExtra("twoProfit", agentInfos.get(i).getTwoProfit());
//                mIntent.putExtra("twoRebate", agentInfos.get(i).getTwoRebate());
                mIntent.putExtra("agentInfo", agentInfos.get(i));
                // 设置结果，并进行传送
                setResult(2, mIntent);
                finish();
            }
        });


        mPullRefreshScrollView = findViewById(R.id.pull_refresh_scrollview);
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
//                reloadData();
                reloadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 上拉加载更多
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel("更新于：" + label);

                if (agentInfos.size() == 0) {
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
                    loadData();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }


    /**
     * 重新加载数据
     */
    private void reloadData() {
        page = 1;
        agentInfos.clear();
        loadData();
    }


    private void loadData() {

//        agentInfos.clear();
        showProgress("加载中...");

        String url = ConfigXy.XY_INCOME_LIST;
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("keywords", mEditText.getText().toString().trim());
        params.add("years", year);
        params.put("pageNo", page);
        params.put("pageSize", "10");

        HttpUtil.get(url, params, new HttpUtil.RequestListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void success(String response) {
                disShowProgress();
                try {

                    JSONObject result = new JSONObject(response);
                    if (result.optInt("code") == -2) {
                        UtilPreference.clearNotKeyValues(mContext);
                        // 退出账号 返回到登录页面
                        MyActivityManager.getInstance().logout(mContext);
                        mPullRefreshScrollView.onRefreshComplete();
                        return;
                    }
                    if (!result.optBoolean("status")) {
                        showErrorMsg(result.getString("message"));
                        mPullRefreshScrollView.onRefreshComplete();
                        return;
                    }


                    Gson gson = new Gson();
                    TransRecordInfo infos = gson.fromJson(result.optString("data"), new TypeToken<TransRecordInfo>() {
                    }.getType());


                    if (infos != null) {

//                        tvSrc.setText("共"+infos.getTransactionPen()+"笔交易、"+ StringUtils.formatIntMoney(infos.getTransactionAmount())+"元交易金额");


                        if (infos.getComsume_list().size() > 0) {
                            agentInfos.addAll(infos.getComsume_list());
                            if (agentInfos.size() > 0) {
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }

                        } else {
                            if (agentInfos.size() > 0) {
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }
                        }
                    }


                    mAdapter.notifyDataSetChanged();
                    mPullRefreshScrollView.onRefreshComplete();


                } catch (Exception e) {
                    e.getMessage();
                    mPullRefreshScrollView.onRefreshComplete();
                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showE404();
                mPullRefreshScrollView.onRefreshComplete();
            }
        });
    }

    @OnClick(R.id.tv_right)
    public void onViewClicked() {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
//                tvDateChoose.setText(TimeUtil.getStringFromDate(date, "yyyy-MM-dd"));
                year = StringUtils.getStringFromDate(date, "yyyyMM");
                reloadData();
            }
        });
        dialog.show();
    }

}