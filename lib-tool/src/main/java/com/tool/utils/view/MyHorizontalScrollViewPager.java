package com.tool.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 自定义ViewPager
 * <p>
 * 作用：当父控件包含有如ScrollView滑动事件的控件，可阻止父控件与ViewPager的左右滑动事件冲突，PS：
 * 但上下滑动事件还是由父控件ScrollView处理
 * </p>
 * 
 * @author ivan
 * 
 */
public class MyHorizontalScrollViewPager extends ViewPager {

	/** 触摸时按下的点 **/
	private PointF downP = new PointF();
	/** 触摸时当前的点 **/
	private PointF curP = new PointF();

	@SuppressWarnings("unused")
	private GestureDetector mGestureDetector;

	public MyHorizontalScrollViewPager(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context, onGestureListener,
				mHandler);
	}

	public MyHorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, onGestureListener,
				mHandler);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// return mGestureDetector.onTouchEvent(ev);

		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = ev.getX();
		curP.y = ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = ev.getX();
			downP.y = ev.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			float distanceX = curP.x - downP.x;
			float distanceY = curP.y - downP.y;
			// 接近水平滑动，ViewPager控件捕获手势，水平滚动
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {
				// 接近垂直滑动，交给父控件处理
				getParent().requestDisallowInterceptTouchEvent(false);
			}
		}

		return super.onTouchEvent(ev);
	}

	private SimpleOnGestureListener onGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 接近水平滑动时子控件处理该事件，否则交给父控件处理
			return (Math.abs(distanceX) > Math.abs(distanceY));
		}

	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};
}
