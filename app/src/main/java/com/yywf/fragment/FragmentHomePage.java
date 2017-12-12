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
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.activity.ActivityMine;
import com.yywf.activity.ActivitySaoMaShouKuan;
import com.yywf.adapter.BankListAdapter;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.EnumConsts;
import com.yywf.model.AdInfo;
import com.yywf.model.BankCardInfo;
import com.yywf.model.Menu;
import com.yywf.widget.ADCommonView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class FragmentHomePage extends AbstractFragment implements
        OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "FragmentHomePage";
    private LinearLayout ll_advertis;

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

                        break;
                    case 2: //智能还款

                        break;
                    case 3: //还信用卡

                        break;
                    case 4: //一键提额

                        break;
                    case 5: //待办事项

                        break;
                    case 6: //一键办卡

                        break;
                    case 7: //我的账单

                        break;
                    case 8: //扫码收钱
                        startActivity(new Intent(mContext, ActivitySaoMaShouKuan.class));
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

        List<AdInfo> infos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AdInfo info = new AdInfo();
            info.setPhotoUrl("https://m.tourongjia.com/escrowwap/channelTemplateRed ");
            infos.add(info);
        }
        ADCommonView adCommonView = new ADCommonView(mContext, infos, false);
        ll_advertis.addView(adCommonView);
    }



    private void initBankList() {

        //测试数据
        for (int i =0; i < 1; i++){
            BankCardInfo info = new BankCardInfo();
            info.setAmt(222222);
            info.setBankName("交通银行");
            info.setHkDay(6);
            info.setZdDay(20);
            info.setwH("3333");
            info.setName("吴从鹏");
            bankList.add(info);
        }



        lv = (MyListView)fragment.findViewById(R.id.show_bankPay);
        bankAdapter = new BankListAdapter((Activity)mContext, bankList);
        lv.setAdapter(bankAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
