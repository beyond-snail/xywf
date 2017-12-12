package com.hankkin.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import java.util.Date;

import com.handmark.pulltorefresh.library.R;

public class RefreshSwipeMenuListView extends ListView implements OnScrollListener {

    /**
     * 鏁翠釜鍔犺浇鏇村鎺т欢鐨勮儗鏅鑹茶祫婧恑d
     */
    private int mLoadMoreBackgroundColorRes = -1;
    /**
     * 鏁翠釜鍔犺浇鏇村鎺т欢鐨勮儗鏅痙rawable璧勬簮id
     */
    private int mLoadMoreBackgroundDrawableRes = -1;

    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;
    public static final int BOTH = 2;//涓婃媺鍜屼笅鎷�
    public static final int HEADER = 0;//涓嬫媺
    public static final int FOOTER = 1;//涓婃媺
    public static String tag;//ListView鐨勫姩浣�
    public static final String REFRESH = "refresh";
    public static final String LOAD = "load";
    private int MAX_Y = 5;  //Y杞存渶澶у亸绉婚噺
    private int MAX_X = 3;  //X杞存渶澶у亸绉婚噺
    private float mDownX;   //瑙︽懜x
    private float mDownY;   //瑙︽懜y
    private int mTouchState;    //瑙︽懜鐘舵��
    private int mTouchPosition; //瑙︽懜浣嶇疆
    private SwipeMenuLayout mTouchView; //婊戝姩寮瑰嚭甯冨眬
    private OnSwipeListener mOnSwipeListener;   //寮瑰嚭鐩戝惉鍣�

    private float firstTouchY;  //绗竴娆¤Е鎽竬鍧愭爣
    private float lastTouchY;   //鏈�鍚庝竴娆¤Е鎽竬鍧愭爣

    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    private float mLastY = -1;
    private Scroller mScroller;
    private OnScrollListener mScrollListener; // 婊戝姩鐩戝惉

    // 涓嬫媺涓婃媺鐩戝惉鍣�
    private OnRefreshListener onRefreshListener;

    //涓嬫媺澶�
    private RefreshListHeader mHeaderView;

    //澶撮儴瑙嗗浘鍐呭锛岀敤鏉ヨ绠楀ご閮ㄩ珮搴︼紝涓嶄笅鎷夋椂闅愯棌
    private RelativeLayout mHeaderViewContent;
    //涓嬫媺鏃堕棿鏂囨湰鎺т欢
    //    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // 澶撮儴楂樺害
    private boolean mEnablePullRefresh = true;//鑳藉惁涓嬫媺鍒锋柊
    private boolean mPullRefreshing = false; // 鏄惁姝ｅ湪鍒锋柊

    //涓婃媺灏鹃儴瑙嗗浘
    private LinearLayout mFooterView;
    private boolean mEnablePullLoad;//鏄惁鍙互涓婃媺鍔犺浇
    private boolean mPullLoading;   //鏄惁姝ｅ湪涓婃媺
    private boolean mIsFooterReady = false;
    private int mTotalItemCount;
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 500;
    private final static int PULL_LOAD_MORE_DELTA = 50;
    private final static float OFFSET_RADIO = 2.0f;
    private boolean isFooterVisible = false;
    private boolean isSwipe = true;
    private int lastVisibleItemPosition = 0;// 鏍囪涓婃婊戝姩浣嶇疆
    private Context context;
    private boolean scrollFlag = false;// 鏍囪鏄惁婊戝姩

    /**
     * 搴曢儴鍔犺浇鏇村鑿婅姳鎺т欢
     */
    protected ImageView mFooterChrysanthemumIv;
    /**
     * 搴曢儴鍔犺浇鏇村鑿婅姳drawable
     */
    protected AnimationDrawable mFooterChrysanthemumAd;

