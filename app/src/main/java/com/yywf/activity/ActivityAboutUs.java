package com.yywf.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.AdInfo;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.ADCommonView;

import org.json.JSONObject;

import java.util.List;

public class ActivityAboutUs extends BaseActivity  {

	private final String TAG = "ActivityAboutUs";
	private TextView tv_web;
	private TextView tv_phone;
	private TextView tv_wxgzh;
	private ImageView img_icon;
	private TextView tv_current_version;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_aboutus);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("关于我们");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		try {
			initView();
		} catch (Exception e) {
			e.printStackTrace();
		}

		getAboutUs();
	}

	private void getAboutUs() {
		HttpUtil.get(ConfigXy.ZF_GET_ABOUT_US, requestListener);
	}


	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {

				JSONObject result = new JSONObject(response);

				//判断登录是否失效
				if (result.optInt("code") == -2){
					UtilPreference.clearNotKeyValues(mContext);
					// 退出账号 返回到登录页面
					MyActivityManager.getInstance().logout(mContext);
					return;
				}

				if (!result.optBoolean("status")) {
					showErrorMsg(result.getString("message"));
					return;
				}

				JSONObject dataStr = result.getJSONObject("data");
				String icon = dataStr.optString("icon");
				String name = dataStr.optString("name");
				String web_site = dataStr.optString("web_site");
				String service_phone = dataStr.optString("service_phone");

                if (!StringUtils.isBlank(web_site)) {
                    tv_web.setText(web_site);
                }
                if (!StringUtils.isBlank(service_phone)) {
                    tv_phone.setText(service_phone);
                }
                if (!StringUtils.isBlank(name)){
                    tv_wxgzh.setText(name);
                }
				if (!StringUtils.isBlank(icon)){
					ImageLoader.getInstance().displayImage(icon, img_icon);
				}



			} catch (Exception e) {
				Log.e(TAG, "doLogin() Exception: " + e.getMessage());

			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};


	private void initView() throws Exception {
		img_icon = imageView(R.id.imageView1);
		tv_web = textView(R.id.tv_web);
		tv_phone = textView(R.id.tv_phone);
		tv_wxgzh = textView(R.id.tv_wxgzh);
		tv_current_version = textView(R.id.tv_current_version);

        //设置默认值
        tv_web.setText("www.xyk11.com");
        tv_phone.setText("0571-8858 6888");
        tv_wxgzh.setText("信易沃富");

		String appname = getResources().getString(R.string.app_name);
		tv_current_version.setText(appname+" V" + getVersionName());

	}


	/**
	 * 获取版本名称
	 *
	 * @return
	 * @throws Exception
	 */
	private String getVersionName() throws Exception {
		return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}






}
