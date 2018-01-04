package com.yywf.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.CreateCodeUtil;
import com.tool.utils.utils.QRCodeUtil;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.QbQrResult;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityMyQr extends BaseActivity implements OnClickListener {

	private static final String TAG = "ActivityMyQr";
	private ImageView twoImg;
	private TextView two_qr_name;
	private String checkQrStr;

	private String memberName;
	private String memberUrl;
	private String memberIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_myqr);
		MyActivityManager.getInstance().addActivity(this);



		initTitle("我的二维码");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();


//		twoCode("12333333");
		loadData();

	}



	private void loadData() {
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.get(ConfigXy.GET_QR, params, requestListener);

	}


	@SuppressLint("NewApi")
	private void initView() {

		twoImg = (ImageView) findViewById(R.id.two_qr);
		two_qr_name = findViewById(R.id.two_qr_name);


		imageView(R.id.img_right_add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (StringUtils.isBlank(memberUrl)){
					ToastUtils.showShort(mContext, "链接地址错误");
					return;
				}

				ShareEntity testBean = new ShareEntity("推荐人", memberName);
				testBean.setUrl(memberUrl); //分享链接
				testBean.setImgUrl(memberIcon);
				int channel = ShareConstant.SHARE_CHANNEL_WEIXIN_FRIEND | ShareConstant.SHARE_CHANNEL_WEIXIN_CIRCLE | ShareConstant.SHARE_CHANNEL_SINA_WEIBO | ShareConstant.SHARE_CHANNEL_QQ;
				ShareUtil.showShareDialog((Activity)mContext, channel,testBean, ShareConstant.REQUEST_CODE);
			}
		});
	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@SuppressLint("NewApi")
		@Override
		public void success(String response) {
			disShowProgress();

			try {
				JSONObject result = new JSONObject(response);
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

				if(!result.has("data")){
					ToastUtils.showShort(mContext, "没有二维码内容");
					return;
				}

				JSONObject dataStr = result.getJSONObject("data");

				memberUrl = dataStr.optString("invite_url");
				memberName = dataStr.optString("member_name");
				memberIcon = dataStr.optString("icon");


				if (!StringUtils.isEmpty(memberUrl)) {

					twoCode(memberUrl);
				}
				if(!StringUtils.isBlank(memberName)){
					two_qr_name.setText("推荐人: "+memberName);
				}

			}

			catch (Exception e) {
				Log.e(TAG, "doLogin() Exception: " + e.getMessage());
			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};






	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 生成二维码
	 *
	 * @param strconteString
	 */
	private void twoCode(String strconteString) {

		twoImg.setImageBitmap(CreateCodeUtil.createQRImage(strconteString,ScreenUtils.dp2px(168, mContext),
				ScreenUtils.dp2px(168, mContext), BitmapFactory.decodeResource(getResources(), R.drawable.mapage_head), false));

	}



	@Override
	public void onClick(View v) {

	}

}
