package com.yywf.activity;

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
import com.tool.utils.utils.ValidateUtil;
import com.tool.utils.view.Split4EditTextWatcher;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.text.ParseException;

public class ActivityCreditAdd extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivityChangeDebitInfo";
    private EditText et_user_name;
    private EditText et_user_id;
    private EditText et_card_no;
    private EditText et_phone;
    private EditText et_bill_day;
    private EditText et_repayment_day;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_credit);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("添加信用卡");
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
        et_bill_day = editText(R.id.et_bill_day);
        et_repayment_day = editText(R.id.et_repayment_day);



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

        if (StringUtils.isBlank(et_card_no.getText().toString().trim())) {
            ToastUtils.showShort(this, "银行卡号不能为空");
            et_card_no.requestFocus();
            return;
        }

        if (!StringUtils.checkBankCard(et_card_no.getText().toString().trim())) {
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

        if (StringUtils.isBlank(et_bill_day.getText().toString().trim())) {
            ToastUtils.showShort(this, "账单日不为空");
            et_phone.requestFocus();
            return;
        }

        if (StringUtils.isBlank(et_repayment_day.getText().toString().trim())) {
            ToastUtils.showShort(this, "还款日不为空");
            et_phone.requestFocus();
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
