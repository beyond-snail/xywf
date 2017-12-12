package com.yywf.activity;

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

public class ActivityEditLoginPwd extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityEditLoginPwd";
    private EditText et_old_login_pwd;
    private EditText et_new_login_pwd;
    private EditText et_comfirm_new_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_edit_login_pwd);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("修改登录密码");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {

        et_old_login_pwd = editText(R.id.et_old_login_pwd);
        et_new_login_pwd = editText(R.id.et_new_login_pwd);
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


        if (StringUtils.isBlank(et_old_login_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "旧登录密码不为空");
            et_old_login_pwd.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_new_login_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "新登录密码不能为空");
            et_new_login_pwd.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_comfirm_new_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "确认密码不能为空");
            et_comfirm_new_pwd.requestFocus();
            return;
        }

        if (!StringUtils.isEquals(et_new_login_pwd.getText().toString().trim(), et_comfirm_new_pwd.getText().toString().trim())) {
            ToastUtils.showShort(this, "两次输入密码不一致");
            return;
        }



        showProgress("加载中...");
        RequestParams params = new RequestParams();
//		params.add("account", username);
//		params.add("password", password);
        HttpUtil.get(ConfigXy.XY_EDIT_LOGIN_PWD, params, requestListener);

    }

    private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

        @Override
        public void success(String response) {
            disShowProgress();
            try {
                JSONObject obj = new JSONObject(response);


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
