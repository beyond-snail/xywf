package com.tool.utils.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理类
 *
 * @author yang
 * @time 2017-3-15 下午6:01:12
 */
public class ActivityTempManager {
    private static ActivityTempManager sActivityManager;
    private List<Activity> mMains = new ArrayList<Activity>();// 主列表
    private List<Activity> mTemps = new ArrayList<Activity>();// 临时列表,用于结束多个Activity

    private ActivityTempManager() {

    }

    public static ActivityTempManager getInstance() {
        if (sActivityManager == null) {
            sActivityManager = new ActivityTempManager();
        }
        return sActivityManager;
    }

    /**
     * 添加Activity
     *
     * @param activity Activity实例
     */
    public void addAtivity(Activity activity) {
        mMains.add(activity);
    }

    /**
     * 移除Activity
     *
     * @param activity Activity实例
     */
    public void removeAtivity(Activity activity) {
        mMains.remove(activity);
    }

    /**
     * 添加临时Activity
     *
     * @param activity 临时Activity实例
     */
    public void addTemp(Activity activity) {
        mTemps.add(activity);
    }

    /**
     * 添加临时Activity
     *
     * @param activity 临时Activity实例
     */
    public void removeTemp(Activity activity) {
        mTemps.remove(activity);
    }

    /**
     * 结束全部AppCompatActivity
     */
    public void finishAllActivity() {
        for (int i = 0; i < mMains.size(); i++) {
            Activity activity = mMains.get(i);
            if (activity != null) {
                activity.finish();
            }
        }
        mMains.clear();
        mTemps.clear();
    }

    /**
     * 结束全部临时AppCompatActivity
     */
    public void finishAllTemp() {
        for (int i = 0; i < mTemps.size(); i++) {
            Activity activity = mTemps.get(i);
            if (activity != null) {
                activity.finish();
            }
        }
        mTemps.clear();
    }

}
