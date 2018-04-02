package com.yywf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityGuide extends Activity implements OnClickListener {

	private Context mContext;
	private ViewPager viewPager;
	private ArrayList<ImageView> images = new ArrayList<ImageView>();
	private MyPagerAdapter adapter;
	private ImageButton button;
	private int currentPosititon;
	private GestureDetector mGestureDetector;
	private int sizeType = 3;// 图片尺寸大小类型

	private TextView id_click;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_guide);
		
		button = (ImageButton) findViewById(R.id.imagebtn);
		button.setOnClickListener(this);
		sizeType = getIntent().getIntExtra("sizeType", 3);
		
		loadDataFromServer();
		initView();
		
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				if (currentPosititon == 4) {
					if (Math.abs(velocityX) < 200) {
						return false;
					}
					if (Math.abs(e1.getRawY() - e2.getRawY()) > 200) {
						return false;
					}
					if (e1.getRawX() - e2.getRawX() > 150) {
						goLoginPager();
						return true;
					}
					if (e2.getRawX() - e1.getRawX() > 200) {
						return false;
					}
				}
				return false;
			}
			
		}, new Handler());

	}

	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.vp);

		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		id_click = findViewById(R.id.id_click);
		id_click.setOnClickListener(this);
	}
	
	/**
	 * 如果加载图片失败，使用本地引导图
	 */
	private void initLocalData() {
		int[] imageIds = new int[] {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
		// 准备显示的图片
		images.clear();
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackgroundResource(imageIds[i]);
//			if (i == 2){
//				imageView.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						goLoginPager();
//					}
//				});
//			}
			
			images.add(imageView);
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 加载引导图数据
	 */
	private void loadDataFromServer(){
		String url = ConfigXy.XY_LOAD_SERVER_IMG;
		RequestParams params = new RequestParams();
		params.add("type_id", "2");//2：引导页
//		params.add("project_name", ConfigApp.HC_APPNAME);
		HttpUtil.get(url, params, new HttpUtil.RequestListener() {
			
			@Override
			public void success(String response) {
				try {
					JSONArray rows = (new JSONObject(response)).getJSONArray("rows");
					images.clear();
					for (int i = 0; i < rows.length(); i++) {
						JSONObject json = rows.getJSONObject(i);
						if(json.getInt("SIZE_TYPE") == sizeType && !StringUtils.isBlank(json.getString("URL"))){
							ImageView imageView = new ImageView(mContext);
							imageView.setScaleType(ScaleType.FIT_XY);
							
							loadImgFromServer(json.getString("URL"), imageView);
							images.add(imageView);
						}
					}
					if(images.size() > 0){
						adapter.notifyDataSetChanged();
					}else{
						initLocalData();
					}
					
				} catch (JSONException e) {
					initLocalData();
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed(Throwable error) {
				initLocalData();
//				Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 从服务器加载图片
	 * 
	 * @param imgurl
	 * @param img
	 */
	private void loadImgFromServer(String imgurl, final ImageView img){
		String imgpath = imgurl;
		ImageLoader.getInstance().displayImage(imgpath, img, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				initLocalData();
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
				img.setImageBitmap(bm);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
	}

	private Handler handler = new Handler();

	/**
	 * 延迟线程，看是否还有下一个字符输入
	 */
	private Runnable delayRun = new Runnable() {

		@Override
		public void run() {
			//在这里调用服务器的接口，获取数据
//			loadfeeDecriptionData();
			goLoginPager();
		}
	};

	private class MyOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
			
		}
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		public void onPageSelected(int position) {
			currentPosititon = position;
			if (position == 3) {
				button.setVisibility(View.INVISIBLE);
//				images.get(position-1).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						goLoginPager();
//					}
//				});

				if(delayRun!=null){
					//每次editText有变化的时候，则移除上次发出的延迟线程
					handler.removeCallbacks(delayRun);
				}


				//延迟800ms，如果不再输入字符，则执行该线程的run方法
				handler.postDelayed(delayRun, 1000);
			} else {
				button.setVisibility(View.INVISIBLE);
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {
		public int getCount() {
			return images.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		public void destroyItem(ViewGroup viewpager, int position, Object object) {
			viewpager.removeView(images.get(position));
		}
		public Object instantiateItem(ViewGroup viewpager, int position) {
			viewpager.addView(images.get(position));
			return images.get(position);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.imagebtn:
				goLoginPager();
				break;
			case R.id.id_click:
				goLoginPager();
				break;
		}
	}

	/**
	 * 跳转到登录界面
	 */
	private void goLoginPager() {

		if(delayRun!=null){
			//每次editText有变化的时候，则移除上次发出的延迟线程
			handler.removeCallbacks(delayRun);
		}
		Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
		startActivity(intent);
//		UtilPreference.saveBoolean(getApplicationContext(), "setUpGuide", true);
		finish();
	}

}
