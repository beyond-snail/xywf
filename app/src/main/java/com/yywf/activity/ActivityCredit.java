package com.yywf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.passwordView.KeyBoardDialog;
import com.tool.utils.passwordView.PayPasswordView;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.utils.ValidateUtil;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyCountDownTimer;
import com.tool.utils.view.RoundImageView;
import com.tool.utils.view.Split4EditTextWatcher;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ActivityCredit extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityCredit";

    private RoundImageView imageView;
    private TextView bankName;
    private TextView userName;
    private TextView bankCarkNo;
    private TextView tv_choose_card;

    private RelativeLayout rl_choose;

    private MoneyEditText et_amt;

    private KeyBoardDialog keyboard;

    private BankCardInfo vo;

    private String orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_credit);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("自助还款");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 4));
                }
            });
        }
        initView();
        loadData();

        DialogUtils.showDialogTb(mContext,true);
    }




    private void initView() {

        rl_choose = relativeLayout(R.id.rl_choose);
        rl_choose.setOnClickListener(this);

        tv_choose_card = textView(R.id.tv_choose_card);

        imageView = findViewById(R.id.icon_bank);
        bankName = textView(R.id.id_bank_name);
        userName = textView(R.id.id_username);
        bankCarkNo = textView(R.id.id_bank_card);
        linearLayout(R.id.ll_change).setOnClickListener(this);

        et_amt = (MoneyEditText) editText(R.id.et_amt);
        et_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Date nowTime = StringUtils.getDateFromString(StringUtils.getFormatCurTime(), "yyyyMMddHHmmss");
                Date startTime  = StringUtils.getDate(StringUtils.getCurDate()+"000000", "yyyyMMddHHmmss");
                Date endTime  = StringUtils.getDate(StringUtils.getCurDate()+"210000", "yyyyMMddHHmmss");
                if (!StringUtils.isEffectiveDate(nowTime, startTime, endTime)){
                    ToastUtils.CustomShow(mContext, "该时间段内不能交易");
                    return;
                }


//                if(delayRun!=null){
//                    //每次editText有变化的时候，则移除上次发出的延迟线程
//                    handler.removeCallbacks(delayRun);
//                }
//
//
//                //延迟800ms，如果不再输入字符，则执行该线程的run方法
//                handler.postDelayed(delayRun, 1000);
            }
        });
        button(R.id.btn_commit).setOnClickListener(this);



    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change:
                //更换银行卡
                startActivityForResult(new Intent(mContext, ActivityBankCardList.class), 1);
                break;
            case R.id.btn_commit://提交
                doCommit();
                break;
            default:
                break;
        }
    }



    /**
     * 提交
     */
    private void doCommit() {


        if (StringUtils.isBlank(et_amt.getText().toString().trim())){
            ToastUtils.CustomShow(mContext, "请输入金额");
            return;
        }

        if (StringUtils.changeY2F(et_amt.getMoneyText()).length() > 7){
            ToastUtils.CustomShow(mContext, "单笔金额不超过2万");
            return;
        }



        keyboard = new KeyBoardDialog((Activity) mContext, getDecorViewDialog());
        keyboard.show();
    }


    protected View getDecorViewDialog() {

        return PayPasswordView.getInstance("", mContext, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(final String password) {// 这里调用验证密码是否正确的请求

                // TODO Auto-generated method stub
				keyboard.dismiss();
				keyboard = null;

                showProgress("加载中...");
                RequestParams params = new RequestParams();
//		params.add("account", username);
//		params.add("password", password);
                HttpUtil.get(ConfigXy.XY_SMRZ, params, requestListener);

            }

            @Override
            public void onForgotPwd() {
                startActivity(new Intent(mContext, ActivityForgotPayPwd.class));
            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub
                keyboard.dismiss();
                keyboard = null;
                ToastUtils.showShort(getApplicationContext(), "交易已取消");
            }
        }).getView();
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
                            //默认选取第一个
                            vo = bankCardInfoList.get(0);
                            if (vo != null){
                                if (!StringUtils.isBlank(vo.getMember_name())){
                                    userName.setText(vo.getMember_name());
                                }
                                if (!StringUtils.isBlank(vo.getBank_name())){
                                    bankName.setText(vo.getBank_name());

                                    if (EnumConsts.BankUi.getTypeByName(vo.getBank_name()) != null){
                                        imageView.setImageResource(EnumConsts.BankUi.getTypeByName(vo.getBank_name()).getIcon_id());
                                    }
                                }
                                if (!StringUtils.isBlank(vo.getCard_num())){
                                    if (vo.getCard_num().length() > 4) {
                                        bankCarkNo.setText(vo.getCard_num());
                                    }else{
                                        bankCarkNo.setText("************"+vo.getCard_num());
                                    }
                                }
                            }

                        }else{
                            //没有银行卡去添加
                            startActivityForResult(new Intent(mContext, ActivityBankCardList.class), 1);
                        }
                    }


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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            vo = (BankCardInfo) data.getSerializableExtra("bankInfo");
            if (vo != null){
                if (!StringUtils.isBlank(vo.getMember_name())){
                    userName.setText(vo.getMember_name());
                }
                if (!StringUtils.isBlank(vo.getBank_name())){
                    bankName.setText(vo.getBank_name());

                    if (EnumConsts.BankUi.getTypeByName(vo.getBank_name()) != null){
                        imageView.setImageResource(EnumConsts.BankUi.getTypeByName(vo.getBank_name()).getIcon_id());
                    }
                }
                if (!StringUtils.isBlank(vo.getCard_num())){
                    if (vo.getCard_num().length() > 4) {
                        bankCarkNo.setText(vo.getCard_num());
                    }else{
                        bankCarkNo.setText("************"+vo.getCard_num());
                    }
                }
            }
        }
    }


}
