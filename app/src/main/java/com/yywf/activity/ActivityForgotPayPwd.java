package com.yywf.activity;

import android.os.Bundle;
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
import com.tool.utils.view.MyCountDownTimer;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class ActivityForgotPayPwd extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityForgotPayPwd";
    private EditText et_code;
    private TextView tv_getcode;
    private EditText et_new_pay_pwd;
    private EditText et_comfirm_new_pwd;

    private LinearLayout ll_code;
    private LinearLayout ll_edit_pwd;

    private String validCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_forgot_pay_pwd);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("忘记支付密码");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {

        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);
        et_code = editText(R.id.et_code);
        et_new_pay_pwd = editText(R.id.et_new_pay_pwd);
        et_comfirm_new_pwd = editText(R.id.et_comfirm_new_pwd);

        ll_code = linearLayout(R.id.ll_code);
        ll_edit_pwd = linearLayout(R.id.ll_edit_pwd);




        button(R.id.btn_commit).setOnClickListener(this);
        button(R.id.btn_next).setOnClickListener(this);





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
            case R.id.btn_next:
                if (StringUtils.isBlank(et_code.getText().toString().trim())) {
                    ToastUtils.showShort(this, "验证码不能为空");
                    et_code.requestFocus();
                    return;
                }
                ll_code.setVisibility(View.GONE);
                ll_edit_pwd.setVisibility(View.VISIBLE);
                validCode = et_code.getText().toString().trim();
                initTitle("重置支付密码");
                break;
            case R.id.btn_commit://提交
                try {
                    doCommit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_getcode://获取验证码
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
        MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS, 1000, tv_getcode,
                getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
        countDowntimer.start();

        RequestParams params = new RequestParams();
        params.add("phone",  UtilPreference.getStringValue(mContext, "userName"));
        showProgress("正在发送");
        HttpUtil.get(ConfigXy.XY_SMSVALDATE, params, new HttpUtil.RequestListener() {

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




        if (StringUtils.isBlank(et_new_pay_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "新支付密码不能为空");
            et_new_pay_pwd.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_comfirm_new_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "确认密码不能为空");
            et_comfirm_new_pwd.requestFocus();
            return;
        }

        if (!StringUtils.isEquals(et_new_pay_pwd.getText().toString().trim(), et_comfirm_new_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "两次输入密码不一致");
            return;
        }



        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("phone", UtilPreference.getStringValue(mContext, "userName"));
        params.add("password", et_new_pay_pwd.getText().toString().trim());
        params.add("code", validCode);
        params.add("type", "2");
        HttpUtil.get(ConfigXy.XY_FORGOT_PAY_PWD, params, requestListener);

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
