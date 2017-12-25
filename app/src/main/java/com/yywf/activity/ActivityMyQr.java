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


		twoCode("12333333");

	}



	private void loadData() {
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		String id = UtilPreference.getStringValue(mContext, "zf_member_id");
		params.add("memberId", id);
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.get(ConfigXy.GET_CODE_API, params, requestListener);

	}

	private void checkQrTask(){
		if (StringUtils.isBlank(checkQrStr)){
			Log.e(TAG, "回调参数为空");
			return;
		}
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		String id = UtilPreference.getStringValue(mContext, "zf_member_id");
		params.add("memberId", id);
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		params.add("marked", checkQrStr);
		HttpUtil.get(ConfigXy.GET_CHECK_QR_CODE_API, params, requestListener1);
	}

	@SuppressLint("NewApi")
	private void initView() {

		twoImg = (ImageView) findViewById(R.id.two_qr);

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

				String codeStr = result.optString("data");

				String code[] = codeStr.split("&");

				checkQrStr = result.optString("data_extend");

				if (!StringUtils.isEmpty(codeStr)) {

					twoCode(codeStr);
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

	private HttpUtil.RequestListener requestListener1 = new HttpUtil.RequestListener() {

		@SuppressLint("NewApi")
		@Override
		public void success(String response) {
			disShowProgress();

			try {
				JSONObject result = new JSONObject(response);

				if (!result.optBoolean("status")) {
//                    showErrorMsg(result.getString("message"));
                    return;
                }
				Gson gson = new Gson();

				QbQrResult datas = gson.fromJson(result.getString("data"), new TypeToken<QbQrResult>() {
				}.getType());
//				startActivity(new Intent(mContext, ActivityQBQrResult.class).putExtra("data", datas));
//				finish();
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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Log.e(TAG, "time task......" + Integer.toString(i++));
				loadData();
			}else if(msg.what == 2){
				checkQrTask();
			}
			super.handleMessage(msg);
		};
	};

	private int i = 0;
	private int TIME = 60 * 1000; // 一分钟
	private int TIME2 = 10 * 1000; //30秒
	Timer timer = new Timer();
	Timer timer2 = new Timer();
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// 需要做的事:发送消息
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	TimerTask task2 = new TimerTask() {

		@Override
		public void run() {
			// 需要做的事:发送消息
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};

	// 关闭定时器
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	private void startTimer(){
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// 需要做的事:发送消息
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};
	}

	private void stopTimer2() {
		if (timer2 != null) {
			timer2.cancel();
			timer2 = null;
		}
		if (task2 != null) {
			task2.cancel();
			task2 = null;
		}
	}

	private void startTimer2(){
		timer2 = new Timer();
		task2 = new TimerTask() {

			@Override
			public void run() {
				// 需要做的事:发送消息
				Message message = new Message();
				message.what = 2;
				handler.sendMessage(message);
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
//		loadData();
		startTimer();
		startTimer2();
		// 创建一个定时器
		timer.schedule(task, TIME, TIME); // 1s后执行task,经过1s再次执行

		//创建第二个定时器
		timer2.schedule(task2, TIME2, TIME2);
	}


	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
		stopTimer2();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopTimer();
		stopTimer2();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopTimer();
		stopTimer2();
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
