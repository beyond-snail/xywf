package com.yywf.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManagerNonConfig;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.adapter.FragmentAdapter;
import com.yywf.adapter.VoucherPagerAdapter;
import com.yywf.fragment.FragmentVoucher1;
import com.yywf.fragment.FragmentVoucher2;
import com.yywf.fragment.FragmentVoucher3;
import com.yywf.util.MyActivityManager;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityVoucherTab extends FragmentActivity {
    private static final String[] CHANNELS = new String[]{"待使用", "已使用", "已过期"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
//    private VoucherPagerAdapter voucherPagerAdapter = new VoucherPagerAdapter(mDataList);
    private Context mContext;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;

    //ViewPager的当前选中页
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_vouchers);
        MyActivityManager.getInstance().addActivity(this);
//        initTitle("我的抵扣券");
        init();
        initMagicIndicator1();
    }

    /**
     * 初始化标题和回退
     */
//    public void initTitle(String title) {
//        TextView tvTitle = findViewById(R.id.tv_header);
//        tvTitle.setText(title);
//        Button btn = findViewById(R.id.backBtn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
//    }


    /**
     * 初始化ViewPager和设置监听器
     */
    private void init() {

        Button btn = findViewById(R.id.backBtn);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView rightTv = findViewById(R.id.tv_right);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 6));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        //将三个页面添加到容器里面
        mFragmentList.add(new FragmentVoucher1());
        mFragmentList.add(new FragmentVoucher2());
        mFragmentList.add(new FragmentVoucher3());

        //重写一个FragmentAdapter继承FragmentPagerAdapter，需要传FragmentManager和存放页面的容器过去
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        //ViewPager绑定监听器
        mViewPager.setAdapter(mFragmentAdapter);
        //ViewPager设置默认当前的项
        mViewPager.setCurrentItem(0);
        //ViewPager设置监听器，需要重写onPageScrollStateChanged，onPageScrolled，onPageSelected三个方法
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             * 三个方法的执行顺序为：用手指拖动翻页时，最先执行一遍onPageScrollStateChanged（1），
             * 然后不断执行onPageScrolled，放手指的时候，直接立即执行一次onPageScrollStateChanged（2），
             * 然后立即执行一次onPageSelected，然后再不断执行onPageScrolled，
             * 最后执行一次onPageScrollStateChanged（0）。
             */

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("PageScroll：", "onPageScrollStateChanged" + ":" + state);
            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tablineIv.getLayoutParams();
//                Log.i("mOffset", "offset:" + offset + ",position:" + position);
//                /**
//                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
//                 * 设置mTabLineIv的左边距 滑动场景：
//                 * 记3个页面,
//                 * 从左到右分别为0,1,2
//                 * 0->1; 1->2; 2->1; 1->0
//                 */
//                if (currentIndex == 0 && position == 0)// 0->1
//                {
//                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
//                            * (screenWidth / 3));
//
//                } else if (currentIndex == 1 && position == 0) // 1->0
//                {
//                    lp.leftMargin = (int) (-(1 - offset)
//                            * (screenWidth * 1.0 / 3) + currentIndex
//                            * (screenWidth / 3));
//
//                } else if (currentIndex == 1 && position == 1) // 1->2
//                {
//                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
//                            * (screenWidth / 3));
//                } else if (currentIndex == 2 && position == 1) // 2->1
//                {
//                    lp.leftMargin = (int) (-(1 - offset)
//                            * (screenWidth * 1.0 / 3) + currentIndex
//                            * (screenWidth / 3));
//                }
//                tablineIv.setLayoutParams(lp);
            }

            /**
             * 将当前选择的页面的标题设置字体颜色为蓝色
             */
            @Override
            public void onPageSelected(int position) {
                Log.i("PageScroll：", "onPageSelected" + ":" + position);
//                resetTextView();
//                switch (position) {
//                    case 0:
//                        mainTv.setTextColor(Color.BLUE);
//                        break;
//                    case 1:
//                        momentTv.setTextColor(Color.BLUE);
//                        break;
//                    case 2:
//                        settingTv.setTextColor(Color.BLUE);
//                        break;
//                }
                currentIndex = position;
            }
        });

    }


    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94746"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#e94746"));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


}
