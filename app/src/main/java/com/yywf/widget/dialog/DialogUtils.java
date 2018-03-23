package com.yywf.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.activity.ActivityFyPay;
import com.yywf.activity.ActivitySmrz;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class DialogUtils {


    private static MyCustomDialog Dialog;

    private Callback listener;

    public interface Callback {
        void getData(String data);
    }


    public static MyCustomDialog showDialog(final Context mContext, String title, String leftText, String rightText, Spanned content, View.OnClickListener leftClick, View.OnClickListener rightClick){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        TextView tv_title = view.findViewById(R.id.tv_alert_dialog_title);
        tv_title.setText(title);
        tv_title.setBackgroundResource(R.drawable.bar_top_tips);
        tv_title.setTextColor(mContext.getResources().getColor(com.tool.R.color.white));

        TextView tv_content = view.findViewById(R.id.tv_alert_dialog_message);
        tv_content.setText(content);


        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setText(leftText);
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(leftClick);

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setText(rightText);
        btn_right.setTextColor(mContext.getResources().getColorStateList(R.color.btn_right_selector));
        btn_right.setOnClickListener(rightClick);

        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(false);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        customBuilder.setDisBottomButton(true);
        // 2线在下方
        MyCustomDialog dialog = customBuilder.create();
        dialog.show();
        return dialog;
    }


    public static MyCustomDialog showDialog2(final Context mContext, String title, String leftText, String rightText, String content, String content2, View.OnClickListener leftClick, View.OnClickListener rightClick){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        TextView tv_title = view.findViewById(R.id.tv_alert_dialog_title);
        tv_title.setText(title);
        tv_title.setBackgroundResource(R.drawable.bar_top_tips);
        tv_title.setTextColor(mContext.getResources().getColor(com.tool.R.color.white));

        TextView tv_content = view.findViewById(R.id.tv_alert_dialog_message);
        tv_content.setText(content);
        tv_content.setTextSize(18f);

        if (!StringUtils.isBlank(content2)) {
            TextView tv_content2 = view.findViewById(R.id.tv_alert_dialog_message2);
            tv_content2.setVisibility(View.VISIBLE);
            tv_content2.setText(content2);
        }


        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setText(leftText);
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(leftClick);

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setText(rightText);
        btn_right.setTextColor(mContext.getResources().getColorStateList(R.color.btn_right_selector));
        btn_right.setOnClickListener(rightClick);

        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(false);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        customBuilder.setDisBottomButton(true);
        // 2线在下方
        MyCustomDialog dialog = customBuilder.create();
        dialog.show();
        return dialog;
    }


    public static MyCustomDialog showDialog3(final Context mContext, String title, String leftText, String rightText, String cardNo, String zd, String hk, String amt, View.OnClickListener leftClick, View.OnClickListener rightClick){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog4, null);
        TextView tv_title = view.findViewById(R.id.tv_alert_dialog_title);
        tv_title.setText(title);
        tv_title.setBackgroundResource(R.drawable.bar_top_tips);
        tv_title.setTextColor(mContext.getResources().getColor(com.tool.R.color.white));

        TextView tv_card_no = view.findViewById(R.id.tv_card_no);
        tv_card_no.setText(cardNo);

        TextView tv_zd_day = view.findViewById(R.id.tv_zd_day);
        tv_zd_day.setText(zd);

        TextView tv_hk_day = view.findViewById(R.id.tv_hk_day);
        tv_hk_day.setText(hk);

        TextView tv_bfj = view.findViewById(R.id.tv_bfj);
        tv_bfj.setText("信用卡备付金"+amt+"元");


        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setText(leftText);
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(leftClick);

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setText(rightText);
        btn_right.setTextColor(mContext.getResources().getColorStateList(R.color.btn_right_selector));
        btn_right.setOnClickListener(rightClick);

        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(false);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        customBuilder.setDisBottomButton(true);
        // 2线在下方
        MyCustomDialog dialog = customBuilder.create();
        dialog.show();
        return dialog;
    }


    public static void showDialogTb(final Context mContext, boolean flag){


        //只弹一次
        boolean isFirst = UtilPreference.getBooleanValue(mContext, "tb");

        if (flag) {
            if (isFirst) {
                return;
            }
            UtilPreference.saveBoolean(mContext, "tb", true);
        }



        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog2, null);

        LinearLayout ll_title = view.findViewById(R.id.ll_title);
        ll_title.setBackgroundResource(R.drawable.bar_top_tips);



        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.dismiss();
            }
        });

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setTextColor(mContext.getResources().getColorStateList(R.color.btn_right_selector));
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(false);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        customBuilder.setDisBottomButton(true);
        // 2线在下方
        Dialog = customBuilder.create();
        Dialog.show();
    }


    public static void showDialogCode(final Context mContext,  final Callback listener){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog3, null);
        TextView tv_title = view.findViewById(R.id.tv_alert_dialog_title);
        tv_title.setBackgroundResource(R.drawable.bar_top_tips);
        tv_title.setTextColor(mContext.getResources().getColor(com.tool.R.color.white));


        final EditText editText = view.findViewById(R.id.et_code_no);

        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.dismiss();
            }
        });

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setTextColor(mContext.getResources().getColorStateList(R.color.btn_right_selector));
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isBlank(editText.getText().toString().trim())){
                    ToastUtils.CustomShow(mContext, "请输入验证码");
                    return;
                }
                if (listener != null){
                    Dialog.dismiss();
                    listener.getData(editText.getText().toString().trim());
                }
            }
        });

        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(false);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        customBuilder.setDisBottomButton(true);
        // 2线在下方
        Dialog = customBuilder.create();
        Dialog.show();
    }



    /**
     * 显示消息 按钮为 确定 取消
     */
    public static void alert(String title, String hint, Context ctx, DialogInterface.OnClickListener positive,
                             DialogInterface.OnClickListener negative) {
        if (negative == null) {
            negative = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            };
        }
        alert(title, hint, ctx, "确定", "取消", positive, negative, null, true, true, 1);

    }



    // 最全的
    public static void alert(String title, String hint, Context ctx, String positiveText, String negativeText,
                             DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative, View view, boolean a,
                             boolean b, int line) {

        if (Dialog != null) {
            Dialog.dismiss();
            Dialog = null;
        }

        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(ctx, com.tool.R.style.MyDialogStyleBottom);
        builder.setTitle(title);// 消息内容
        builder.setMessage(hint);// 提示补充
        builder.setContentView(view);// 自定义布局
        builder.setCancelable(a);
        builder.setCanceledOnTouchOutside(b);
        builder.setLine(line);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方 2线在下方

        builder.setPositiveButton(positiveText, positive);
        builder.setNegativeButton(negativeText, negative);
        Dialog = builder.create();

        Dialog.show();

    }




    public static boolean checkApproveStatus(final Context mContext){
        //判断是否实名认证
        int approve_status = UtilPreference.getIntValue(mContext, "approve_status");
        if (approve_status == 0){
            showRegisterSuccess(mContext);
            return true;
        }
        return false;
    }


    private static MyCustomDialog myCustomDialog;

    public static boolean checkGradeStatus(final Context mContext){
        int isGrade = UtilPreference.getIntValue(mContext, "isGrade");

        if (isGrade == 0){
            myCustomDialog = showGradeDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myCustomDialog.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myCustomDialog.dismiss();
                    mContext.startActivity(new Intent(mContext, ActivityFyPay.class));
                }
            });
            return false;
        }

//        myCustomDialog = showGradeDialog(mContext, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myCustomDialog.dismiss();
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myCustomDialog.dismiss();
//                mContext.startActivity(new Intent(mContext, ActivityFyPay.class));
//
//            }
//        });
        return true;
    }

    private static MyCustomDialog showGradeDialog(Context mContext, View.OnClickListener leftClick, View.OnClickListener rightClick) {

        return showDialog2(mContext, "温馨提示", "取消", "确定", "您还未成为沃富正式会员是否支付199元成为会员", null, leftClick, rightClick);
    }


    private static void showRegisterSuccess(final Context mContext){

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_register_success, null);
        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
                R.style.MyDialogStyleBottom);
        customBuilder.setCancelable(true);
        customBuilder.setCanceledOnTouchOutside(false);
        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
        customBuilder.setContentView(view);
        // 2线在下方
        customBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                dialog.dismiss();
                mContext.startActivity(new Intent(mContext, ActivitySmrz.class));
            }
        });
        customBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                dialog.dismiss();
//                finish();
            }
        });
        final MyCustomDialog dialog = customBuilder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //直接退出应用
                dialog.dismiss();
//                finish();
            }
        });
        dialog.show();
    }
}
