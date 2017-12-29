package com.yywf.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.AlertUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
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
import com.yywf.activity.ActivityMyQr;
import com.yywf.activity.ActivityMyTeam;
import com.yywf.activity.ActivityMyWallet;
import com.yywf.activity.ActivitySaoMaShouKuan;
import com.yywf.activity.ActivitySetting;
import com.yywf.activity.ActivitySmrz;
import com.yywf.activity.ActivityVoucherTab;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.ConfigXy;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.Menu;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class FragmentMine extends AbstractFragment implements
        OnClickListener {
    private static final String TAG = "FragmentMine";
    private RoundImageView roundImageView;
    private TextView tvName;
    private TextView tvGradeName;
    private TextView tvPhone;
    private TextView tvRz;
//    private TextView tvBalanceAmt;
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
        tvGradeName = fragment.findViewById(R.id.grade_name);
        tvPhone = fragment.findViewById(R.id.tv_phone);
        tvRz = fragment.findViewById(R.id.tv_rz);
//        tvBalanceAmt = fragment.findViewById(R.id.tv_balance_amt);
        tvLxwmTel = fragment.findViewById(R.id.tv_lxwm_tel);
        tvLxwmTel.setOnClickListener(this);

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
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityMyWallet.class));
                        break;
                    case 2: //我的团队
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityMyTeam.class));
                        break;
                    case 3: //推广二维码
                        if (DialogUtils.checkApproveStatus(mContext)){
                            return;
                        }
                        startActivity(new Intent(mContext, ActivityMyQr.class));
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
                startActivity(new Intent(mContext, ActivityMine.class).putExtra("userName", tvName.getText().toString().trim()));
                break;
            case R.id.rl_smrz: //实名认证
                startActivity(new Intent(mContext, ActivitySmrz.class));
                break;
            case R.id.rl_bank_manager: //银行卡管理
                if (DialogUtils.checkApproveStatus(mContext)){
                    return;
                }
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
            case R.id.tv_lxwm_tel:
                final String phone = tvLxwmTel.getText().toString().trim();
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


    @Override
    public void onResume() {
        super.onResume();
        getMemberInfo();
    }

    private void getMemberInfo() {

        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.XY_GET_MEMBER_INFO, params, requestListener);
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
                    showErrorMsg(result.getString("message"));
                    return;
                }

                JSONObject dataStr = result.getJSONObject("data");
                String name = dataStr.optString("name");
                String grade_name = dataStr.optString("grade_name");
                String grade_count = dataStr.optString("grade_count");
                String icon = dataStr.optString("icon");
                String service_phone = dataStr.optString("service_phone");
                String phone = dataStr.optString("phone");
                int status = dataStr.optInt("status");


                if (!StringUtils.isBlank(icon)){
                    ImageLoader.getInstance().displayImage(icon, roundImageView);
                }

                if (!StringUtils.isBlank(name)){
                    tvName.setText(name);
                }

                if (!StringUtils.isBlank(grade_name)){
                    tvGradeName.setText(grade_name + " - "+ grade_count+"次");
                }

                if (!StringUtils.isBlank(service_phone)){
                    tvLxwmTel.setText(service_phone);
                }

                if (!StringUtils.isBlank(phone)){
                    tvPhone.setText(phone);
                }

                if (status == 1){
                    tvRz.setVisibility(View.VISIBLE);
                }





            } catch (Exception e) {
                Log.e(TAG, "doCommit() Exception: " + e.getMessage());
            }
        }

        @Override
        public void failed(Throwable error) {
            disShowProgress();
            showE404();
        }
    };


}
