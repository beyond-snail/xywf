package com.yywf.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tool.utils.utils.StatusBarUtil;
import com.tool.utils.utils.ToastUtils;
import com.views.popwindow.bean.BaseFilter;
import com.views.popwindow.pop.CommonFilterPop;
import com.yywf.R;
import com.yywf.util.MyActivityManager;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public abstract class BaseActivity extends Activity {

    private static final String TAG = "BaseActivity";


    protected final static int DATA_LOAD_ING = 0x001;
    protected final static int DATA_LOAD_COMPLETE = 0x002;
    protected final static int DATA_LOAD_FAIL = 0x003;

    public static final Handler handler = new Handler();

    protected CallbackLister callbacklister;

    public interface CallbackLister{
        void show();
    }

    /**
     * 上下文 当进入activity时必须 mContext = this 方可使用，否则会报空指针
     */
    public Context mContext;

    /**
     * 加载等待效果
     */
    public ProgressDialog progress;
    // public AlertDialog progress;

    /**
     * 筛选pop
     */
    private CommonFilterPop mPopupWindow;


    /**
     * 初始化创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
//        StatusBarUtil.setTranslucent((Activity)mContext, 12);
        StatusBarUtil.setTranslucent((Activity)mContext);
    }

    /**
     * 重回前台显示调用
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Activity销毁，关闭加载效果
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Activity暂停，关闭加载效果
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Activity停止，关闭加载效果
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 初始化标题和回退
     */
    public void initTitle(String title) {
        if (textView(R.id.tv_header) != null) {
            textView(R.id.tv_header).setText(title);
        }
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 触发手机返回按钮
     */
    @Override
    public void onBackPressed() {
        MyActivityManager.getInstance().removeActivity(BaseActivity.this);
    }

    /**
     * 滚动条超时
     *
     * @author mo
     *
     */
    @SuppressWarnings("unused")
    private class ProgressTimeOut extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(30000);
                Message message1 = new Message();
                BaseActivity.this.disProgressHander.sendMessage(message1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 消息队列方式显示进度
     */
    @SuppressLint("HandlerLeak")
    public Handler progressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
            progress = new ProgressDialog(BaseActivity.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage(msg.obj.toString());
            progress.setCancelable(false);
            progress.show();
            View layout = View.inflate(mContext, R.layout.layout_dialog, null);
            TextView tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
            if (!"".equals(msg.obj.toString())) {
                tv_msg.setText("  " + msg.obj.toString());
            }
            progress.getWindow().setContentView(layout);// show()代码后
        }
    };

    /**
     * 隐藏消息队列进度的显示
     */
    public Handler disProgressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
        }
    };

    /**
     * 显示字符串消息
     *
     * @param message
     */
    public void showProgress(String message) {
        if (progress != null) {
            progress.dismiss();
        }
        progress = new ProgressDialog(BaseActivity.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(message);
//		progress.setCancelable(false);
        progress.show();
        View layout = View.inflate(mContext, R.layout.layout_dialog, null);
        TextView tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
        if (!"".equals(message)) {
            tv_msg.setText("  " + message);
        }
        progress.getWindow().setContentView(layout);// show()代码后

    }

    /**
     * 隐藏字符串消息
     */
    public void disShowProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 提示信息
     *
     */
    public void showErrorMsg(String message) {
        final String str = message;
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息号或请求失败信息
     *
     * showErrorRequestFair
     *
     */
    public void showE404() {
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), "手机信号差或服务器维护，请稍候重试。谢谢！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息
     *
     */
    public void showMsgAndDisProgress(String message) {
        final String str = message;
        BaseActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                disShowProgress();
            }
        });
    }

    /**
     * 文本View
     */
    public TextView textView(int textview) {
        return (TextView) findViewById(textview);
    }

    protected void setTvText(int vid, String text) {
        TextView view = (TextView) findViewById(vid);
        if(view!=null){view.setText(TextUtils.isEmpty(text) ? "": text);}
    }

    protected void showView(int vid, boolean isShow){
        View view = (View) findViewById(vid);
        if(view!=null){
            view.setVisibility(isShow?View.VISIBLE:View.GONE);
        }
    }

    protected void showLayoutView(int vid, boolean isShow){
        LinearLayout view = (LinearLayout) findViewById(vid);
        if(view!=null){
            view.setVisibility(isShow?View.VISIBLE:View.GONE);
        }
    }

    /**
     * 文本button
     */
    public Button button(int id) {
        return (Button) findViewById(id);
    }

    /**
     * 文本button
     */
    public ImageView imageView(int id) {
        return (ImageView) findViewById(id);
    }

    /**
     * 文本editText
     */
    public EditText editText(int id) {
        return (EditText) findViewById(id);
    }

    public LinearLayout linearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }
    public RelativeLayout relativeLayout(int id) {
        return (RelativeLayout) findViewById(id);
    }

    /**
     * 显示数据加载状态
     *
     * @param loading
     * @param
     * @param datas
     * @param type
     */
    public void viewSwitch(View loading, View datas, int type) {
        switch (type) {
            case DATA_LOAD_ING:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_COMPLETE:
                datas.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                break;
            case DATA_LOAD_FAIL:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
        }
    }


    /**
     * 列表选择popupWindow
     *
     * @param parentView        父View
     * @param itemTexts         列表项文本集合
     * @param itemClickListener 列表项点击事件
     */
    public void showFilterPopupWindow(View parentView,
                                      List<String> itemTexts,
                                      AdapterView.OnItemClickListener itemClickListener,
                                      CustomerDismissListener dismissListener) {
        showFilterPopupWindow(parentView, itemTexts, itemClickListener, dismissListener, 0);
    }

    /**
     * 列表选择popupWindow
     *
     * @param parentView        父View
     * @param itemTexts         列表项文本集合
     * @param itemClickListener 列表项点击事件
     */
    public void showFilterPopupWindow(View parentView,
                                      List<String> itemTexts,
                                      AdapterView.OnItemClickListener itemClickListener,
                                      CustomerDismissListener dismissListener, float alpha) {

        // 判断当前是否显示
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
        mPopupWindow = new CommonFilterPop(mContext, itemTexts);
        mPopupWindow.setOnDismissListener(dismissListener);
        // 绑定筛选点击事件
        mPopupWindow.setOnItemSelectedListener(itemClickListener);
        // 如果透明度设置为0的话,则默认设置为0.6f
        if (0 == alpha) {
            alpha = 0.6f;
        }
        // 设置背景透明度
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
        // 显示pop
        mPopupWindow.showAsDropDown(parentView);

    }

    /**
     * Tab筛选栏切换
     *
     * @param isChecked         选中状态
     * @param showView          展示pop的跟布局
     * @param showMes           展示选择的数据
     * @param itemClickListener 点击回调
     * @param tabs              所有的cb(需要几个输入几个就可以,cb1,cb2....)
     */
    public void filterTabToggle(boolean isChecked, View showView, List<String> showMes, AdapterView.OnItemClickListener itemClickListener, final CheckBox... tabs) {
        if (isChecked) {
            if (tabs.length <= 0) {
                return;
            }
            // 第一个checkBox为当前点击选中的cb,其他cb进行setChecked(false);
            for (int i = 1; i < tabs.length; i++) {
                tabs[i].setChecked(false);
            }

            showFilterPopupWindow(showView, showMes, itemClickListener, new CustomerDismissListener() {
                @Override
                public void onDismiss() {
                    super.onDismiss();
                    // 当pop消失时对第一个cb进行.setChecked(false)操作
                    tabs[0].setChecked(false);
                }
            });
        } else {
            // 关闭checkBox时直接隐藏popuwindow
            hidePopListView();
        }
    }

    /**
     * Tab筛选栏切换
     *
     * @param isChecked         选中状态
     * @param showView          展示pop的跟布局
     * @param showMes           展示选择的数据源
     * @param itemClickListener 点击回调
     * @param tabs              所有的cb(需要几个输入几个就可以,cb1,cb2....)
     */
    public void filterTabToggleT(boolean isChecked, View showView, List<? extends BaseFilter> showMes, AdapterView.OnItemClickListener itemClickListener, final CheckBox... tabs) {
        if (isChecked) {
            if (tabs.length <= 0) {
                return;
            }
            // 第一个checkBox为当前点击选中的cb,其他cb进行setChecked(false);
            for (int i = 1; i < tabs.length; i++) {
                tabs[i].setChecked(false);
            }
            // 从数据源中提取出展示的筛选条件
            List<String> showStr = new ArrayList<String>();
            for (BaseFilter baseFilter : showMes) {
                showStr.add(baseFilter.getFilterStr());
            }
            showFilterPopupWindow(showView, showStr, itemClickListener, new CustomerDismissListener() {
                @Override
                public void onDismiss() {
                    super.onDismiss();
                    // 当pop消失时对第一个cb进行.setChecked(false)操作
                    tabs[0].setChecked(false);
                }
            });
        } else {
            // 关闭checkBox时直接隐藏popuwindow
            hidePopListView();
        }
    }

    /**
     * 自定义OnDismissListener
     */
    public class CustomerDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            // 当pop消失的时候,重置背景色透明度
            WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
            lp.alpha = 1.0f;
            ((Activity)mContext).getWindow().setAttributes(lp);
        }
    }

    /**
     * 隐藏pop
     */
    public void hidePopListView() {
        // 判断当前是否显示,如果显示则dismiss
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }



    public void checkPermission() {
//        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储", R.drawable.permission_ic_storage));
//        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "定位", R.drawable.permission_ic_location));
        HiPermission.create(mContext)
//                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.i(TAG, "onClose");
                        ToastUtils.showShort(mContext, "用户关闭权限申请");
                    }

                    @Override
                    public void onFinish() {
//						ToastUtils.showShort(mContext, "所有权限申请完成");
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                        Log.i(TAG, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                        Log.i(TAG, "onGuarantee");
                    }
                });
    }
}
