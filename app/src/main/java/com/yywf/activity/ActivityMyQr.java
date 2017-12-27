package com.yywf.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.QbQrResult;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityMyQr extends BaseActivity implements OnClickListener {

	private static final String TAG = "ActivityMyQr";
	private ImageView twoImg;
	private TextView two_qr_name;
	private String checkQrStr;

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

	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@SuppressLint("NewApi")
		@Override
		public void success(String response) {
			disShowProgress();

			try {
				JSONObject result = new JSONObject(response);

				if (!result.optBoolean("status")) {
                    showErrorMsg(result.getString("message"));
                    return;
                }

				if(!result.has("data")){
					ToastUtils.showShort(mContext, "没有二维码内容");
					return;
				}

				JSONObject dataStr = result.getJSONObject("data");

				String invite_url = dataStr.optString("invite_url");
				String member_name = dataStr.optString("member_name");



				if (!StringUtils.isEmpty(invite_url)) {

					twoCode(invite_url);
				}
				if(!StringUtils.isBlank(member_name)){
					two_qr_name.setText("推荐人: "+member_name);
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
		loadData();

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
