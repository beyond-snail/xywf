package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.ALog;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.adapter.BankListAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.model.BankUserInfo;
import com.yywf.model.UserInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityBankCardManager extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityBankCardManager";

    private PullToRefreshListView listview;
    private BankListAdapter bankAdapter;
    private List<BankCardInfo> bankList = new ArrayList<BankCardInfo>();

    private List<BankCardInfo> vo;

    private RelativeLayout ll_credit;
    private LinearLayout ll_debit;
    private TextView tvCredit;
    private TextView tvDebit;

    private RoundImageView imageView;
    private TextView bankName;
    private TextView userName;
    private TextView bankCarkNo;
    private Button btnChange;

    private int page = 1;

    private MyCustomDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_bankcard_manager);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("银行卡管理");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
        if (findViewById(R.id.img_right_add) != null) {
            findViewById(R.id.img_right_add).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right_add).setOnClickListener(this);
        }
		initView();
        //加载储蓄卡的信息
        loadDebitCardInfo();
	}


	private void initView() {

		tvCredit = textView(R.id.tv_credit_card);
        tvCredit.setOnClickListener(this);
		tvDebit = textView(R.id.tv_debit_card);
        tvDebit.setOnClickListener(this);
        ll_credit = relativeLayout(R.id.ll_credit);
        ll_debit = linearLayout(R.id.ll_debit);

        //储蓄卡信息
        imageView = findViewById(R.id.icon_bank);
        bankName = textView(R.id.id_bank_name);
        userName = textView(R.id.id_username);
        bankCarkNo = textView(R.id.id_bank_card);
        button(R.id.btn_change_card).setOnClickListener(this);


        listview = (PullToRefreshListView)findViewById(R.id.pull_refresh_listView);
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

                handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showErrorMsg("没有更多了");
                            listview.onRefreshComplete();
                        }
                    }, 1000);

            }
        });
        listview.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int local = i;
                Log.e(TAG, ""+local);

                dialog = DialogUtils.showDialog2(mContext, "温馨提示", "取消", "确定", "是否删除该卡", null, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        delCard(bankList.get(local-1));
                    }
                });

                return false;
            }
        });

	}

    private void delCard(BankCardInfo bankCardInfo) {


        showProgress("加载中...");

        String url = ConfigXy.XY_BANK_INFO_REMOVE;
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("bankId", bankCardInfo.getId()+"");
        params.put("bankbillId", bankCardInfo.getBankbillId());

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
                        return;
                    }


                    showErrorMsg(result.getString("message"));
                    reloadData();
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showE404();
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
        if (ll_credit.getVisibility() == View.VISIBLE) {
            reloadData();
        }

        if (ll_debit.getVisibility() == View.VISIBLE){
            //加载储蓄卡的信息
            loadDebitCardInfo();
        }

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_credit_card:
                setUI(1);
				break;
			case R.id.tv_debit_card:
                setUI(2);
				break;
            case R.id.btn_change_card:
                startActivity(new Intent(mContext, ActivityChangeDebitInfo.class));
                break;
            case R.id.img_right_add:
                startActivity(new Intent(mContext, ActivityCreditAdd.class));
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
        tvCredit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tvCredit.setBackgroundResource(R.drawable.btn_yellow_selector2);
        tvDebit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector));
        tvDebit.setBackgroundResource(R.drawable.btn_yellow_selector2);
        ll_credit.setVisibility(View.GONE);
        ll_debit.setVisibility(View.GONE);

        switch (i) {
            case 1:// 切换到信用卡
                tvCredit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tvCredit.setBackgroundResource(R.drawable.btn_yellow_selector);
                ll_credit.setVisibility(View.VISIBLE);
                findViewById(R.id.img_right_add).setVisibility(View.VISIBLE);
                break;
            case 2:// 切换到储蓄卡
                tvDebit.setTextColor(mContext.getResources().getColorStateList(R.color.font_red_selector2));
                tvDebit.setBackgroundResource(R.drawable.btn_yellow_selector);
                ll_debit.setVisibility(View.VISIBLE);
                findViewById(R.id.img_right_add).setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }



    /**
     * 加载储蓄卡信息
     */
    private void loadDebitCardInfo() {
        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("type", "1");
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        String url = ConfigXy.GET_DEBIT_INFO;
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
                        showErrorMsg(result.getString("message"));
                        return;
                    }
                    JSONObject obj = result.getJSONObject("data");
                    String bank_list = obj.optString("bank_list");

                    if (!StringUtils.isBlank(bank_list)) {
                        Gson gson = new Gson();
                         vo = gson.fromJson(bank_list, new TypeToken<List<BankCardInfo> >() {
                        }.getType());



                        if (vo != null){
                            if (!StringUtils.isBlank(vo.get(0).getMember_name())){
                                userName.setText(vo.get(0).getMember_name());
                            }
                            if (!StringUtils.isBlank(vo.get(0).getBank_name())){
                                bankName.setText(vo.get(0).getBank_name());

                                if (EnumConsts.BankUi.getTypeByName(vo.get(0).getBank_name()) != null){
                                    imageView.setImageResource(EnumConsts.BankUi.getTypeByName(vo.get(0).getBank_name()).getIcon_id());
                                }
                            }
                            if (!StringUtils.isBlank(vo.get(0).getCard_num())){
                                bankCarkNo.setText(vo.get(0).getCard_num());
                            }


                        }
                    }


                } catch (Exception e) {
                    Log.e(TAG, "doLogin() Exception: " + e.getMessage());
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
