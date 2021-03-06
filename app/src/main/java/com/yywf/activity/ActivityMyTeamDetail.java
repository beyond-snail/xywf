package com.yywf.activity;

import android.content.Intent;
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
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterTeamDetal;
import com.yywf.adapter.AdapterVoucher;
import com.yywf.adapter.AdapterWallet;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.MyTeamInfo;
import com.yywf.model.TeamInfo;
import com.yywf.model.VoucherInfo;
import com.yywf.model.WalletInfo;
import com.yywf.model.WalletListInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyTeamDetail extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMyTeamDetail";


    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<MyTeamInfo> teamInfos = new ArrayList<MyTeamInfo>();
    private AdapterTeamDetal adapter;
    private MyListView myListView;
    private int page = 1;

    private TextView tv_one;
    private TextView tv_second;
    private TextView btn_commit;

    private int type = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_myteam_detail);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("我的团队");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();
        setUI(type);
	}


	private void initView() {



//        //测试数据
//        for (int i =0; i < 2; i++){
//            MyTeamInfo info = new MyTeamInfo();
//            info.setPhone("14782108169");
//            info.setMemberName("吴从鹏");
//            info.setCount(122);
//            info.setType(2);
//            teamInfos.add(info);
//        }


        tv_one = textView(R.id.tv_one);
        tv_one.setOnClickListener(this);
        tv_second = textView(R.id.tv_second);
        tv_second.setOnClickListener(this);

        btn_commit = textView(R.id.btn_commit);


        myListView = findViewById(R.id.listview);
        adapter = new AdapterTeamDetal(mContext, teamInfos);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ToastUtils.CustomShow(mContext, ""+i);
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

//                if (teamInfos.size() == 0) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showErrorMsg("没有更多了");
                            // ListScrollUtil.setGridViewHeightBasedOnChildren(gridView);
                            mPullRefreshScrollView.onRefreshComplete();
                        }
                    }, 1000);

//                } else {
//                    page++;
//                    loadData(false);
//                }
            }
        });
    }




    /**
     * 重新加载数据
     */
    private void reloadData() {
        Log.d(TAG, "reloadData()");
        page = 1;
        teamInfos.clear();
        loadData(true);
    }


    private void loadData(boolean showProgress) {



        // String url = ConfigApp.HC_GET_STORE_GOODS;
        String url = ConfigXy.XY_MY_TEAM_LIST;
        RequestParams params = new RequestParams();

        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        params.put("type", type);

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
			showProgress("加载中...");
//		}

        HttpUtil.get(url, params, new HttpUtil.RequestListener() {

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
                        mPullRefreshScrollView.onRefreshComplete();
                        return;
                    }
                    if (result.getString("data") != null) {
                        String str = result.getString("data");

                        Gson gson = new Gson();
                        // json数据转换成List
                        List<MyTeamInfo> datas = gson.fromJson(str, new TypeToken<List<MyTeamInfo>>() {
                        }.getType());
                        teamInfos.addAll(datas);
                        if (teamInfos.size() > 0) {
                            linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                            myListView.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                            myListView.setVisibility(View.GONE);
                        }
                    }
                    if (result.getString("data_extend") != null){
                        String totalCount = result.getString("data_extend");
                        btn_commit.setText("累计成功推荐"+totalCount+"人");
                    }
                    // 刷新完成
                    mPullRefreshScrollView.onRefreshComplete();
                    adapter.notifyDataSetChanged();
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
        reloadData();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one:
                setUI(1);
                break;
            case R.id.tv_second:
                setUI(2);
                break;
            default:
                break;
        }
        reloadData();

	}


    /**
     * @Description 切换 基本信息 详细信息 评论信息
     * @param i
     */
    private void setUI(int i) {
        tv_one.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_one.setBackgroundResource(R.drawable.btn_yellow_selector2);
        tv_second.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tv_second.setBackgroundResource(R.drawable.btn_yellow_selector2);

        type = i;
        switch (i) {
            case 1:// 切换到信用卡
                tv_one.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_one.setBackgroundResource(R.drawable.btn_yellow_selector);
                break;
            case 2:// 切换到储蓄卡
                tv_second.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tv_second.setBackgroundResource(R.drawable.btn_yellow_selector);
                break;
            default:
                break;
        }

//        reloadData();

    }






}
