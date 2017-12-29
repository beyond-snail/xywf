package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.business.Update;
import com.business.UpdateManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.GsonUtil;
import com.tool.utils.utils.StatusBarUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.fragment.FragmentHomePage;
import com.yywf.fragment.FragmentMine;
import com.yywf.fragment.FragmentWhd;
import com.yywf.fragmenttab.FragmentTab;
import com.yywf.fragmenttab.TabItemImpl;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityHome extends FragmentActivity implements OnClickListener{

    private static final String TAG = "ActivityHome";

    private Context mContext;
    private static final int[] tabIconSelector = new int[]{R.drawable.label_home_pre,
            R.drawable.label_weidaihuan_pre, R.drawable.label_mypage_pre};

    private static final int[] tabIcon = new int[]{R.drawable.label_home,
            R.drawable.label_weidaihuan, R.drawable.label_mypage};
    private static final int[] tabs = new int[]{R.id.tv_home_1,
            R.id.tv_home_2, R.id.tv_home_3};


    private FragmentTab mFragmentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_home);
        StatusBarUtil.setTranslucent((Activity)mContext);

        MyActivityManager.getInstance().addActivity(this);
        initView();
//
//        //判断是否实名认证
//        checkApproveStatus();
        DialogUtils.checkApproveStatus(mContext);


        //版本检测
        versionUpdate();



    }




    /**
     * 版本检测
     */
    private void versionUpdate() {
        UpdateManager.getUpdateManager().setCallBack(new UpdateManager.OnCheckFinished() {

            @Override
            public void onNeedToUpdate() {
                // 必须更新，否则退出
                finish();
            }
        });
        UpdateManager.getUpdateManager().checkAppUpdate(mContext, false, new UpdateManager.ListenerCallBack() {
            @Override
            public void doAction(final Handler handler) {
                String url = ConfigXy.XY_VERSION_CHECK;
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("types", ConfigXy.UPGRADE_CODE+"");
                HttpUtil.postJson(mContext, url, params, new HttpUtil.RequestListener() {

                    @Override
                    public void success(String response) {
                        try {
                            JSONObject result = new JSONObject(response);

                            String data = result.optString("result");

                            if (StringUtils.isBlank(data)){
                                return;
                            }

                            Update update = (Update) GsonUtil.getInstance().convertJsonStringToObject(data, Update.class);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = update;
                            handler.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable error) {
//				Log.e(TAG, error.getMessage());
                    }
                });
            }
        });
    }

//    public void showRegisterSuccess(){
//        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.dialog_register_success, null);
//        final MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(mContext,
//                R.style.MyDialogStyleBottom);
//        customBuilder.setCancelable(true);
//        customBuilder.setCanceledOnTouchOutside(false);
//        customBuilder.setLine(0);// 分割横线所处位置 在自定义布局上下或隐藏 0隐藏 1线在上方
//        customBuilder.setContentView(view);
//        // 2线在下方
//        customBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog,
//                                int which) {
//                dialog.dismiss();
//                startActivity(new Intent(mContext, ActivitySmrz.class));
//            }
//        });
//        customBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog,
//                                int which) {
//                dialog.dismiss();
////                finish();
//            }
//        });
//        final MyCustomDialog dialog = customBuilder.create();
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                //直接退出应用
//                dialog.dismiss();
////                finish();
//            }
//        });
//        dialog.show();
//    }





    private void initView() {
        findViewById(R.id.rl_home_1).setOnClickListener(this);
        findViewById(R.id.rl_home_2).setOnClickListener(this);
        findViewById(R.id.rl_home_3).setOnClickListener(this);

        //
        mFragmentTab = new FragmentTab(getSupportFragmentManager());
        mFragmentTab.addTabItem(new TabItemImpl<FragmentHomePage>(mContext,
                "FragmentHomePage", FragmentHomePage.class));
        mFragmentTab.addTabItem(new TabItemImpl<FragmentWhd>(mContext,
                "FragmentWhd", FragmentWhd.class));
        mFragmentTab.addTabItem(new TabItemImpl<FragmentMine>(mContext,
                "FragmentMine", FragmentMine.class));

        mFragmentTab.selectTab("FragmentHomePage");
        doSelect(0);
    }

    /**
     * tab页点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_1://
//                if (DialogUtils.checkApproveStatus(mContext)){
//                    return;
//                }
                mFragmentTab.selectTab("FragmentHomePage");
                doSelect(0);
                break;
            case R.id.rl_home_2://
                if (DialogUtils.checkApproveStatus(mContext)){
                    return;
                }
                if (!DialogUtils.checkGradeStatus(mContext)){
                    return;
                }
                mFragmentTab.selectTab("FragmentWhd");
                doSelect(1);
                break;
            case R.id.rl_home_3://
//                if (DialogUtils.checkApproveStatus(mContext)){
//                    return;
//                }
                mFragmentTab.selectTab("FragmentMine");
                doSelect(2);
                break;
            default:
                break;
        }
    }



    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    showErrorMsg("再按一次退出");
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    MyActivityManager.getInstance().moveTaskToBack(mContext);// 不退出，后台运行
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 一级菜单选中状态事件。
     */
    private void doSelect(int position) {
        Resources res = getResources();
        for (int i = 0; i < tabIcon.length; i++) {
            if (i == position) {
                Drawable selected = res.getDrawable(tabIconSelector[i]);
                selected.setBounds(0, 0, selected.getMinimumWidth(),
                        selected.getMinimumHeight());
                findTextViewById(tabs[i]).setCompoundDrawables(null, selected,
                        null, null);
                findTextViewById(tabs[i]).setTextColor(
                        res.getColor(R.color.text_font));
            } else {
                Drawable unselected = res.getDrawable(tabIcon[i]);
                unselected.setBounds(0, 0, unselected.getMinimumWidth(),
                        unselected.getMinimumHeight());
                findTextViewById(tabs[i]).setCompoundDrawables(null,
                        unselected, null, null);
                findTextViewById(tabs[i]).setTextColor(
                        res.getColor(R.color.gray_normal));
            }
        }
    }



    /**
     * 提示信息
     *
     * @param
     */
    public void showErrorMsg(String message) {
        final String str = message;
        BaseActivity.handler.post(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }


    /**
     * 获取TextView控件
     *
     * @param id
     * @return
     */
    private TextView findTextViewById(int id) {
        return (TextView) findViewById(id);
    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("test", "onNewIntent");


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("gg", "主页清除缓存");
        ImageLoader.getInstance().clearDiscCache();
        ImageLoader.getInstance().clearMemoryCache();
        try {
            finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
    }



    /**
     * 文本View
     */
    public TextView textView(int textview) {
        return (TextView) findViewById(textview);
    }






}
