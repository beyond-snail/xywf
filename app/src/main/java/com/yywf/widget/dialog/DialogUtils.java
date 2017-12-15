package com.yywf.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yywf.R;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class DialogUtils {


    public static MyCustomDialog showDialog(final Context mContext, String title, String content, View.OnClickListener leftClick, View.OnClickListener rightClick){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog, null);
        TextView tv_title = view.findViewById(R.id.tv_alert_dialog_title);
        tv_title.setText(title);
        tv_title.setBackgroundResource(R.drawable.bar_top_tips);
        tv_title.setTextColor(mContext.getResources().getColor(com.tool.R.color.white));

        TextView tv_content = view.findViewById(R.id.tv_alert_dialog_message);
        tv_content.setText(content);

        TextView btn_left = view.findViewById(R.id.btn_alert_dialog_btn_left);
        btn_left.setText("取消");
        btn_left.setTextColor(mContext.getResources().getColorStateList(R.color.btn_left_selector));
        btn_left.setOnClickListener(leftClick);

        TextView btn_right = view.findViewById(R.id.btn_alert_dialog_btn_right);
        btn_right.setText("确定");
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
}
