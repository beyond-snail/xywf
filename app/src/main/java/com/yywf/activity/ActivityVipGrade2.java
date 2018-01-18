package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyGridView;
import com.yywf.R;
import com.yywf.adapter.AdapterVipGrade;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.VipGrade;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActivityVipGrade2 extends BaseActivity implements OnClickListener {
    private final String TAG = "ActivityVipGrade";

    private VipGrade vo;
    private List<VipGrade> list = new ArrayList<VipGrade>();

    private AdapterVipGrade adapter;

//    private TextView gradedemand;
//    private TextView gradegive;
//    private TextView total;
//    private TextView totalBetter;

    private TextView tv_jlj_amt;
    private TextView tv_fl_amt;
    private TextView tv_fr_amt;


    private MyGridView gridview;


    private MyCustomDialog dialog;

    private int baseAmount = 19900;

    private int currIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_purchase_grade2);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("购买等级");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 10));
                }
            });
        }
        initView();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {


//        gradedemand = textView(R.id.gradedemand);
//        gradegive = textView(R.id.gradegive);
//        total = textView(R.id.total);
//        totalBetter = textView(R.id.id_count_amt);

        tv_jlj_amt = textView(R.id.tv_jlj_amt);
        tv_fl_amt = textView(R.id.tv_fl_amt);
        tv_fr_amt = textView(R.id.tv_fr_amt);


        gridview = (MyGridView) findViewById(R.id.id_gridview);
        adapter = new AdapterVipGrade(mContext, list);
        gridview.setAdapter(adapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e(TAG, "position="+position);


                currIndex = position;

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setDefault(false);
                }

                vo = list.get(position);
                vo.setDefault(true);
                adapter.notifyDataSetChanged();

                setDefault(vo);

                //如果大于等于VIP4，限额，线下收款
//                if (position >= 3){
//                    ToastUtils.showShort(mContext, "由于限额，请线下付款");
////                    VipGrade vipGrade = list.get(position);
//                    ShowAccount(vo);
////                    return;
//                }

            }
        });

        button(R.id.btn_commit).setOnClickListener(this);

//        textView(R.id.id_grade_msg).setOnClickListener(this);
    }

    private void setDefault(VipGrade vo){
//        gradedemand.setText(vo.getGradedemand()+"");
//        gradegive.setText(vo.getGradegive()+"");
//        total.setText((vo.getGradedemand()+vo.getGradegive())+"");
//        int amount = vo.getGradegive()*baseAmount;
//        totalBetter.setText(StringUtils.formatIntMoney(amount));
        tv_jlj_amt.setText(StringUtils.formatIntMoney(vo.getCashback()));
        tv_fl_amt.setText(vo.getProfitratio()+"");

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_grade_msg:
                startActivity( new Intent(mContext, ActivityReadWord.class));
                break;
            case R.id.btn_commit:
                final String phone = "0571-8858-6888";
                DialogUtils.alert("拨打 " + phone, null, mContext, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
//                if (vo == null){
//                    return;
//                }
//
//                if (currIndex >= 3){
//                    ToastUtils.showShort(mContext, "由于限额，请线下付款");
//                    ShowAccount(vo);
//                    return;
//                }
//
//
//
//                Spanned str = Html.fromHtml("您将拥有"+"<font color='red' size='20'>"+vo.getGradedemand()+"</font>"+"次"+vo.getGradename()+"等级的会员分润比例, 共"+
//                        "<font color='red' size='20'>"+StringUtils.formatIntMoney(vo.getPurchasePrice())+"</font>"+"元"+"(返利"+"<font color='red' size='20'>"+StringUtils.formatIntMoney(vo.getCashback())+"</font>"+"元,分润"+
//                        "<font color='red' size='20'>"+"万"+vo.getProfitratio()+"</font>"+")"
//                );
//
//                dialog = DialogUtils.showDialog(mContext, "温馨提示", "取消", "确定", str, new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                }, new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(mContext, ActivitySaleVipGrade.class).putExtra("payAmount", vo.getPurchasePrice()).putExtra("gradeId", vo.getId()));
//                        dialog.dismiss();
//                    }
//                });
                break;
        }
    }


    private void ShowAccount(final VipGrade vo){

        dialog = DialogUtils.showDialog2(mContext, "温馨提示", "取消", "确定", "您将付款"+StringUtils.formatIntMoney(vo.getPurchasePrice())+"元", "信易（杭州）互联网科技有限公司\r\n账号: 19020101040041032\r\n开户行: 中国农业银行杭州市城西支行", new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {

        showProgress("正在发送");
        RequestParams params = new RequestParams();
        params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
        params.put("token", UtilPreference.getStringValue(mContext, "token"));
        HttpUtil.get(ConfigXy.GRADE_LIST, params, new HttpUtil.RequestListener() {

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

                    JSONObject obj = result.getJSONObject("data");
                    String grade_list = obj.optString("grade_list");
                    if (!StringUtils.isBlank(grade_list)) {

                        Gson gson = new Gson();
                        List<VipGrade> vipGradeList = gson.fromJson(grade_list, new TypeToken<List<VipGrade> >() {
                        }.getType());
                        if (vipGradeList.size() > 0) {
                            //去掉前三个
                            List<VipGrade> tempList = new ArrayList<>();
                            for (int i = 3; i < vipGradeList.size(); i++){
                                tempList.add(vipGradeList.get(i));
                            }

                            list.clear();
                            list.addAll(list.size(), tempList);

                            //设置默认一个
//                            list.get(list.size()-1).setDefault(true);
                            vo = list.get(list.size()-1);
                            vo.setDefault(true);
                            setDefault(vo);

                        } else {
                           ToastUtils.CustomShow(mContext, "返回套餐为空");
                        }
                    }else{
                        ToastUtils.CustomShow(mContext, "返回数据错误");
                    }

                    adapter.notifyDataSetChanged();

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


}
