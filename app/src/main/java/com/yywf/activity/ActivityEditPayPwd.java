package com.yywf.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.text.ParseException;

public class ActivityEditPayPwd extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityEditPayPwd";
    private EditText et_old_pay_pwd;
    private EditText et_new_pay_pwd;
    private EditText et_comfirm_new_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_edit_pay_pwd);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("修改支付密码");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {

        et_old_pay_pwd = editText(R.id.et_old_pay_pwd);
        et_new_pay_pwd = editText(R.id.et_new_pay_pwd);
        et_comfirm_new_pwd = editText(R.id.et_comfirm_new_pwd);



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


    /**
     * 提交
     */
    private void doCommit() throws ParseException {


        if (StringUtils.isBlank(et_old_pay_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "旧支付密码不为空");
            et_old_pay_pwd.requestFocus();
            return;
        }

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
        params.add("oldPassword", et_old_pay_pwd.getText().toString().trim());
        params.add("newPassword", et_new_pay_pwd.getText().toString().trim());
        params.add("type", "2");
        HttpUtil.get(ConfigXy.XY_EDIT_LOGIN_PWD, params, requestListener);

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
