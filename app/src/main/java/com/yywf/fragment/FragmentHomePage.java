package com.yywf.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.DialogUtil;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.activity.ActivityAddZhangDan;
import com.yywf.activity.ActivityBanKTiE;
import com.yywf.activity.ActivityBanKa;
import com.yywf.activity.ActivityCredit;
import com.yywf.activity.ActivityDaiBanShiXiang;
import com.yywf.activity.ActivityKjsk;
import com.yywf.activity.ActivityMine;
import com.yywf.activity.ActivityMyZhangDan;
import com.yywf.activity.ActivitySaoMaShouKuan;
import com.yywf.activity.ActivitySmartCredit;
import com.yywf.activity.ActivityVipGrade;
import com.yywf.adapter.BankListAdapter;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.AdInfo;
import com.yywf.model.BankCardInfo;
import com.yywf.model.Menu;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.ADCommonView;
import com.yywf.widget.dialog.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class FragmentHomePage extends AbstractFragment implements
        OnClickListener{
    private static final String TAG = "FragmentHomePage";
    private LinearLayout ll_advertis;

    private List<AdInfo> infos = new ArrayList<>();

    private List<Menu> list = new ArrayList<Menu>();
    private MyGridView gridView;
    private MyMenuAdapter adapter;

    private List<BankCardInfo> bankList = new ArrayList<BankCardInfo>();
    private MyListView lv;
    private BankListAdapter bankAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newsLayout = inflater.inflate(R.layout.fragment_homepage,
                container, false);
        return newsLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void initView() {

        //初始化广告栏
        initAdvertis();
        //初始化类目
        initMenu();

        //银行卡列表
        initBankList();

    }

    private void initMenu() {
        for (int i = 0; i < EnumConsts.MenuType.values().length; i++){
            Menu menu = new Menu();
            menu.setBg(EnumConsts.MenuType.values()[i].getBg());
            menu.setName(EnumConsts.MenuType.values()[i].getName());
            list.add(menu);
        }

        gridView = (MyGridView) fragment.findViewById(R.id.id_gridview);
        adapter = new MyMenuAdapter(mContext, list);
        gridView.setAdapter(adapter);

        //类目
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemClick " + position);
                // 下拉刷新占据一个位置
                int index = EnumConsts.MenuType.getCodeByName(list.get(position).getName());
                switch (index){
                    case 1: //快捷收钱
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
//                        if (!DialogUtils.checkGradeStatus(mContext)){
//                            return;
//                        }
                        startActivity(new Intent(mContext, ActivityKjsk.class));
                        break;
                    case 2: //智能还款
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        if (!DialogUtils.checkGradeStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivitySmartCredit.class));
                        break;
                    case 3: //还信用卡
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityCredit.class));
                        break;
                    case 4: //一键提额
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityBanKTiE.class));
                        break;
                    case 5: //待办事项
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        if (!DialogUtils.checkGradeStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityDaiBanShiXiang.class));
                        break;
                    case 6: //一键办卡
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityBanKa.class));
                        break;
                    case 7: //我的账单
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        if (!DialogUtils.checkGradeStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityMyZhangDan.class));
                        break;
                    case 8: //购买等级
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        if (!DialogUtils.checkGradeStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityVipGrade.class));
                        break;
                }
            }
        });
    }

    //初始化广告栏
    private void initAdvertis(){
        // 广告栏开始
        ll_advertis = (LinearLayout) fragment.findViewById(R.id.advertis);
        // 设置宽度高度一致
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_advertis.getLayoutParams();
        linearParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2);// 640-370
        // 750-434
        ll_advertis.setLayoutParams(linearParams);

        //添加账单
        textView(R.id.id_add_zd).setOnClickListener(this);


        //获取广告
//        getAds();
    }

    private void getAds() {
        RequestParams params = new RequestParams();
        params.add("type", "3");
        HttpUtil.get(ConfigXy.ZF_GET_ADS_API, params,requestListener);
    }


    private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

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
//					showErrorMsg(result.getString("message"));
                    return;
                }

                JSONObject obj = result.getJSONObject("data");
                if (obj == null) {
                    return;
                }

                Gson gson = new Gson();

                List<AdInfo> datas = gson.fromJson(obj.getString("advert_list"), new TypeToken<List<AdInfo>>() {
                }.getType());

                infos.clear();
                infos.addAll(datas);

                ADCommonView adCommonView = new ADCommonView(mContext, infos, false);
                ll_advertis.addView(adCommonView);

            } catch (Exception e) {
                Log.e(TAG, "doLogin() Exception: " + e.getMessage());

            }
        }

        @Override
        public void failed(Throwable error) {
            disShowProgress();
            showE404();
        }
    };


    private void initBankList() {

        lv = (MyListView)fragment.findViewById(R.id.show_bankPay);
        bankAdapter = new BankListAdapter((Activity)mContext, bankList, 1);
        lv.setAdapter(bankAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_add_zd:
                startActivity(new Intent(mContext, ActivityAddZhangDan.class));
                break;
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        getAds();
        loadData();
    }

    private void loadData() {

        showProgress("加载中...");

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
                        return;
                    }

                    JSONObject obj = result.getJSONObject("data");
                    String bank_list = obj.optString("bank_list");
                    if (!StringUtils.isBlank(bank_list)) {

                        Gson gson = new Gson();
                        List<BankCardInfo> bankCardInfoList = gson.fromJson(bank_list, new TypeToken<List<BankCardInfo> >() {
                        }.getType());
                        if (bankCardInfoList.size() > 0) {
                            bankList.clear();
                            bankList.addAll(bankList.size(), bankCardInfoList);
                        }
                    }



                    bankAdapter.notifyDataSetChanged();

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


}
