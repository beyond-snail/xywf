package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.NumberKeyListener;
import android.util.Base64;
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
import com.yywf.aly.Keys;
import com.yywf.aly.MyAlipayClient;
import com.yywf.aly.PayResult;
import com.yywf.config.ConfigXy;
import com.yywf.config.ConstApp;
import com.yywf.config.EnumConsts;
import com.yywf.http.HttpUtil;
import com.yywf.model.BankCardInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class ActivitySaleVipGrade extends BaseActivity implements OnClickListener {

    private final String TAG = "ActivitySaleVipGrade";


    private LinearLayout id_wallet;
    private LinearLayout id_wx;
    private LinearLayout id_aly;
    private LinearLayout id_card;

    private TextView tv_dk;
    private TextView tv_wh;

    private int payType;

    private int payAmount = 0;
    private int gradeId = 0;

    private String orderId;
    private BankCardInfo vo;

    private String cardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_sale_vipgrade);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("购买等级");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        initView();
    }


    private void initView() {

        id_wallet = linearLayout(R.id.id_wallet);
        id_wallet.setVisibility(View.GONE);
        id_wx = linearLayout(R.id.id_wx);
        id_wx.setVisibility(View.GONE);
        id_aly = linearLayout(R.id.id_aly);
        id_aly.setVisibility(View.GONE);
        id_card = linearLayout(R.id.id_card);
        id_wallet.setOnClickListener(this);
        id_wx.setOnClickListener(this);
        id_aly.setOnClickListener(this);
        id_card.setOnClickListener(this);

        //默认银行卡
        setTickSet(4);


        tv_dk = textView(R.id.tv_dk);
        tv_wh = textView(R.id.tv_wh);

        button(R.id.btn_commit).setOnClickListener(this);


        payAmount = getIntent().getIntExtra("payAmount", 0);
        gradeId = getIntent().getIntExtra("gradeId", 0);
        
        //生成订单
        loadOrderId();
//        loadRSAKeys();
    }

    private void loadOrderId() {
        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("gradeId", gradeId+"");
        params.add("orderAmount", payAmount+"");
        HttpUtil.get(ConfigXy.GET_ORDER_ID, params, new HttpUtil.RequestListener() {
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
                        orderId = dataStr.optString("orderId");
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
        });
    }


    /**
     * 加载私钥
     */
    private void loadRSAKeys() {
        showProgress("检测支付环境...");
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.HC_GET_RSA_STORE, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {
                disShowProgress();
                try {
                    JSONObject rsa = new JSONObject(response);
                    if (rsa.optInt("code") == -2){
                        UtilPreference.clearNotKeyValues(mContext);
                        // 退出账号 返回到登录页面
                        MyActivityManager.getInstance().logout(mContext);
                        return;
                    }
                    if ("success".equals(rsa.optString("status"))) {
                        // 获取成功
                        JSONObject obj = rsa.getJSONObject("obj");
                        String privateKey = obj.getString("private_key");
                        Keys.RSA_PRIVATE = new String(Base64.decode(privateKey, Base64.DEFAULT));
                        Keys.PARTNER = obj.getString("pid");
                        Keys.SELLER = obj.getString("account");
                        Log.d(TAG, "私钥：" + Keys.RSA_PRIVATE + "，PARTNER：" + Keys.PARTNER + "，SELLER：" + Keys.SELLER);
                    } else {
                        showErrorMsg("支付环境检测失败！");
                        onBackPressed();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg("抱歉，支付环境检测失败！");
                    onBackPressed();
                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showErrorMsg("抱歉，网络异常，支付环境检测失败！");
                onBackPressed();
            }
        });
    }



    // 重置选中状态
    private void setTickSet(int type) {
        imageView(R.id.iv_wallet).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_unpre));
        imageView(R.id.iv_wx).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_unpre));
        imageView(R.id.iv_aly).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_unpre));
        imageView(R.id.iv_card).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_unpre));
        if (type == 1) {
            imageView(R.id.iv_wallet).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_pre));
        } else if (type == 2) {
            imageView(R.id.iv_wx).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_pre));
        } else if (type == 3) {
            imageView(R.id.iv_aly).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_pre));
        } else if (type == 4) {
            imageView(R.id.iv_card).setImageDrawable(getResources().getDrawable(R.drawable.icon_circle_pre));
        }
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
            case R.id.id_wallet:
                payType = 1;
                setTickSet(payType);
                break;
            case R.id.id_wx:
                payType = 2;
                setTickSet(payType);
                break;
            case R.id.id_aly:
                payType = 3;
                setTickSet(payType);
                break;
            case R.id.id_card:
                payType = 4;
                setTickSet(payType);
                //更换银行卡
                startActivityForResult(new Intent(mContext, ActivityBankCardList.class), 1);
                break;
            case R.id.btn_commit://提交

                if (StringUtils.isBlank(orderId)){
                    ToastUtils.showShort(mContext, "订单号为空");
                    break;
                }

                if (payType == 3){
                    doZfbPay(orderId, payAmount);
                    break;
                }


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
        if (requestCode == 1 && resultCode == 2) {
            vo = (BankCardInfo) data.getSerializableExtra("bankInfo");
            cardId = vo.getId();
            if (vo != null){
                if (!StringUtils.isBlank(vo.getCard_num())){
                    if (vo.getCard_num().length() > 4) {
                        tv_wh.setText(StringUtils.getEndStr(vo.getCard_num(), 4));
                    }else{
                        tv_wh.setText(vo.getCard_num());
                    }
                }
            }
        }else if (requestCode == 2 && resultCode == 3){
            cardId = data.getStringExtra("cardId");
        }
    }



    /**
     * 提交
     */
    private void doCommit() throws ParseException {

        if (StringUtils.isBlank(tv_wh.getText().toString().trim())){
            //更换银行卡
            startActivityForResult(new Intent(mContext, ActivityBankCardList.class), 1);
            return;
        }

        if (StringUtils.isBlank(cardId)){
            //补全信息
            startActivityForResult(new Intent(mContext, ActivityCreditSupply.class).putExtra("type", 1).putExtra("bankbillId", vo.getBankbillId()), 2);
            return;
        }


        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("tranAmt", "1");//payAmount+"");
        params.add("cardId", cardId+"");
        params.add("orderId", orderId);
        HttpUtil.get(ConfigXy.CREDIT_PAY, params, requestListener);

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
//                    ToastUtils.CustomShow(mContext, result.optString("message"));
                    DialogUtils.showDialogCode(mContext, new DialogUtils.Callback() {
                        @Override
                        public void getData(String data) {
                            doCodeCommit(data);
                        }
                    });
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

    private void doCodeCommit(String code) {

        showProgress("加载中...");
        RequestParams params = new RequestParams();
        params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.add("token", UtilPreference.getStringValue(mContext, "token"));
        params.add("code", code+"");
        HttpUtil.get(ConfigXy.CREDIT_PAY_CODE, params, new HttpUtil.RequestListener() {
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
        });
    }


    private void doZfbPay(String orderid, int orderAmount) {
        showProgress("准备支付数据...");
        RequestParams params = new RequestParams();
        // params.add("type", "1");
        params.add("orderId", orderid);
        params.add("orderAmount", orderAmount + "");
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.ALY_PAY, params, new HttpUtil.RequestListener() {

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
                    if (result.has("data")) {
                        JSONObject obj = result.getJSONObject("data");
                        if (obj != null) {
                            String notifyUrl = obj.getString("notifyUrl");
                            doAliPay(notifyUrl);
                        }
                    }


                } catch (Exception e) {
                    showMsgAndDisProgress("支付宝支付订单有误，请联系管理员。");
                }
            }

            @Override
            public void failed(Throwable error) {
                Log.i(TAG, "======= 获取失败 ========");
                showE404();
                disShowProgress();
            }
        });
    }


    /**
     * 进入支付
     */
    private void doAliPay(String notifyUrl) {
        Log.d(TAG, "======= 进入支付宝快捷支付 ========");
        // 从服务器生成订单
        Log.i(TAG, "支付生成orderId：" + orderId);
        String body = "用户充值：" + StringUtils.formatIntMoney(payAmount) + "元，实付" + StringUtils.formatIntMoney(payAmount)
                + "元。";
        String appname = getResources().getString(R.string.app_name);
        MyAlipayClient.pay(this, orderId, appname + "消费服务", body, StringUtils.formatIntMoney(payAmount),
                mHandler, notifyUrl);

    }



    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            PayResult result = new PayResult((String) msg.obj);

            switch (msg.what) {
                case MyAlipayClient.RQF_PAY: {

                    if (PayResult.PAY_SUCCESS.equals(result.getResultStatus())) {
                        // 支付成功
                        Log.i(TAG, "======= 支付成功 ========");

//                        AlertUtils.alert("支付成功！", mContext, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                onBackPressed();
//
//                            }
//                        });

                        ToastUtils.showShort(mContext, "支付成功");
                        onBackPressed();

                    } else if (PayResult.PAY_WAITING_CONFIRM.equals(result.getResultStatus())) {
//                        AlertUtils.alert("支付结果确认中，稍后可在(充值记录)处查看支付结果", mContext);
                        ToastUtils.showLong(mContext, "支付结果确认中,稍后可在我的账单中查看支付结果");
                    } else {
//                        AlertUtils.alert("支付失败！", mContext);
                        ToastUtils.showShort(mContext, "支付失败!");
                    }

                    break;
                }
                case MyAlipayClient.RQF_ERROR: {
//                    AlertUtils.alert("请求支付失败", mContext);
                    ToastUtils.showShort(mContext, "请求支付失败!");
                    break;
                }

                default:
                    break;
            }
        };
    };


}
