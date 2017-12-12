
/******************************************************************
 *
 *    Java Lib For Android, Powered By GuangZhou WLB.
 *
 *    Package:     cn.myapp.mobile.owner.widget
 *
 *    Filename:    MyScollView.java
 *
 *    Description: TODO(用一句话描述该文件做什么)
 *
 *    Copyright:   Copyright (c) 2015-2018
 *
 *    Company:     Digital Telemedia Co.,Ltd
 *
 *    @author:     WLB
 *
 *    @version:    1.0.0
 *
 *    Create at:   2017年6月23日 下午3:45:03
 *
 *    Revision:
 *
 *    2017年6月23日 下午3:45:03
 *        - first revision
 *
 *****************************************************************/
package com.tool.utils.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


/**
 * @ClassName MyScollView
 * @author WLB
 * @category 所属功能分类
 * @Date 2017年6月23日 下午3:45:03
 * @Description what
 */
public class MyScrollView extends ScrollView implements View.OnTouchListener{

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
    

    // 阻尼效果 
    // 记录首次按下位置  
    private float mFirstPosition = 0;  
    // 是否正在放大  
    private Boolean mScaling = false;
    private View dropZoomView;  
    private int dropZoomViewWidth;  
    private int dropZoomViewHeight; 
    
    @Override  
    protected void onFinishInflate() {  
        super.onFinishInflate();  
        init();  
    }  

    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  

    }  

    private void init() {  
        setOverScrollMode(OVER_SCROLL_NEVER);  
        if (getChildAt(0) != null) {  
            ViewGroup vg = (ViewGroup) getChildAt(0);  
            if (vg.getChildAt(0) != null) {  
                dropZoomView = vg.getChildAt(0);  
                setOnTouchListener(this);
            }  
        }  
    }  

    @Override  
    public boolean onTouch(View v, MotionEvent event) {  
        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {  
            dropZoomViewWidth = dropZoomView.getMeasuredWidth();  
            dropZoomViewHeight = dropZoomView.getMeasuredHeight();  
        }  
        switch (event.getAction()) {  
            case MotionEvent.ACTION_UP:  
                //手指离开后恢复图片  
                mScaling = false;  
                replyImage();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                if (!mScaling) {  
                    if (getScrollY() == 0) {  
                        mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回  
                    } else {  
                        break;  
                    }  
                }  
                int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数  
                if (distance < 0) { // 当前位置比记录位置要小，正常返回  
                    break;  
                }
                // 处理放大  
                mScaling = true;  
//                setZoom(2 + distance);//设置放大速度
                return true; // 返回true表示已经完成触摸事件，不再处理  
        }  
        return false;  
    }
    // 回弹动画 (使用了属性动画)  
    public void replyImage() {  
        final float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;
        // 设置动画  
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration((long) (distance * 0.5));//设置回缩复原耗时
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  
            @Override  
            public void onAnimationUpdate(ValueAnimator animation) {  
                float cVal = (Float) animation.getAnimatedValue();  
                setZoom(distance - ((distance) * cVal));  
            }  
        });  
        anim.start();
    }
    //缩放  
    public void setZoom(float s) {  
        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {  
            return;  
        }  
        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();  
        lp.width = (int) (dropZoomViewWidth + s);  
        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));  
        dropZoomView.setLayoutParams(lp);  
    }  
    
}
