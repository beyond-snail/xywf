package com.yywf.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yywf.R;
import com.yywf.model.AdInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 广告
 * 
 */
public class ADCommonView extends LinearLayout implements OnClickListener {
	private static final String TAG = "ADCommonView";

	private Context mContext;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private ArrayList<View> viewsList = new ArrayList<View>();
	private LinearLayout ll_icon;
	private List<View> icons = new ArrayList<View>();

	// timer 自动滚动实现
	private Timer timer;
	private TimerTask timerTask;
	private Handler handlerBanner;

	/**
	 * 该类有俩个构造函数，与advertis二者选其一，传入广告图片地址
	 */
	List<String> urls = new ArrayList<String>();

	/**
	 * 该类有俩个构造函数，与urls二者选其一，传入广告图片对象
	 */
	List<AdInfo> advertis = new ArrayList<AdInfo>();// 广告图片对象
	private boolean isClickable = false;

	// 记录当前图片所在页面
	private int currentPage = 0;

	private static final int PEROID = 5000;
	
	
	private ImageCycleViewListener listener;
	
	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public interface ImageCycleViewListener {

		/**
		 * 单击图片事件
		 *
		 */
		public void onImageClick(AdInfo info);
	}
	
	

	public ADCommonView(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 传入广告图片地址
	 * 
	 * @param context
	 *            广告图片地址
	 */
	public ADCommonView(Context context, List<String> values) {
		super(context);
		mContext = context;
		urls = values;

		init();
	}
	
	/**
	 * 传入广告图片地址
	 * 
	 * @param context
	 *            广告图片地址
	 */
	public ADCommonView(Context context, List<String> values, boolean flag, ImageCycleViewListener imageCycleViewListener) {
		super(context);
		mContext = context;
		urls = values;
		isClickable = flag;
		this.listener = imageCycleViewListener;
		init();
	}

	/**
	 * 传入广告图片对象
	 * 
	 * @param context
	 *            广告图片对象
	 *            是否可点击图片
	 */
	public ADCommonView(Context context, List<AdInfo> news, boolean flag) {
		super(context);
		mContext = context;
		advertis = news;
		isClickable = flag;

		init();
	}
	

	
	public void setListener(ImageCycleViewListener imageCycleViewListener){
		this.listener = imageCycleViewListener;
	}

	@SuppressLint("InflateParams")
	private void init() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_ad_layout, null);
		viewPager = (ViewPager) view.findViewById(R.id.ad_viewpager);
		ll_icon = (LinearLayout) view.findViewById(R.id.ll_icon);

		addView(view);
		initViewPager();
		startTimer();
		loadAD();
	}

	/**
	 * 请求服务端，从服务端加载广告图片
	 */
	private void loadAD() {
		try {
			viewPager.removeAllViews();
			ll_icon.removeAllViews();
			icons.clear();
			viewsList.clear();

			if (urls.size() > 0) {
				for (int j = 0; j < urls.size(); j++) {
					// 添加一张广告图片
					ImageView iv = addImages(urls.get(j), j == 0);
					if (isClickable) {
						iv.setTag(urls.get(j));
						iv.setOnClickListener(this);
					}
				}
			}
			if (advertis.size() > 0) {
				for (int j = 0; j < advertis.size(); j++) {
					// 添加一张广告图片
					ImageView iv = addImages(advertis.get(j).getPhoto_url(),
							j == 0);
					if (isClickable) {
						iv.setTag(advertis.get(j));
						iv.setOnClickListener(this);
					}
				}
			}
			pagerAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ImageView addImages(String url, boolean isCurr) {
		ImageView ivIcon = new ImageView(mContext);
		if (isCurr) {
			ivIcon.setBackgroundResource(R.drawable.home_lunbo_circle_pre);
		} else {
			ivIcon.setBackgroundResource(R.drawable.home_lunbo_circle);
		}
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 2, 5, 2);
		ll_icon.addView(ivIcon, lp);
		icons.add(ivIcon);

		final ImageView iv = new ImageView(mContext);
		iv.setScaleType(ScaleType.FIT_XY);
		viewsList.add(iv);

		try {
			// 异步加载图片
			ImageLoader.getInstance().displayImage(url, iv);
//			iv.setImageResource(R.drawable.ad);
		} catch (Exception e) {
			Log.e(TAG, "图片加载失败：路径->" + url);
		}
		return iv;
	}

	/**
	 * 初始化ViewPager
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void initViewPager() {
		pagerAdapter = new AdPagerAdapter(mContext, viewsList);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new PageChangeListener());
		// 触摸时停止滚动
		viewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					startTimer();
				} else {
					stopTimer();
				}
				return false;
			}
		});
	}

	/**
	 * 图片适配器
	 * 
	 * @author morton
	 * 
	 */
	class AdPagerAdapter extends PagerAdapter {
		private List<View> views = null;
		@SuppressWarnings("unused")
		private Context mContext;

		public AdPagerAdapter(Context mContext, List<View> views) {
			this.mContext = mContext;
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			if (getCount() > 1) {
				((ViewPager) container).removeView(views.get(position));
			}
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}

	class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			/* 设置下标位置图片变化 */
			for (int i = 0; i < icons.size(); i++) {
				if (arg0 == i) {
					icons.get(i).setBackgroundResource(
							R.drawable.home_lunbo_circle_pre);
				} else {
					icons.get(i).setBackgroundResource(
							R.drawable.home_lunbo_circle);
				}
			}
			currentPage = arg0;
		}
	}

	/**
	 * 设置广告自动滚动的时间控制器
	 */
	@SuppressLint("HandlerLeak")
	private void startTimer() {
//		Log.i(TAG, "开始轮播");
		timer = new Timer();

		if (timerTask != null) {
			timerTask.cancel();
		}

		handlerBanner = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					currentPage++;
					if (currentPage >= viewsList.size()) {
						currentPage = 0;
					}
					viewPager.setCurrentItem(currentPage, true);
				} catch (Exception e) {
					Log.e("timer", e.getMessage());
				}
			}
		};

		timerTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				handlerBanner.sendMessage(msg);
			}
		};
		timer.schedule(timerTask, PEROID, PEROID);
	}

	/**
	 * 停止轮播
	 */
	private void stopTimer() {
//		Log.i(TAG, "停止轮播");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			// 跳转到广告详细页面

//			Intent intent = new Intent(mContext, Activity_Advertis_Detail.class);
//			intent.putExtra("advertisId", vo.getgId());
//			mContext.startActivity(intent);
			if (listener != null) {
				AdInfo vo = (AdInfo) v.getTag();
				listener.onImageClick(vo);
			}
		}
	}
}
