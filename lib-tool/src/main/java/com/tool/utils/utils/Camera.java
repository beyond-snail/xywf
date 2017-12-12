package com.tool.utils.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.config.ConfingApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 1 直接使用performCamera
 * 2 CarmeraCallback 回调 bitmap
 * 
 * 3 在activity做一个onActivityResult
 * 调用handler通知回调
 * 
 * 	//拍照后回调
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (requestCode) {
		case Camera.CAMERA:
			Message msg = new Message();
			msg.what = Camera.CAMERA;
			Camera.handler.sendMessage(msg);
			break;
		}
	}
 * 
 * 
 * @author WG
 *
 */


public class Camera {
	public static final int CAMERA = 1;
	private static String imagePath;  //当前拍照图片目录
	private static String imageName;  //当前拍照图片名(**.jpg)
	private static Context mContext;
	private static CarmeraCallback callback;
	
	public Camera(Context context, CarmeraCallback callbacks) {
		mContext = context;
		callback = callbacks;
	}
	
	/**
	 * 执行拍照
	 * @throws FileNotFoundException, Exception
	 * @author WG
	 */
	public void performCamera() {
		try {
			//生成当前时间
			Date date = new Date(System.currentTimeMillis());
			//格式化
			SimpleDateFormat sdf = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmssSSS", Locale.getDefault());
			//生成文件名
			imageName = sdf.format(date) + ".jpg";
			//生成目录
			imagePath = Environment.getExternalStorageDirectory().getPath() + "/" + ConfingApp.APPNAME + "/";
			boolean camera = takePhoto(((Activity)mContext), imagePath, imageName, CAMERA);
			if(!camera) {
				ToastUtils.showShort(mContext, "拍照失败");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ToastUtils.showShort(mContext, "拍照失败");
		}
	}
	
	

	public final static Handler handler = new Handler() {
		public void handleMessage(Message message) {
			try {
				handlerImage();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * 处理压缩图片
	 * @throws FileNotFoundException, Exception
	 * @ WG
	 */
	private static void handlerImage() throws FileNotFoundException, Exception {
		
		callback.carmeraCallback(BMapUtil.decodeFile(new File(imagePath + imageName)));
		
	}
	
	public interface CarmeraCallback {
		public void carmeraCallback(Bitmap bitmap);
	}
	
	private boolean takePhoto(Activity activity, String dir,
                              String filename, int cmd) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File cameraDir = new File(dir);
		if (!cameraDir.exists()) {
			cameraDir.mkdirs();
		}

		File file = new File(dir + filename);
		Uri outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		try {
			activity.startActivityForResult(intent, cmd);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}
}
