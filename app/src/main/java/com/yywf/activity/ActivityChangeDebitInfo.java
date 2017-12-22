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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.yywf.model.BankCardInfo;
import com.yywf.model.BankUserInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

public class ActivityChangeDebitInfo extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityChangeDebitInfo";
    private EditText et_user_name;
    private EditText et_user_id;
    private EditText et_card_no;
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_getcode;
    private TextView tvBank;



    private LinearLayout ll_bankName;

    private String bankCode;

    private BankUserInfo vo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_change_debit_info);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("绑定银行卡");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {
        et_user_name = editText(R.id.et_user_name);
        et_user_id = editText(R.id.et_user_id);
        et_card_no = editText(R.id.et_card_no);
        et_card_no.addTextChangedListener(new Split4EditTextWatcher(et_card_no));
        et_phone = editText(R.id.et_phone);
        et_code = editText(R.id.et_code);
        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);
        tvBank = textView(R.id.tv_card_bank);

        ll_bankName = linearLayout(R.id.tv_ll_card_text);
        ll_bankName.setOnClickListener(this);



        button(R.id.btn_commit).setOnClickListener(this);






    }


    @Override
    protected void onResume() {
        super.onResume();
        doGetApproveInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

        if (StringUtils.isBlank(tvBank.getText().toString().trim())){
            ToastUtils.showShort(this, "发卡银行不能为空");
            return;
        }





        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("cardId", vo.getCard_id());
        params.add("idName", vo.getMember_name());
        params.add("phone", et_phone.getText().toString().trim());
        params.add("idNo", vo.getId_no());
        params.add("cardNum", bandCard);
        params.add("newBankNo", bankCode);
        params.add("code", et_code.getText().toString().trim());
        HttpUtil.get(ConfigXy.CHANGE_DEBIT_INFO, params, requestListener);

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
                    String dataStr = result.optString("data");
                    if (!StringUtils.isBlank(dataStr)) {
                        Gson gson = new Gson();
                        vo = gson.fromJson(dataStr, new TypeToken<BankUserInfo>() {
                        }.getType());

                        if (!StringUtils.isBlank(vo.getMember_name())) {
                            et_user_name.setText(vo.getMember_name());
                        }
                        if (!StringUtils.isBlank(vo.getId_no())){
                            et_user_id.setText(StringUtils.formatCardNo(vo.getId_no()));
                        }
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


}
