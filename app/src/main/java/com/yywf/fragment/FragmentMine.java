package com.yywf.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.tool.utils.view.MyListView;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.activity.ActivityBankCardManager;
import com.yywf.activity.ActivityFollowUs;
import com.yywf.activity.ActivityHome;
import com.yywf.activity.ActivityMine;
import com.yywf.activity.ActivityMyWallet;
import com.yywf.activity.ActivitySetting;
import com.yywf.activity.ActivitySmrz;

import java.util.Timer;


public class FragmentMine extends AbstractFragment implements
        OnClickListener {

    private RoundImageView roundImageView;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvRz;
    private TextView tvBalanceAmt;
    private TextView tvLxwmTel;
    private RelativeLayout rl_smrz;
    private RelativeLayout rl_bank_manager;
    private RelativeLayout rl_mywallet;
    private RelativeLayout rl_gzwm;
    private RelativeLayout rl_setting;
    private LinearLayout ll_user_info;




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

        rl_mywallet = fragment.findViewById(R.id.rl_mywallet);
        rl_mywallet.setOnClickListener(this);

        rl_gzwm = fragment.findViewById(R.id.rl_gzwm);
        rl_gzwm.setOnClickListener(this);

        rl_setting = fragment.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(this);

        ll_user_info = fragment.findViewById(R.id.ll_user_info);
        ll_user_info.setOnClickListener(this);


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
            case R.id.rl_mywallet: //我的钱包
                startActivity(new Intent(mContext, ActivityMyWallet.class));
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
