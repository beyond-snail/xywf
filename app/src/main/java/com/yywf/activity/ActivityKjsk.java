package com.yywf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tool.utils.passwordView.KeyBoardDialog;
import com.tool.utils.passwordView.PayPasswordView;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyCountDownTimer;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityKjsk extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityKjsk";

    private RoundImageView imageView;
    private TextView bankName;
    private TextView userName;
    private TextView bankCarkNo;
    private LinearLayout ll_change;

    private EditText et_code;
    private TextView tv_getcode;


    private MoneyEditText et_amt;

    private KeyBoardDialog keyboard;

    private String validCode;


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
        }
        initView();
    }


    private void initView() {
        imageView = findViewById(R.id.icon_bank);
        bankName = textView(R.id.id_bank_name);
        userName = textView(R.id.id_username);
        bankCarkNo = textView(R.id.id_bank_card);
        linearLayout(R.id.ll_change).setOnClickListener(this);

        // 获取验证码
        tv_getcode = textView(R.id.tv_getcode);
        tv_getcode.setOnClickListener(this);

        // 验证码
        et_code = editText(R.id.et_code);

        et_amt = (MoneyEditText) editText(R.id.et_amt);

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
     */
    private void getValidCode() {
        MyCountDownTimer countDowntimer = new MyCountDownTimer(ConstApp.VOLID_CODE_SECONDS, 1000, tv_getcode,
                getResources().getDrawable(R.drawable.reg_suc_bar1), getResources().getDrawable(R.drawable.reg_suc_bar2));
        countDowntimer.start();

        RequestParams params = new RequestParams();
//        params.add("tele", phone);
        showProgress("正在发送");
        HttpUtil.get(ConfigXy.XY_FORGOT_PAY_PWD_VALDATE, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    validCode = object.getString("result");
                    Log.i("注册码：", validCode);
                    disShowProgress();
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


        if (StringUtils.isBlank(et_amt.getText().toString().trim())){
            ToastUtils.CustomShow(mContext, "请输入金额");
            return;
        }

        if (StringUtils.isBlank(et_code.getText().toString().trim())) {
            ToastUtils.showShort(this, "验证码不能为空");
            et_code.requestFocus();
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
