package com.yywf.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.activity.ActivityBankCardManager;
import com.yywf.activity.ActivityCredit;
import com.yywf.activity.ActivityFollowUs;
import com.yywf.activity.ActivityHome;
import com.yywf.activity.ActivityKjsk;
import com.yywf.activity.ActivityMine;
import com.yywf.activity.ActivityMyWallet;
import com.yywf.activity.ActivitySaoMaShouKuan;
import com.yywf.activity.ActivitySetting;
import com.yywf.activity.ActivitySmrz;
import com.yywf.activity.ActivityVoucherTab;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.EnumConsts;
import com.yywf.model.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class FragmentMine extends AbstractFragment implements
        OnClickListener {
    private static final String TAG = "FragmentMine";
    private RoundImageView roundImageView;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvRz;
    private TextView tvBalanceAmt;
    private TextView tvLxwmTel;
    private RelativeLayout rl_smrz;
    private RelativeLayout rl_bank_manager;
    private RelativeLayout rl_smsq;
    private RelativeLayout rl_gzwm;
    private RelativeLayout rl_setting;
    private LinearLayout ll_user_info;


    private List<Menu> list = new ArrayList<Menu>();
    private MyGridView gridView;
    private MyMenuAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newsLayout = inflater.inflate(R.layout.fragment_mine_setting,
                container, false);
        return newsLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initMenu();
    }


    private void initView() {

        roundImageView = fragment.findViewById(R.id.iv_img_header);
        tvName = fragment.findViewById(R.id.tv_name);
        tvPhone = fragment.findViewById(R.id.tv_phone);
        tvRz = fragment.findViewById(R.id.tv_rz);
        tvBalanceAmt = fragment.findViewById(R.id.tv_balance_amt);
        tvLxwmTel = fragment.findViewById(R.id.tv_lxwm_tel);

        rl_smrz = fragment.findViewById(R.id.rl_smrz);
        rl_smrz.setOnClickListener(this);

        rl_bank_manager = fragment.findViewById(R.id.rl_bank_manager);
        rl_bank_manager.setOnClickListener(this);

        rl_smsq = fragment.findViewById(R.id.rl_smsq);
        rl_smsq.setOnClickListener(this);

        rl_gzwm = fragment.findViewById(R.id.rl_gzwm);
        rl_gzwm.setOnClickListener(this);

        rl_setting = fragment.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(this);

        ll_user_info = fragment.findViewById(R.id.ll_user_info);
        ll_user_info.setOnClickListener(this);


    }


    private void initMenu() {
        for (int i = 0; i < EnumConsts.MineMenuType.values().length; i++){
            Menu menu = new Menu();
            menu.setBg(EnumConsts.MineMenuType.values()[i].getBg());
            menu.setName(EnumConsts.MineMenuType.values()[i].getName());
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
                int index = EnumConsts.MineMenuType.getCodeByName(list.get(position).getName());
                switch (index){
                    case 1: //我的钱包
                        startActivity(new Intent(mContext, ActivityMyWallet.class));
                        break;
                    case 2: //我的团队

                        break;
                    case 3: //推广二维码
//                        startActivity(new Intent(mContext, ActivityCredit.class));
                        break;
                    case 4: //抵用券
                        startActivity(new Intent(mContext, ActivityVoucherTab.class));
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_user_info: //个人中心
                startActivity(new Intent(mContext, ActivityMine.class));
                break;
            case R.id.rl_smrz: //实名认证
                startActivity(new Intent(mContext, ActivitySmrz.class));
                break;
            case R.id.rl_bank_manager: //银行卡管理
                startActivity(new Intent(mContext, ActivityBankCardManager.class));
                break;
            case R.id.rl_smsq: //扫码收钱
                startActivity(new Intent(mContext, ActivitySaoMaShouKuan.class));
                break;
            case R.id.rl_gzwm: //关注我们
                startActivity(new Intent(mContext, ActivityFollowUs.class));
                break;
            case R.id.rl_setting: //设置
                startActivity(new Intent(mContext, ActivitySetting.class));
                break;


        }


    }



}
