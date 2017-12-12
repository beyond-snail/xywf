package com.tool.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tool.utils.http.HttpUtilKey;

import org.json.JSONObject;


/**
 * SharedPreferece 工具类
 */
public class UtilPreference {

    /**
     * SharedPreference名称
     */
    private static final String PREFERENCE_FILE_NAME = "dock_preferences";

    /**
     * 换个名称保存导航的历史记录、收藏地址、家和公司地址，不至于退出帐号就清除了
     */
    private static final String PREFERENCE_NAVI_NAME = "navi_preferences";

    private static final String PREFERENCE_SOCIAL_NAME = "social_preferences";


    private static final String spFileName = "welcomePage";
    public static final String FIRST_OPEN = "first_open";


    public static Boolean getBoolean(Context context, String strKey,
                                     Boolean strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }

    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }

    /**
     * 添加String串到SharedPreference中
     *
     * @param context Context
     * @param key     键
     * @param value   值
     */
    public static void saveString(final Context context, final String key,
                                  final String value) {
        if (context == null) {
            return;
        }
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveString(String PREFERENCE_NAME,
                                  final Context context, final String key, final String value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 添加String到导航的SharedPreference中用PREFERENCE_NAVI_NAME
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveNaviString(final Context context, final String key,
                                      final String value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_NAVI_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 清除本地所有数据
     *
     * @param context
     */
    public static void clearLocalValues(final Context context) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清除除用户名、密码数据之外的所有本地数据
     *
     * @param context
     */
    public static void clearNotKeyValues(final Context context) {
        String userName = getStringValue(context, "userName");
        String password = getStringValue(context, "password");
        String login_history = getStringValue(context, "login_history");
        String appSid = getStringValue(context, "appSid");
        String deviceNo = getStringValue(context, "deviceNo");
        String wifi = getStringValue(context, deviceNo + "_WIFI");

        clearLocalValues(context);

        saveString(context, "userName", userName);
        saveString(context, "password", password);
        saveString(context, "login_history", login_history);
        saveString(context, "appSid", appSid);
        saveString(context, deviceNo + "_WIFI", wifi);

    }

    /**
     * 清除除用户名、密码数据之外的所有本地数据
     *
     * @param context
     */
    public static void clearAppSid(final Context context) {
        saveString(context, "appSid", "");
    }

    /**
     * 添加int到SharedPreference中
     *
     * @param context Context
     * @param key     键
     * @param value   值
     */
    public static void saveInt(final Context context, String key, int value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 添加int到SharedPreference中
     *
     * @param context Context
     * @param key     键
     * @param value   值
     */
    public static void saveLong(final Context context, String key, long value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 添加boolean到SharedPreference中
     *
     * @param context Context
     * @param key     键
     * @param value   值
     */
    public static void saveBoolean(final Context context, String key,
                                   Boolean value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返回Null
     */
    public static String getStringValue(final Context context, final String key) {
        if (context == null)
            return null;
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getString(key, null);
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返回Null
     */
    public static String getStringValue(String PREFERENCE_NAME,
                                        final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preference.getString(key, null);
    }

    /**
     * 获取导航的数据用PREFERENCE_NAVI_NAME
     *
     * @param context
     * @param key
     * @return
     */
    public static String getNaviStringValue(final Context context,
                                            final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_NAVI_NAME, Context.MODE_PRIVATE);
        return preference.getString(key, null);
    }

    /**
     * 获取Boolean
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返回false
     */
    public static boolean getBooleanValue(final Context context,
                                          final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getBoolean(key, false);
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返回-1
     */
    public static int getIntValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getInt(key, -1);
    }

    public static long getLongValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getLong(key, 0L);
    }

    /**
     * 获取String
     *
     * @param context
     * @param key          名称
     * @param defaultValue
     * @return 键对应的值，如果找不到对应的值， 则返回默认值
     */
    public static int getIntValue(final Context context, final String key,
                                  int defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getInt(key, defaultValue);
    }

    /**
     * 保存车辆信息
     *
     * @param context
     * @param carId
     * @param chePai
     * @param deviceNo
     * @param isBandObd
     */
    public static void saveCarInfo(final Context context, String carId,
                                   String chePai, String deviceNo, String logoPath, String categoryId,
                                   String cpId, String cxgId) {
        // 切换车辆 那些数据要清空 需整理 首页车辆resume 都更新车辆列表 可放置oncreate 但是凡是涉及车辆修改 都需要更新本地车数据
        // saveString(context, "latLng.latitude", "");
        // saveString(context, "latLng.longitude", "");

        saveString(context, "carId", carId);
        saveString(context, "chePai", chePai);
        saveString(context, "deviceNo", deviceNo);
        saveString(context, "logoPath", logoPath);
        // 是否绑定obd 废除
        // saveString(context, "isBandObd", isBandObd);
        saveString(context, "categoryId", categoryId);
        saveString(context, "chePinPai", cpId);
        saveString(context, "cxgId", cxgId);// 车型id

    }

    /**
     * 是否点赞过
     *
     * @param context
     * @param contentId
     */
    public static boolean isGood(final Context context, String contentId) {
        String name = getStringValue(context, "userName") + "_good";

        String goodsp = getStringValue(PREFERENCE_SOCIAL_NAME, context, name);
        if (!StringUtils.isBlank(goodsp)) {
            String[] goods = goodsp.split(":");
            for (String s : goods) {
                if (contentId.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 保存点赞记录
     *
     * @param context
     * @param contentId
     */
    public static void saveGood(final Context context, String contentId) {
        String name = getStringValue(context, "userName") + "_good";

        String goodsp = getStringValue(PREFERENCE_SOCIAL_NAME, context, name);
        if (StringUtils.isBlank(goodsp)) {
            saveString(PREFERENCE_SOCIAL_NAME, context, name, contentId);
        } else {
            goodsp = goodsp + ":" + contentId;
            saveString(PREFERENCE_SOCIAL_NAME, context, name, goodsp);
        }

    }

    public static void saveLoginInfo(Context mContext, String username,
                                     String password, JSONObject data) {
        try {
            // 缓存所属部门基本信息
            if (data.has("org")) {
                JSONObject org = data.getJSONObject("org");
                saveString(mContext, "orgId", org.optString("id", ""));
                // 门店（部门）ID
                saveString(mContext, "ORG_ID", org.getString("id"));
                saveString(mContext, "OrgLatitude",
                        org.optDouble("latitude", 0) + "");
                saveString(mContext, "OrgLongitude",
                        org.optDouble("longitude", 0) + "");
            }

            saveString(mContext, "channelId", data.getString("huanxin"));
            // 登录用户的昵称
            saveString(mContext, "nickName", data.optString("name", ""));
            if (!StringUtils.isBlank(username))
                saveString(mContext, "userName", username);
            if (!StringUtils.isBlank(password))
                saveString(mContext, "password", password);
            saveString(mContext, "appSid", data.optString("appSid", ""));
            HttpUtilKey.appSid = data.optString("appSid", "");
            saveString(mContext, "phone", data.optString("phone", ""));
            saveString(mContext, "userId", data.optString("id", ""));
            saveString(mContext, "memberId", data.optString("id", ""));

            saveInt(mContext, "sInteract", data.optInt("sInteract", 0));
            saveInt(mContext, "sAlert", data.optInt("sAlert", 0));
            saveInt(mContext, "sEmergency", data.optInt("sEmergency", 0));
            saveInt(mContext, "sRepair", data.optInt("sRepair", 0));

            saveString(mContext, "imgPath", data.optString("imgPath", ""));
        } catch (Exception e) {
        }
    }

    /**
     * 缓存一条最新的推送消息类型和消息内容
     *
     * @param context
     * @param type
     * @param message
     */
    public static void cacheNewestMessage(Context context, String type, String message) {
        saveString(context, "newestType", type);
        saveString(context, "newestDate", StringUtils.getCurrentDate("yyyy-MM-dd HH:mm"));
        saveString(context, "newestMsg", message);
    }

    /**
     * 获取缓存最新推送消息的消息类型
     *
     * @param context
     * @return
     */
    public static String getCacheNewestType(Context context) {
        return getStringValue(context, "newestType");
    }

    /**
     * 获取缓存最新推送消息的时间
     *
     * @param context
     * @return
     */
    public static String getCacheNewestDate(Context context) {
        return getStringValue(context, "newestDate");
    }

    /**
     * 获取缓存最新推送消息的消息内容
     *
     * @param context
     * @return
     */
    public static String getCacheNewestMessage(Context context) {
        return getStringValue(context, "newestMsg");
    }

    /**
     * 设备号
     *
     * @param context
     * @return
     */
    public static String getDeviceNo(Context context) {
        return getStringValue(context, "deviceNo");
    }

    /**
     * 车辆ID
     *
     * @param context
     * @return
     */
    public static String getCarId(Context context) {
        return getStringValue(context, "carId");
    }

    /**
     * 车牌
     *
     * @param context
     * @return
     */
    public static String getCarNum(Context context) {
        return getStringValue(context, "chePai");
    }

    /**
     * 用户ID
     *
     * @param context
     * @return
     */
    public static String getMemberId(Context context) {
        return getStringValue(context, "memberId");
    }
}