    public RefreshSwipeMenuListView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    /**
     * 鍒濆鍖栫粍浠�
     *
     * @param context
     */
    private void init(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        // 鍒濆鍖栧ご閮ㄨ鍥�
        mHeaderView = new RefreshListHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        //        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);
        setHeaderDividersEnabled(false);

        // 鍒濆鍖栧熬閮ㄨ鍥�
        mFooterView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_footer1, null, false);
        mFooterView.setBackgroundColor(Color.TRANSPARENT);
        if (mLoadMoreBackgroundColorRes != -1) {
            mFooterView.setBackgroundResource(mLoadMoreBackgroundColorRes);
        }
        if (mLoadMoreBackgroundDrawableRes != -1) {
            mFooterView.setBackgroundResource(mLoadMoreBackgroundDrawableRes);
        }
        mFooterChrysanthemumIv = (ImageView) mFooterView.findViewById(R.id.iv_normal_refresh_footer_chrysanthemum);
        mFooterChrysanthemumAd = (AnimationDrawable) mFooterChrysanthemumIv.getDrawable();
        // 鍒濆鍖栧ご閮ㄩ珮搴�
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;

    }

    /**
     * 娣诲姞閫傞厤鍣�
     *
     * @param adapter
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mIsFooterReady == false) {  //娣诲姞灏鹃儴闅愯棌
            mIsFooterReady = true;
            addFooterView(mFooterView);
            mFooterView.setVisibility(GONE);
        }
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {//鍒涘缓宸︽粦鑿滃崟
                if (isSwipe) {
                    if (mMenuCreator != null) {
                        mMenuCreator.create(menu);
                    }
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
                if (mOnMenuItemClickListener != null) {//宸︽粦鑿滃崟鐐瑰嚮浜嬩欢
                    mOnMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
                }
                if (mTouchView != null) {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) { //鑾峰彇涓婃y杞村潗鏍�
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:   //鎵嬪娍鎸変笅浜嬩欢銆佽幏鍙栧潗鏍囥�佽缃笂娆′笅鎷夋椂闂�
                firstTouchY = ev.getRawY();
                mLastY = ev.getRawY();
                //                setRefreshTime(RefreshTime.getRefreshTime(getContext()));
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;

                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                if (isSwipe) {
                    //寮瑰嚭宸︽粦鑿滃崟
                    if (mTouchPosition == oldPos && mTouchView != null && mTouchView.isOpen()) {
                        mTouchState = TOUCH_STATE_X;
                        mTouchView.onSwipe(ev);
                        return true;
                    }
                    View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

                    if (mTouchView != null && mTouchView.isOpen()) {
                        mTouchView.smoothCloseMenu();
                        mTouchView = null;
                        return super.onTouchEvent(ev);
                    }
                    if (view instanceof SwipeMenuLayout) {
                        mTouchView = (SwipeMenuLayout) view;
                    }
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                }


                break;
            case MotionEvent.ACTION_MOVE://鎵嬪娍婊戝姩浜嬩欢
                final float deltaY = ev.getRawY() - mLastY;
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                mLastY = ev.getRawY();

                if ((mTouchView == null || !mTouchView.isActive()) && Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) {
                    if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                        // the first item is showing, header has shown or pull down.
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    }
                }

                if (isSwipe) {
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            mTouchView.onSwipe(ev);
                        }
                        getSelector().setState(new int[]{0});
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    } else if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(RefreshListHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            tag = REFRESH;
                            onRefreshListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }

                lastTouchY = ev.getRawY();
                if (canLoadMore()) {
                    loadData();
                }

                if (isSwipe) {
                    if (mTouchState == TOUCH_STATE_X) {
                        if (mTouchView != null) {
                            mTouchView.onSwipe(ev);
                            if (!mTouchView.isOpen()) {
                                mTouchPosition = -1;
                                mTouchView = null;
                            }
                        }
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeEnd(mTouchPosition);
                        }
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return true;
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    class ResetHeaderHeightTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            mPullRefreshing = false;
            mHeaderView.setState(RefreshListHeader.STATE_NORMAL);
            resetHeaderHeight();

        }
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisiblePosition() && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.smoothOpenMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public static interface OnMenuItemClickListener {
        void onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    /**
     * 璁剧疆鍒锋柊鍙敤
     *
     * @param enable
     */
    private void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 璁剧疆鍔犺浇鍙敤
     *
     * @param enable
     */
    private void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.setVisibility(GONE);
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.setVisibility(VISIBLE);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * stop refresh, reset header view.
     * 鍋滄鍒锋柊,閲嶇疆澶撮儴鎺т欢
     */
    private void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     * 鍋滄鍔犺浇鏇村,閲嶇疆灏鹃儴鎺т欢
     */
    private void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setVisibility(GONE);
        }
    }

    /**
     * set last refresh time
     */
    //    public void setRefreshTime(String time) {
    //        mHeaderTimeView.setText(time);
    //    }
    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(RefreshListHeader.STATE_READY);
            } else {
                mHeaderView.setState(RefreshListHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    // setSelection(mTotalItemCount - 1); // scroll to bottom


    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setVisibility(VISIBLE);
        if (onRefreshListener != null) {
            tag = LOAD;
            onRefreshListener.onLoadMore();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            isFooterVisible = true;
        } else {
            isFooterVisible = false;
        }
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

    }

    public void setOnRefreshListener(OnRefreshListener l) {
        onRefreshListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();
    }

    /**
     * 涓婃媺鍔犺浇鍜屼笅鎷夊埛鏂拌姹傚畬姣�
     */
    public void complete() {
        stopLoadMore();
        stopRefresh();
        if (REFRESH.equals(tag)) {
            RefreshTime.setRefreshTime(getContext(), new Date());
        }
    }

    /**
     * 璁剧疆ListView鐨勬ā寮�,涓婃媺鍜屼笅鎷�
     *
     * @param mode
     */
    public void setListViewMode(int mode) {
        if (mode == BOTH) {
            setPullRefreshEnable(true);
            setPullLoadEnable(true);
        } else if (mode == FOOTER) {
            setPullLoadEnable(true);
        } else if (mode == HEADER) {
            setPullRefreshEnable(true);
        }
    }

    /**
     * 鍒ゆ柇鏄惁鍙互涓婅湣鍔犺浇
     *
     * @return
     */
    private boolean canLoadMore() {
        return isBottom() && !mPullLoading && isPullingUp();
    }

    /**
     * 鍒ゆ柇鏄惁鍒拌揪搴曢儴
     *
     * @return
     */
    private boolean isBottom() {
        if (getCount() > 0) {
            if (getLastVisiblePosition() == getAdapter().getCount() - 1 &&
                getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPullingUp() {
        return (firstTouchY - lastTouchY) >= 200;
    }


    private void loadData() {
        if (onRefreshListener != null) {
            setLoading(true);
            mFooterChrysanthemumAd.start();
        }
    }

    public void setLoading(boolean loading) {
        if (this == null) return;
        mPullLoading = loading;
        if (loading) {
            mFooterView.setVisibility(VISIBLE);
            mFooterChrysanthemumAd.stop();
            setSelection(getAdapter().getCount() - 1);
            onRefreshListener.onLoadMore();
        } else {
            mFooterView.setVisibility(GONE);
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }

    public void setSwipe(boolean swipe) {
        isSwipe = swipe;
    }

}
