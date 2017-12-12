package com.tool.utils.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 倒计时类
 * 
 * @author WG
 * 
 */

@SuppressLint("NewApi")
public class MyCountDownTimer extends CountDownTimer {
	private String title;  //view上面计时完毕时显示的文字
	@SuppressWarnings("unused")
	private View view,view2;
	private int onTickBg = -1, type = 0;
	private Drawable finishBg = null;
	private Drawable onTickbBg = null;
	private Handler handler;
	
	public MyCountDownTimer(long millisInFuture, long countDownInterval, View view, int onTickBg, Drawable finishBg) {
		// millisInFuture 总时长
		// countDownInterval 时间间隔
		super(millisInFuture, countDownInterval);
		
		this.view = view;
		this.onTickBg = onTickBg;
		this.finishBg = finishBg;
	}

	public MyCountDownTimer(long millisInFuture, long countDownInterval, View view, Drawable onTickBg, Drawable finishBg) {
		// millisInFuture 总时长
		// countDownInterval 时间间隔
		super(millisInFuture, countDownInterval);

		this.view = view;
		this.onTickbBg = onTickBg;
		this.finishBg = finishBg;
	}

	public MyCountDownTimer(long millisInFuture, long countDownInterval, View view) {
		// millisInFuture 总时长
		// countDownInterval 时间间隔
		super(millisInFuture, countDownInterval);
		
		this.view = view;
	}
	
	public MyCountDownTimer(long millisInFuture, long countDownInterval, View view, int type, Handler handler) {
		// millisInFuture 总时长
		// countDownInterval 时间间隔
		super(millisInFuture, countDownInterval);
		
		this.view = view;
		this.handler = handler;
		this.type = type;
	}


	
	@Override
	public void onFinish() { // 内部类计时完毕时触发
		if(finishBg != null){
			view.setBackground(finishBg);
		}
		if(type == 1){
			handler.sendEmptyMessage(1);
			return;
		}
		setView(true, title == null ? "重新发送" : title);
	}

	@Override
	public void onTick(long milisUtilFinished) { // 内部内 时计过程显示
		if(onTickBg != -1){
			view.setBackgroundColor(onTickBg);
		}
		if (onTickbBg != null){
			view.setBackground(onTickbBg);
		}
		if(type == 1){
			setView(false, milisUtilFinished / 1000 + "秒");
			return;
		}
//		setView(false, "重新获取(" +milisUtilFinished / 1000 + "秒)");
		setView(false, "(" +milisUtilFinished / 1000 + "秒)");
	}
	
	/**
	 * 设置 view
	 * @param clickable  是否可点击
	 * @param text 倒计时完成后显示的文字
	 */
	public void setView(boolean clickable, String text) {
		//在这里添加view
		if(view instanceof Button) {
			Button b = (Button) view;
			b.setText(text);
			b.setClickable(clickable);
		}else if(view instanceof TextView) {
			TextView tv = (TextView) view;
			tv.setText(text);
			tv.setClickable(clickable);
		}
	}
	
	/**
	 * 设置view完毕时显示的文字
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
