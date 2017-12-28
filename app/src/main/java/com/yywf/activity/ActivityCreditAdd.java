package com.yywf.activity;

import android.content.Intent;
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

public class ActivityCreditAdd extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityCreditAdd";
    private EditText et_user_name;
    private EditText et_user_id;
    private EditText et_card_no;
    private EditText et_iccu;
    private EditText et_bill_day;
    private EditText et_repayment_day;

    private EditText et_cvv2;
    private EditText et_date;

    private EditText et_phone;
    private EditText et_code;
    private TextView tv_getcode;


    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_credit);
        MyActivityManager.getInstance().addActivity(this);
        int type = getIntent().getIntExtra("type", 0);
        if (type != 2) {
            initTitle("添加信用卡");
        }else{
            initTitle("手动输入");
        }
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
        et_iccu = editText(R.id.et_iccu);
        et_bill_day = editText(R.id.et_bill_day);
        et_repayment_day = editText(R.id.et_repayment_day);


        et_phone = editText(R.id.et_phone);
        et_code = editText(R.id.et_code);
        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

        et_cvv2 = editText(R.id.et_cvv2);
        et_date = editText(R.id.et_date);





        button(R.id.btn_commit).setOnClickListener(this);
        linearLayout(R.id.ll_xy_dk).setOnClickListener(this);



        doGetApproveInfo();

    }


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
                if (!result.optBoolean("status")) {
                    ToastUtils.CustomShow(mContext, result.optString("message"));
                }else{
                    JSONObject dataStr = result.getJSONObject("data");
                    String id_no = dataStr.optString("id_no");
                    String member_name = dataStr.optString("member_name");

                    if (!StringUtils.isBlank(member_name)) {
                        et_user_name.setEnabled(false);
                        et_user_name.setText(member_name);
                    }
                    if (!StringUtils.isBlank(id_no)) {
                        et_user_id.setEnabled(false);
                        et_user_id.setText(StringUtils.formatCardNo(id_no));
                        userId = id_no;
                    }
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
            case R.id.btn_commit://提交
                try {
                    doCommit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_xy_dk://代扣协议
                startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 2));
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


    /**
     * 从服务器获取验证码
     *
     * @param phone
     */
    private void getValidCode(final String phone) {
        MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS, 1000, tv_getcode,
                getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
        countDowntimer.start();

        RequestParams params = new RequestParams();
        params.add("phone", phone);
        showProgress("正在发送");
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
        if (!StringUtils.isBlank(userId)){
            if (!ValidateUtil.IDCardValidate(userId)) {
                ToastUtils.showShort(this, "身份证号格式不正确");
                et_user_id.requestFocus();
                return;
            }
        }else{
            if (!ValidateUtil.IDCardValidate(et_user_id.getText().toString().trim())) {
                ToastUtils.showShort(this, "身份证号格式不正确");
                et_user_id.requestFocus();
                return;
            }
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

//        if (StringUtils.isBlank(et_iccu.getText().toString().trim())) {
//            ToastUtils.showShort(this, "发卡行不能为空");
//            et_iccu.requestFocus();
//            return;
//        }

        if (StringUtils.isBlank(et_cvv2.getText().toString().trim())) {
            ToastUtils.showShort(this, "CVV2不能为空");
            et_cvv2.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_date.getText().toString().trim())) {
            ToastUtils.showShort(this, "有效期不能为空");
            et_date.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_code.getText().toString().trim())) {
            ToastUtils.showShort(this, "验证码不能为空");
            et_code.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_bill_day.getText().toString().trim())) {
            ToastUtils.showShort(this, "账单日不为空");
            et_bill_day.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_repayment_day.getText().toString().trim())) {
            ToastUtils.showShort(this, "还款日不为空");
            et_repayment_day.requestFocus();
            return;
        }






        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("cardNum",bandCard);
        params.add("idName", et_user_name.getText().toString().trim());
        if (StringUtils.isBlank(userId)) {
            params.add("idNo", et_user_id.getText().toString().trim());
        }else{
            params.add("idNo", userId.trim());
        }
        params.add("phone", et_phone.getText().toString().trim());
        params.add("cardCvn", et_cvv2.getText().toString().trim());
        params.add("cardExpdate", et_date.getText().toString().trim());
        params.add("billDate", et_bill_day.getText().toString().trim());
        params.add("payDate", et_repayment_day.getText().toString().trim());
        params.add("code", et_code.getText().toString().trim());
        HttpUtil.get(ConfigXy.XY_CREDIT_ADD, params, requestListener);

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


}
