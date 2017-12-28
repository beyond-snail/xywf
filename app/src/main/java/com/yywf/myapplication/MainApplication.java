package com.yywf.myapplication;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.moxie.client.manager.MoxieSDK;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;

import java.util.Iterator;
import java.util.List;


public class MainApplication extends MultiDexApplication {

    public static Context applicationContext;
    private static MainApplication instance;

    // login user name
    public static final String PREF_USERNAME = "chat_username";
    private String userName = null;
    // login password
    private static final String PREF_PWD = "chat_pwd";
    private String password = null;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        applicationContext = this;

        instance = this;
        // CrashHandler.getInstance().init(this);// 初始化全局异常管理
        /*
		 * Bugly SDK初始化 参数1：上下文对象 参数2：APPID，平台注册时得到,注意替换成你的appId
		 * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
		 */
//        CrashReport
//                .initCrashReport(getApplicationContext(), "3fe07ad6ef", true);
        // 语音服务入口 讯飞
//        SpeechUtility.createUtility(MainApplication.this, "appid=59102b03");// 云流车惠59102b03
        // 548661f2，云流车惠_android59708324

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        // 如果没有，进入地图会跳出来
//        SDKInitializer.initialize(this);

        MoxieSDK.init(this);

        // 图片加载工具初始化
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_public_nophoto)
                .showImageOnFail(R.mipmap.ic_public_nophoto)
                .imageScaleType(ImageScaleType.EXACTLY)
                // IN_SAMPLE_INT
                .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
                .cacheInMemory(true).cacheOnDisc(true).build();
        // 图片加载工具配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)
                //
                .discCacheFileCount(100)
                // 缓存一百张图片
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                // .memoryCacheSize(1 * 1024 * 1024)
                // .memoryCacheExtraOptions(400, 300)
                .memoryCacheSizePercentage(13)
                // .memoryCache(new WeakMemoryCache())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
        if (processAppName == null || processAppName.equals("")) {
            // workaround for baidu location sdk
            // 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
            // 创建新的进程。
            // 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
            // processName，
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        // 初始化环信SDK,一定要先调用init()
//        Log.d("EMChat Demo", "initialize EMChat SDK");
//        EMChat.getInstance().init(applicationContext);
//        EMChatManager.getInstance().setMipushConfig(AppConstants.APP_ID_XiaoMi, AppConstants.APP_KEY_XiaoMi);
//        // debugmode设为true后，就能看到sdk打印的log了
//        EMChat.getInstance().setDebugMode(true);
//
//        // 获取到EMChatOptions对象
//        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(true);
//        // 设置收到消息是否有新消息通知，默认为true
//        options.setNotificationEnable(false);
//        // 设置收到消息是否有声音提示，默认为true
//        options.setNoticeBySound(true);
//        // 设置收到消息是否震动 默认为true
//        options.setNoticedByVibrate(true);
//        // 设置语音消息播放是否设置为扬声器播放 默认为true
//        options.setUseSpeaker(true);
//
//        // 设置应用后台运行时不执行此提醒
//        options.setShowNotificationInBackgroud(false);
//
//        // 设置一个connectionlistener监听账户重复登陆
//        // EMChatManager.getInstance().addConnectionListener(new
//        // MyConnectionListener());
//
//        initXiaoMiPush();
    }


    /**
     * 注册初始化小米推送
     *
     * @param
     * @return
     */
//    private void initXiaoMiPush() {
////        if (shouldInit()) {
//            MiPushClient.registerPush(this, AppConstants.APP_ID_XiaoMi,
//                    AppConstants.APP_KEY_XiaoMi);
////        }
//    }
//
//    private boolean shouldInit() {
//        ActivityManager am = ((ActivityManager)
//                getSystemService(Context.ACTIVITY_SERVICE));
//        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
//        String mainProcessName = getPackageName();
//        int myPid = android.os.Process.myPid();
//        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
//            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static MainApplication getInstance(Context appContext) {
        return instance;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        if (userName == null) {
            userName = UtilPreference.getStringValue(applicationContext,
                    PREF_USERNAME);
        }
        return userName;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        if (password == null) {
            password = UtilPreference.getStringValue(applicationContext,
                    PREF_PWD);
        }
        return password;
    }

    /**
     * 设置用户名
     *
     * @param username
     */
    public void setUserName(String username) {
        if (username != null) {
            UtilPreference.saveString(applicationContext, PREF_USERNAME,
                    username);
            userName = username;
        }
    }

    /**
     * 设置密码
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        UtilPreference.saveString(applicationContext, PREF_PWD, pwd);
        password = pwd;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        @SuppressWarnings("rawtypes")
        List l = am.getRunningAppProcesses();
        @SuppressWarnings("rawtypes")
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    @SuppressWarnings("unused")
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +" Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 退出登录,清空数据
     */
    public void logout() {
        // 先调用sdk logout，在清理app中自己的数据
//        if (EMChatManager.getInstance().isConnected()) {
//            EMChatManager.getInstance().logout();
//        }

        // DbOpenHelper.getInstance(applicationContext).closeDB();
        // reset password to null
        setPassword(null);

    }

    public static Context getContext() {
        return context;
    }
}
