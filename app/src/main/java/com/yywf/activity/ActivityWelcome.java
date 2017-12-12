package com.yywf.activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tool.utils.http.HttpUtilKey;
import com.tool.utils.utils.NetUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.util.GuideImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ActivityWelcome extends BaseActivity {

	private ImageView ivWelcome;
	private Animation anim = null;
	private Context mContext;
	private int sizeType = 3, width = 0, height = 0;
	private float scale = 0.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_welcome);
		
		ImageLoader.getInstance().clearDiscCache();
        ImageLoader.getInstance().clearMemoryCache();
		sizeType = 4;


		ivWelcome = (ImageView) findViewById(R.id.ivWelcome);
		anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(1000);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// do nothing
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// do nothing
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (!NetUtils.isConnected(ActivityWelcome.this)) {
					showErrorMsg("请连接网络后重新打开应用程序！");
				}

				try {
					// 检测基本数据 userid appsid 用户密码本地有 直接登录
					doRedirect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ivWelcome.startAnimation(anim);

	}

	/**
	 * 从服务器加载图片路径
	 */
	private void loadImgFromServer() {
		String url = ConfigXy.XY_LOAD_SERVER_IMG;
		RequestParams params = new RequestParams();
		params.add("type_id", "1");// 1:启动页
//		params.add("project_name", ConfigApp.HC_APPNAME);
		HttpUtil.get(url, params, new HttpUtil.RequestListener() {

			@Override
			public void success(String response) {
				try {
					String imgpath = "";
					JSONArray rows = (new JSONObject(response)).getJSONArray("rows");
					for (int i = 0; i < rows.length(); i++) {
						JSONObject json = rows.getJSONObject(i);
						// 巡查查找跟屏幕匹配的图片
						if (json.getInt("SIZE_TYPE") == sizeType) {
							imgpath = json.getString("URL");
						}

						// 如果图片尺寸跟手机屏幕大小没有匹配的，默认最后一张图片
						if (StringUtils.isBlank(imgpath)) {
							imgpath = rows.getJSONObject(rows.length() - 1).getString("URL");
						}
					}
					imgpath =imgpath;
					Log.i("ActivityWelcome", "欢迎页图片地址：" + imgpath);
					loadImgAndShow(imgpath);

				} catch (JSONException e) {
					loadImgFail();
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable error) {
				loadImgFail();
				showErrorMsg("网络异常，请重新再试！");
			}
		});
	}

	/**
	 * 加载欢迎图并显示
	 * 
	 * @param path
	 */
	private void loadImgAndShow(String path) {
		ImageSize size = new ImageSize(width, height);
		ImageLoader.getInstance().loadImage(path, size, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {

			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				loadImgFail();
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bp) {
				ivWelcome.setImageBitmap(bp);
				ivWelcome.startAnimation(anim);
				GuideImageUtil.getInstance(mContext, sizeType).loadDataFromServer();
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {

			}
		});
	}

	/**
	 * 加载图片失败时显示本地图片
	 */
	private void loadImgFail() {
		ivWelcome.setImageResource(R.drawable.welcome_bg);
		ivWelcome.startAnimation(anim);
	}

	/**
	 * 页面跳转
	 */
	private void doRedirect() {

		//判断是否是第一次启动app
		//如果第一次启动，则先进入功能引导页
		boolean isFirstOpen = UtilPreference.getBoolean(mContext, UtilPreference.FIRST_OPEN, true);
		if (isFirstOpen){
			startActivity(new Intent(ActivityWelcome.this, ActivityGuide.class));
			UtilPreference.putBoolean(mContext, UtilPreference.FIRST_OPEN, false);
			finish();
			return;
		}


		if (isHasLogin()) {
		    // 跳到登录页
            startActivity(new Intent(ActivityWelcome.this, ActivityHome.class));
            finish();
		} else {
			// 跳到登录页
			startActivity(new Intent(ActivityWelcome.this, ActivityLogin.class));
			finish();
		}
	}

	/**
	 * 该账号是否已经登录过
	 * 
	 * @return
	 */
	private boolean isHasLogin() {
		String userId = UtilPreference.getStringValue(mContext, "userId");
		String userName = UtilPreference.getStringValue(mContext, "userName");
		// 聊天服务器标识 
//		String channelId = UtilPreference.getStringValue(mContext, "channelId");
		String appSid =  UtilPreference.getStringValue(mContext, "appSid");
        HttpUtilKey.appSid = appSid;

		if (StringUtils.isBlank(userId)) {
			return false;
		}
		if (StringUtils.isBlank(userName)) {
			return false;
		}
		if (StringUtils.isBlank(appSid)) {
			return false;
		}
		return true;
	}

	/**
	 * 进入无线网络配置界面
	 */
	private void showSetNetworkDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("设置网络");
		builder.setMessage("网络连接不可用，请检查网络状态");
		builder.setPositiveButton("设置网络", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));// 进入无线网络配置界面
				finish();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.create().show();
	}

}
