package com.yywf.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.tool.utils.banner.AutoPlayManager;
import com.tool.utils.banner.ImageIndicatorView;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterVipGrade;
import com.yywf.adapter.AdapterVipGrade3;
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


public class ActivityVipGrade3 extends BaseActivity implements OnClickListener {
    private final String TAG = "ActivityVipGrade";

    private MyListView myListView;
    private List<VipGrade> list = new ArrayList<VipGrade>();
    private AdapterVipGrade3 adapter;
    private LinearLayout ll_advertis;
    private MyCustomDialog dialog;
    private MyCustomDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_purchase_grade3);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("升级代理");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null) {
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, ActivityReadTxt.class).putExtra("type", 26));
                }
            });
        }
        initAdvertis();
        initView();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {

        linearLayout(R.id.ll_xy_dl).setOnClickListener(this);
        textView(R.id.tv_dlxy).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        myListView = findViewById(R.id.listview);
        adapter = new AdapterVipGrade3(mContext, list);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String title = "<font color='red'><b><big>恭喜您</big></b>" + "</font>" + "<font color='black'>成为" + list.get(i).getGradename() + "</font>";
                String content = "请缴纳" + StringUtils.formatIntMoney(list.get(i).getEarnestMoney()) + "元保证金，返利" + StringUtils.formatIntMoney(list.get(i).getCashback()) + "，分润万" + list.get(i).getProfitratio() + "，目标任务完成" + list.get(i).getGradedemand() + "个会员或" + list.get(i).getIshot() + "万营业额。";
                dialog2 = DialogUtils.showDialogDl(mContext, Html.fromHtml(title), "取消", "充值", content, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();

                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                        startActivity(new Intent(mContext, ActivityFyPay.class).putExtra("amount", list.get(i).getEarnestMoney()));
                    }
                });
            }
        });

        button(R.id.btn_commit).setOnClickListener(this);

    }


    //初始化广告栏
    private void initAdvertis() {
        // 广告栏开始
        ll_advertis = (LinearLayout) findViewById(R.id.advertis);
        // 设置宽度高度一致
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_advertis.getLayoutParams();
        linearParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2);// 640-370
        // 750-434
        ll_advertis.setLayoutParams(linearParams);
        local();
    }


    //系统本地图片加载
    public void local() {
        // 声明一个数组, 指定图片的ID
        final Integer[] resArray = new Integer[]{R.drawable.shenjidaili
        };
        ImageIndicatorView indicate_view = new ImageIndicatorView(mContext);
        // 把数组交给图片展播组件
        indicate_view.setupLayoutByDrawable(resArray);
        // 展播的风格
//        indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
        indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        // 显示组件
        indicate_view.show();
        final AutoPlayManager autoBrocastManager = new AutoPlayManager(indicate_view);
        //设置开启自动广播
        autoBrocastManager.setBroadcastEnable(true);
        //autoBrocastManager.setBroadCastTimes(5);//loop times
        //设置开始时间和间隔时间
        autoBrocastManager.setBroadcastTimeIntevel(3000, 3000);
        //设置循环播放
        autoBrocastManager.loop();
        ll_advertis.addView(indicate_view);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_grade_msg:
                startActivity(new Intent(mContext, ActivityReadWord.class));
                break;
            case R.id.ll_xy_dl://代扣协议
                startActivity(new Intent(mContext, ActivityReadTxt.class).putExtra("type", 27));
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
                break;
        }
    }


    private void ShowAccount(final VipGrade vo) {

        dialog = DialogUtils.showDialog2(mContext, "温馨提示", "取消", "确定", "您将付款" + StringUtils.formatIntMoney(vo.getPurchasePrice()) + "元", "信易（杭州）互联网科技有限公司\r\n账号: 19020101040041032\r\n开户行: 中国农业银行杭州市城西支行", new OnClickListener() {
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
        list.clear();
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

                    JSONObject obj = result.getJSONObject("data");
                    String grade_list = obj.optString("grade_list");
                    if (!StringUtils.isBlank(grade_list)) {

                        Gson gson = new Gson();
                        List<VipGrade> vipGradeList = gson.fromJson(grade_list, new TypeToken<List<VipGrade>>() {
                        }.getType());

                        if (vipGradeList.size() > 0) {
                            list.addAll(vipGradeList);
                            if (list.size() > 0) {
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }

                        } else {
                            if (list.size() > 0) {
                                linearLayout(R.id.id_no_data).setVisibility(View.GONE);
                                myListView.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout(R.id.id_no_data).setVisibility(View.VISIBLE);
                                myListView.setVisibility(View.GONE);
                            }
                        }
                    } else {
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
