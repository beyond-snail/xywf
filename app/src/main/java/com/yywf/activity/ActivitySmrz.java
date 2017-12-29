package com.yywf.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.utils.ValidateUtil;
import com.tool.utils.view.MyCountDownTimer;
import com.tool.utils.view.Split4EditTextWatcher;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class ActivitySmrz extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivitySmrz";
    private EditText et_user_name;
    private EditText et_user_id;
    private EditText et_card_no;
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_getcode;

    private TextView tv_user_name;
    private TextView tv_user_id;
    private EditText tv_card_no;
    private TextView tv_phone;
    private TextView tvBank;

    private LinearLayout ll_smrz;
    private LinearLayout ll_smrz_success;
    private LinearLayout ll_bankName;

    private String validCode;
    private String bankCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_smrz);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("实名认证");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {
        et_user_name = editText(R.id.et_user_name);
        et_user_id = editText(R.id.et_user_id);
        et_user_id.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X' };
                return numberChars;
            }
        });
        et_card_no = editText(R.id.et_card_no);
        et_card_no.addTextChangedListener(new Split4EditTextWatcher(et_card_no));
        et_phone = editText(R.id.et_phone);
        et_code = editText(R.id.et_code);
        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

        tv_user_name = textView(R.id.tv_user_name);
        tv_user_id = textView(R.id.tv_user_id);
        tv_card_no = editText(R.id.tv_card_no);
//        tv_card_no.addTextChangedListener(new Split4EditTextWatcher(tv_card_no));
        tv_phone = textView(R.id.tv_phone);

        ll_smrz = linearLayout(R.id.ll_smrz);
        ll_smrz_success = linearLayout(R.id.ll_smrz_success);
        ll_bankName = linearLayout(R.id.tv_ll_card_text);
        ll_bankName.setOnClickListener(this);

        tvBank = (TextView) findViewById(R.id.tv_card_bank);

        button(R.id.btn_commit).setOnClickListener(this);






    }




    private void checkApproveStatus(){
        //判断是否实名认证
        int approve_status = UtilPreference.getIntValue(mContext, "approve_status");
        if (approve_status == 0){
            ll_smrz.setVisibility(View.VISIBLE);
            ll_smrz_success.setVisibility(View.GONE);
        }else{
            ll_smrz.setVisibility(View.GONE);
            ll_smrz_success.setVisibility(View.VISIBLE);
            //去获取认证的信息
            doGetApproveInfo();
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        checkApproveStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ll_card_text:
                startActivityForResult(new Intent(this, ActivityBankList.class).putExtra("cardType", 0), 1);
                break;
            case R.id.btn_commit://提交
                try {
                    doCommit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_getcode://获取验证码
                // 获取验证码
                String phone = et_phone.getText().toString();
                if (StringUtils.isCellPhone(phone)) {
                    et_code.requestFocus();
                    getValidCode(phone);// 从服务器获取验证码
                    et_code.setTextColor(mContext.getResources().getColor(R.color.gray));
                } else {
                    et_phone.requestFocus();
                    et_phone.setError("输入手机号格式不正确！");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            String s = data.getStringExtra("bankName");
            bankCode = data.getStringExtra("bankCode");
            tvBank.setText(s);
        }
    }


    /**
     * 从服务器获取验证码
     *
     * @param phone
     */
    private void getValidCode(final String phone) {
        MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS, 1000, tv_getcode,
                getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
        countDowntimer.start();

        showProgress("正在发送");
        RequestParams params = new RequestParams();
        params.add("phone", phone);
        HttpUtil.get(ConfigXy.XY_SMSVALDATE, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {
                disShowProgress();
                try {
                    JSONObject result = new JSONObject(response);
                    if (!result.optBoolean("status")) {
                        ToastUtils.CustomShow(mContext, result.optString("message"));
                    }

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
    private void doCommit() throws ParseException {

        String bandCard = StringUtils.replaceBlank(et_card_no.getText().toString().trim());


        if (StringUtils.isBlank(et_user_name.getText().toString().trim())) {
            ToastUtils.showShort(this, "姓名不能为空");
            et_user_name.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_user_id.getText().toString().trim())) {
            ToastUtils.showShort(this, "身份证号不能为空");
            et_user_id.requestFocus();
            return;
        }

        if (!ValidateUtil.IDCardValidate(et_user_id.getText().toString().trim())) {
            ToastUtils.showShort(this, "身份证号格式不正确");
            et_user_id.requestFocus();
            return;
        }



        if (StringUtils.isBlank(bandCard)) {
            ToastUtils.showShort(this, "银行卡号不能为空");
            et_card_no.requestFocus();
            return;
        }

        if (!StringUtils.checkBankCard(bandCard)) {
            ToastUtils.showShort(this, "请输入正确银行卡号");
            et_card_no.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_phone.getText().toString().trim())) {
            ToastUtils.showShort(this, "手机号不能为空");
            et_phone.requestFocus();
            return;
        }

        if (!StringUtils.isCellPhone(et_phone.getText().toString().trim())) {
            ToastUtils.showShort(this, "手机号码格式不正确");
            et_phone.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_code.getText().toString().trim())) {
            ToastUtils.showShort(this, "验证码不能为空");
            et_code.requestFocus();
            return;
        }




        showProgress("加载中...");
        RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		params.add("idName", et_user_name.getText().toString().trim());
		params.add("phone", et_phone.getText().toString().trim());
		params.add("idNo", et_user_id.getText().toString().trim());
		params.add("bankCode", bankCode);
		params.add("cardNum", bandCard);
		params.add("code", et_code.getText().toString().trim());
        HttpUtil.get(ConfigXy.XY_SMRZ, params, requestListener);

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
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                }else{
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                    //实名认证成功
                    UtilPreference.saveInt(mContext, "approve_status", 1);

                    ll_smrz.setVisibility(View.GONE);
                    ll_smrz_success.setVisibility(View.VISIBLE);

                    doGetApproveInfo();
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



    private void doGetApproveInfo() {
        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.XY_GET_SMRZ_INFO, params, requestListener1);
    }


    private HttpUtil.RequestListener requestListener1 = new HttpUtil.RequestListener() {

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
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                }else{
                    JSONObject dataStr = result.getJSONObject("data");
                    String card_num = dataStr.optString("card_num");
                    String id_no = dataStr.optString("id_no");
                    String phone = dataStr.optString("phone");
                    String member_name = dataStr.optString("member_name");

                    tv_user_name.setText(member_name);
                    tv_card_no.setText(StringUtils.formatCardNo(card_num));
                    tv_user_id.setText(StringUtils.formatCardNo(id_no));
                    tv_phone.setText(StringUtils.formatPhoneNo(phone));

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
