package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StatusBarUtil;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.fragment.FragmentHomePage;
import com.yywf.fragment.FragmentMine;
import com.yywf.fragment.FragmentWhd;
import com.yywf.fragmenttab.FragmentTab;
import com.yywf.fragmenttab.TabItemImpl;
import com.yywf.util.MyActivityManager;

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

    }






    private void initView() {
        findViewById(R.id.rl_home_1).setOnClickListener(this);
        findViewById(R.id.rl_home_2).setOnClickListener(this);
        findViewById(R.id.rl_home_3).setOnClickListener(this);

        //
        mFragmentTab = new FragmentTab(getSupportFragmentManager());
        mFragmentTab.addTabItem(new TabItemImpl<FragmentHomePage>(mContext,
                "main", FragmentHomePage.class));
        mFragmentTab.addTabItem(new TabItemImpl<FragmentWhd>(mContext,
                "whd", FragmentWhd.class));
        mFragmentTab.addTabItem(new TabItemImpl<FragmentMine>(mContext,
                "mine", FragmentMine.class));

        mFragmentTab.selectTab("main");
        doSelect(0);
    }

    /**
     * tab页点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home_1://
                mFragmentTab.selectTab("main");
                doSelect(0);
                break;
            case R.id.rl_home_2://
                mFragmentTab.selectTab("whd");
                doSelect(1);
                break;
            case R.id.rl_home_3://
                mFragmentTab.selectTab("mine");
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
