package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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

public class ActivityCreditSupply extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityChangeDebitInfo";
    private EditText et_user_name;
    private EditText et_card_no;
    private EditText et_iccu;

    private EditText et_cvv2;
    private EditText et_date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_credit_supply);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("补全信用卡信息");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {
        et_user_name = editText(R.id.et_user_name);
        et_card_no = editText(R.id.et_card_no);
        et_card_no.addTextChangedListener(new Split4EditTextWatcher(et_card_no));
        et_iccu = editText(R.id.et_iccu);
        et_cvv2 = editText(R.id.et_cvv2);
        et_date = editText(R.id.et_date);


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


        String bandCard = StringUtils.replaceBlank(et_card_no.getText().toString().trim());


        if (StringUtils.isBlank(et_user_name.getText().toString().trim())) {
            ToastUtils.showShort(this, "姓名不能为空");
            et_user_name.requestFocus();
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



        if (StringUtils.isBlank(et_iccu.getText().toString().trim())) {
            ToastUtils.showShort(this, "发卡行不能为空");
            et_iccu.requestFocus();
            return;
        }

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





        showProgress("加载中...");
        RequestParams params = new RequestParams();
//		params.add("account", username);
//		params.add("password", password);
        HttpUtil.get(ConfigXy.XY_CREDIT_ADD, params, requestListener);

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


}
