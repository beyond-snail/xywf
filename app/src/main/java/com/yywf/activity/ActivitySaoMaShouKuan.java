package com.yywf.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StatusBarUtil;
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

public class ActivitySaoMaShouKuan extends BaseActivity implements OnClickListener {

	private static final String TAG = "ActivitySaoMaShouKuan";
	private ImageView twoImg;

	private String checkQrStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_qb_pay);
		MyActivityManager.getInstance().addActivity(this);



		initTitle("扫码收钱");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();

		Log.e(TAG, "ActivityQbPay onCreate");

//		twoCode("12333333");

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
		twoImg.setImageResource(R.drawable.sk);

		findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 7));
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
//		startTimer();
//		startTimer2();
//		// 创建一个定时器
//		timer.schedule(task, TIME, TIME); // 1s后执行task,经过1s再次执行
//
//		//创建第二个定时器
//		timer2.schedule(task2, TIME2, TIME2);
	}


	@Override
	protected void onPause() {
		super.onPause();
//		stopTimer();
//		stopTimer2();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		stopTimer();
//		stopTimer2();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
//		stopTimer();
//		stopTimer2();
	}

	/**
	 * 生成二维码
	 *
	 * @param strconteString
	 */
	private void twoCode(String strconteString) {
		Bitmap mBitmap = null;
		try {
			if (!strconteString.equals("")) {
				mBitmap = Create2DCode(strconteString);
				twoImg.setImageBitmap(createBitmap(mBitmap,
						zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mapage_head), 100, 100)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	@Override
	public void onClick(View v) {

	}

	public Bitmap Create2DCode(String str) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");
		hints.put(EncodeHintType.MARGIN, 0);
		// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
				ScreenUtils.getScreenWidth(mContext) * 2 / 3, ScreenUtils.getScreenWidth(mContext) * 2 / 3, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xffffffff;
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public File GetCodePath(String name) {
		String EXTERN_PATH = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) == true) {
			EXTERN_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
			File f = new File(EXTERN_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		return new File(EXTERN_PATH + name);
	}

	/**
	 * 图片两端所保留的空白的宽度
	 */
	private int marginW = 20;
	/**
	 * 条形码的编码类型
	 */
	private BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;





	/***
	 * 合并图片
	 * 
	 * @param src
	 * @param watermark
	 * @return
	 */
	private Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		String tag = "createBitmap";
		Log.d(tag, "create a new bitmap");
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);

		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src

		// 在src的中间画watermark
		cv.drawBitmap(watermark, w / 2 - ww / 2, h / 2 - wh / 2, null);// 设置ic_launcher的位置

		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/***
	 * 缩放图片
	 * 
	 * @param src
	 * @param destWidth
	 * @param destHeigth
	 * @return
	 */
	private Bitmap zoomBitmap(Bitmap src, int destWidth, int destHeigth) {
		String tag = "lessenBitmap";
		if (src == null) {
			return null;
		}
		int w = src.getWidth();// 源文件的大小
		int h = src.getHeight();
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) destWidth) / w;// 宽度缩小比例
		float scaleHeight = ((float) destHeigth) / h;// 高度缩小比例
		Log.d(tag, "bitmap width is :" + w);
		Log.d(tag, "bitmap height is :" + h);
		Log.d(tag, "new width is :" + destWidth);
		Log.d(tag, "new height is :" + destHeigth);
		Log.d(tag, "scale width is :" + scaleWidth);
		Log.d(tag, "scale height is :" + scaleHeight);
		Matrix m = new Matrix();// 矩阵
		m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
		Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);// 直接按照矩阵的比例把源文件画入进行
		return resizedBitmap;
	}



}
