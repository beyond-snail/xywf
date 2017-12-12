package com.yywf.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 引导图片工具类
 * 
 * @author morton
 *
 */
public class GuideImageUtil {

	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	public static Context mContext;
	private static GuideImageUtil instance;// 单例
	private static int sizeType = 3;// 图片尺寸大小类型
	private OnLoadCompleteListener completeListener;// 加载完成监听
	private boolean isLoadComplete = false;// 是否加载完成
	
	public GuideImageUtil(Context context) {
		mContext = context;
	}
	
	public static GuideImageUtil getInstance(Context context, int type) {
		if (instance == null) {
			instance = new GuideImageUtil(context);
			sizeType = type;
		}
		return instance;
	}
	
	/**
	 * 加载引导图数据
	 */
	public void loadDataFromServer(){
		String url = ConfigXy.XY_LOAD_SERVER_IMG;
		RequestParams params = new RequestParams();
		params.add("type_id", "2");//2：引导页
//		params.add("project_name", ConfigApp.HC_APPNAME);
		HttpUtil.get(url, params, new HttpUtil.RequestListener() {
			
			@Override
			public void success(String response) {
				images.clear();
				try {
					@SuppressWarnings("unused")
					Map<String, List<String>> sizeUrl = new HashMap<String, List<String>>();
					JSONArray rows = (new JSONObject(response)).getJSONArray("rows");
					for (int i = 0; i < rows.length(); i++) {
						JSONObject json = rows.getJSONObject(i);
						if(json.getInt("SIZE_TYPE") == sizeType){
							loadImgFromServer(json.getString("URL"));
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed(Throwable error) {
				Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 从服务器加载图片
	 * 
	 * @param imgurl
	 */
	private void loadImgFromServer(String imgurl){
		String imgpath = imgurl;
		ImageLoader.getInstance().loadImage(imgpath, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setImageBitmap(bm);
				images.add(imageView);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
	}
	
	public OnLoadCompleteListener getCompleteListener() {
		return completeListener;
	}

	public void setCompleteListener(OnLoadCompleteListener completeListener) {
		this.completeListener = completeListener;
	}

	public boolean isLoadComplete() {
		return isLoadComplete;
	}

	public void setLoadComplete(boolean isLoadComplete) {
		this.isLoadComplete = isLoadComplete;
	}

	/**
	 * 引导图片加载完成监听
	 * 
	 * @author morton
	 *
	 */
	public interface OnLoadCompleteListener {
		
		/**
		 * 引导图片加载完毕回调
		 */
		public void OnLoadComplete();
		
		/**
		 * 图片获取失败回调
		 */
		public void OnloadFail();
	}

	public ArrayList<ImageView> getImages() {
		return images;
	}
	
}
