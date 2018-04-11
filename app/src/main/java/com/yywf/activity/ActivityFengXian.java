package com.yywf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.TxQueryInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityFengXian extends BaseActivity {

    private final String TAG = "ActivityFengXian";
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_amt)
    TextView tvAmt;
    private MyCustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_fengxian);
        ButterKnife.bind(this);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("风险保证金");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
//        if (findViewById(R.id.img_right) != null){
//            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
//            findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 9));
//                }
//            });
//        }

        initView();
        getData();
    }


    private void initView() {


    }


    private void getData() {

        showProgress("加载中...");
        String url = ConfigXy.XY_MY_CAN_TX;
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        params.put("type", 4);

        HttpUtil.get(url, params, new HttpUtil.RequestListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void success(String response) {
                disShowProgress();
                try {

                    JSONObject result = new JSONObject(response);
                    if (result.optInt("code") == -2) {
                        UtilPreference.clearNotKeyValues(mContext);
                        // 退出账号 返回到登录页面
                        MyActivityManager.getInstance().logout(mContext);
                        return;
                    }
                    if (!result.optBoolean("status")) {
                        showErrorMsg(result.getString("message"));
                        return;
                    }

                    TxQueryInfo txRecordDetail = new Gson().fromJson(result.optString("data"), new TypeToken<TxQueryInfo>() {
                    }.getType());
                    if (txRecordDetail != null) {
//                        tv_amt.setText(StringUtils.formatIntMoney(txRecordDetail.getTranAmt()));
//                        tv_no_amt.setText("不可提现余额: " + StringUtils.formatIntMoney(txRecordDetail.getNotranAmt()));
//                        textView(R.id.fee_text).setText(txRecordDetail.getDescription());
//                        textView(R.id.tv_gz).setText(txRecordDetail.getFeeDescription());
                        tvAmt.setText(StringUtils.formatIntMoney(txRecordDetail.getTranAmt()));
                    }


                } catch (Exception e) {
                    e.getMessage();

                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showE404();

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//        loadData();
    }


    private void doCommit() {




        showProgress("加载中...");

        String url = ConfigXy.XY_MY_TX;
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        params.put("tranAmt", StringUtils.changeY2F(tvAmt.getText().toString().trim()));
        params.put("type", 4);

        HttpUtil.get(url, params, new HttpUtil.RequestListener() {

            @SuppressWarnings("unchecked")
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


                    showErrorMsg(result.getString("message"));
                    finish();


                } catch (Exception e) {
                    e.getMessage();

                }
            }

            @Override
            public void failed(Throwable error) {
                disShowProgress();
                showE404();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() {

        dialog = DialogUtils.showDialogFx(mContext, "取消", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                doCommit();
            }
        });
    }
}
