package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyCountDownTimer;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ActivityKjsk extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityKjsk";

    private RoundImageView imageView;
    private TextView bankName;
    private TextView userName;
    private TextView bankCarkNo;
    private TextView id_fee_description;

    private EditText et_code;
    private TextView tv_getcode;


    private MoneyEditText et_amt;



    private BankCardInfo vo;

    private String orderId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_kjsk);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("快捷收款");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 3));
                }
            });
        }
        initView();
        loadData();
        loadfeeDecriptionData();

        DialogUtils.showDialogTb(mContext, true);

    }





    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
//            int temp = StringUtils.getInt(StringUtils.changeY2F(et_amt.getMoneyText()));
//            if (temp < 58900){
//                ToastUtils.CustomShow(mContext, "交易金额最低589");
//                return;
//            }



            //在这里调用服务器的接口，获取数据
            loadfeeDecriptionData();
        }
    };


    private void initView() {
        imageView = findViewById(R.id.icon_bank);
        bankName = textView(R.id.id_bank_name);
        userName = textView(R.id.id_username);
        bankCarkNo = textView(R.id.id_bank_card);
        id_fee_description = textView(R.id.id_fee_description);
        linearLayout(R.id.ll_change).setOnClickListener(this);

        // 获取验证码
        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

        // 验证码
        et_code = editText(R.id.et_code);

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

                if (!getIsPay()){
                    return;
                }


                if(delayRun!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }


                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 1000);
            }
        });

        button(R.id.btn_commit).setOnClickListener(this);

    }


    private boolean getIsPay(){
        Date nowTime = StringUtils.getDateFromString(StringUtils.getFormatCurTime(), "yyyyMMddHHmmss");
        Date startTime  = StringUtils.getDate(StringUtils.getCurDate()+"000000", "yyyyMMddHHmmss");
        Date endTime  = StringUtils.getDate(StringUtils.getCurDate()+"210000", "yyyyMMddHHmmss");
        if (!StringUtils.isEffectiveDate(nowTime, startTime, endTime)){
            ToastUtils.CustomShow(mContext, "该时间段内不能交易");
            return false;
        }
        return true;
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
            case R.id.tv_getcode://获取验证码
                // 获取验证码
                getValidCode();// 从服务器获取验证码
                break;
            default:
                break;
        }
    }


    /**
     * 从服务器获取验证码
     *
     */
    private void getValidCode() {

        if (!getIsPay()){
            return;
        }

        if (StringUtils.isBlank(et_amt.getText().toString().trim())){
            ToastUtils.CustomShow(mContext, "请输入金额");
            return;
        }
        MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS2, 1000, tv_getcode,
                getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
        countDowntimer.start();

        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("cardId", vo.getId());
        params.add("transAmount", StringUtils.changeY2F(et_amt.getText().toString().trim()));
        HttpUtil.get(ConfigXy.XY_KJSK_PREORDER, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {
                disShowProgress();
                try {
                    JSONObject result = new JSONObject(response);
                    if (!result.optBoolean("status")) {
                        ToastUtils.CustomShow(mContext, result.optString("message"));
                    }

                    JSONObject dataStr = result.getJSONObject("data");
                    orderId = dataStr.optString("orderId");

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

    /**
     * 提交
     */
    private void doCommit() {

        if (!getIsPay()){
            return;
        }

        if (StringUtils.isBlank(vo.getId())) {
            //补全信息
            startActivityForResult(new Intent(mContext, ActivityCreditSupply.class).putExtra("bankbillId", vo.getBankbillId()), 0);
            return;
        }

        if (StringUtils.isBlank(et_amt.getText().toString().trim())){
            ToastUtils.CustomShow(mContext, "请输入金额");
            return;
        }

//        int temp = StringUtils.getInt(StringUtils.changeY2F(et_amt.getMoneyText()));
//        if (temp < 58900){
//            ToastUtils.CustomShow(mContext, "交易金额最低589");
//            return;
//        }

        if (StringUtils.isBlank(et_code.getText().toString().trim())) {
            ToastUtils.showShort(this, "验证码不能为空");
            et_code.requestFocus();
            return;
        }


        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("verificationCode", et_code.getText().toString().trim());
        params.add("orderId", orderId);
        HttpUtil.get(ConfigXy.XY_KJSK_PAY_SUBMIT, params, requestListener);

    }



    private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

        @Override
        public void success(String response) {
            disShowProgress();
            try {
                JSONObject result = new JSONObject(response);
                if (!result.optBoolean("status")) {
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                }else{
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                    finish();
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
                                    bankCarkNo.setText(vo.getCard_num());
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
                    bankCarkNo.setText(vo.getCard_num());
                }
            }
        }else if (requestCode == 1 && resultCode == 4){
            if (vo == null){

                finish();
            }
        }if (requestCode == 2 && resultCode == 3){
            String cardId = data.getStringExtra("cardId");
            vo.setId(cardId);
        }
    }



    private void loadfeeDecriptionData() {

//        showProgress("加载中...");

        String url = ConfigXy.FEE_DESCRIPTION1;
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("tranAmt", StringUtils.changeY2F(et_amt.getText().toString().trim()));
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

                    String feeDescription = result.getString("message");
                    if (!StringUtils.isBlank(feeDescription)){
                        id_fee_description.setText("实际到账金额: "+feeDescription+"元");
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



}
