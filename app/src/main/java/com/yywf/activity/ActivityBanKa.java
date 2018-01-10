package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.utils.banner.AutoPlayManager;
import com.tool.utils.banner.ImageIndicatorView;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.ScreenUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.view.MoneyEditText;
import com.tool.utils.view.MyGridView;
import com.yywf.R;
import com.yywf.adapter.MyMenuAdapter;
import com.yywf.config.EnumConsts;
import com.yywf.model.AdInfo;
import com.yywf.model.Menu;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.ADCommonView;

import java.util.ArrayList;
import java.util.List;

public class ActivityBanKa extends BaseActivity  {

	private final String TAG = "ActivityBanKa";

	private LinearLayout ll_advertis;

	private List<Menu> list = new ArrayList<Menu>();
	private MyGridView gridView;
	private MyMenuAdapter adapter;

//	ImageIndicatorView indicate_view;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_banka);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("一键办卡");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {

		//初始化广告栏
		initAdvertis();
		//初始化类目
		initMenu();

	}


	private void initMenu() {
		for (int i = 0; i < EnumConsts.BankMenuType.values().length; i++){
			switch (i){
				case 0:
				case 1:
				case 2:
//				case 3:
					Menu menu = new Menu();
					menu.setBg(EnumConsts.BankMenuType.values()[i].getBg());
					menu.setName(EnumConsts.BankMenuType.values()[i].getName());
					list.add(menu);
					break;
				default:
					break;
			}
		}

		gridView = (MyGridView) findViewById(R.id.id_gridview);
		adapter = new MyMenuAdapter(mContext, list);
		gridView.setAdapter(adapter);

		//类目
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.e(TAG, "onItemClick " + position);
				// 下拉刷新占据一个位置
				int index = EnumConsts.BankMenuType.getCodeByName(list.get(position).getName());
				switch (index){
					case 1: //浦发银行
						String url_pf = "https://ecentre.spdbccc.com.cn/creditcard/indexActivity.htm?data=P2070702&from=groupmessage&isappinstalled=0";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_pf).putExtra("title",
								list.get(position).getName()));
						break;
					case 2: //民生银行
						String url_ms = "https://creditcard.cmbc.com.cn/wsv2/smp_web/mgm/recomAccept.html?rKey=0b4327f4c4af81f4ecf284fdbb594810672f0869e7f3413d42c8f0b523d1287cdf460d5d2c5687fc186308f6875036ae3672a7ff7f0efa456b149b2c37c0c69b&rpKey=0b4327f4c4af81f4ecf284fdbb594810982de6bc7360ff20d31f8eddab88382520405a1cd35d0d9b12ab87c544592eed9e1901b8fb1e3ff443688e51846b240c";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_ms).putExtra("title",
								list.get(position).getName()));
						break;
					case 3: //兴业银行
						String url_xy = "https://wm.cib.com.cn/application/cardapp/newfast/ApplyCard/toSelectCard?id=a8b4ff5394744e31ba3607c9668cd24c";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_xy).putExtra("title",
								list.get(position).getName()));
						break;
					case 4: //平安银行

//						String url_pa = "https://c.pingan.com/apply/mobile/apply/index.html?showt=1&scc=203100001&ccp=3a2a5a4a1a11a8a10a12a13a9a7a15a20&p=sn%3D%25E8%2582%2596%25E9%259B%2584sc%3DE00010242604ro%3DNcl%3D2&bt8=m_Q0Lp4Y911ouA2Img&isApp=true&bt2=E00010242604&bt5=employmee_edm&bt7=V0182&from=timeline&isappinstalled=0";
//						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_pa).putExtra("title",
//								list.get(position).getName()));
						break;
					case 5: //招商银行

						break;
					case 6: //交通银行

						break;

				}
			}
		});
	}

	//初始化广告栏
	private void initAdvertis(){
		// 广告栏开始
		ll_advertis = (LinearLayout) findViewById(R.id.advertis);
//		indicate_view = (ImageIndicatorView) findViewById(R.id.indicate_view);
		// 设置宽度高度一致
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_advertis.getLayoutParams();
		linearParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2);// 640-370
		// 750-434
		ll_advertis.setLayoutParams(linearParams);

//		List<AdInfo> infos = new ArrayList<>();
//		for (int i = 0; i < 3; i++) {
//			AdInfo info = new AdInfo();
//			info.setPhoto_url("https://m.tourongjia.com/escrowwap/channelTemplateRed ");
//			infos.add(info);
//		}
//		ADCommonView adCommonView = new ADCommonView(mContext, infos, false);
//		ll_advertis.addView(adCommonView);


//		ImageView imageView = new ImageView(mContext);
//		imageView.setImageResource(R.drawable.banner_yijiantie);
//		ll_advertis.addView(imageView);

		local();
	}

	//系统本地图片加载
	public void local() {
		// 声明一个数组, 指定图片的ID
		final Integer[] resArray = new Integer[] {R.drawable.card_banner1, R.drawable.card_banner2
				};
		ImageIndicatorView indicate_view = new ImageIndicatorView(mContext);
		// 把数组交给图片展播组件
		indicate_view.setupLayoutByDrawable(resArray);
		// 展播的风格
//        indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
		indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
		indicate_view.setShowIndicator(false);
		// 显示组件
		indicate_view.show();
		final AutoPlayManager autoBrocastManager = new AutoPlayManager(indicate_view);
		//设置开启自动广播
		autoBrocastManager.setBroadcastEnable(true);
		//autoBrocastManager.setBroadCastTimes(5);//loop times
		//设置开始时间和间隔时间
		autoBrocastManager.setBroadcastTimeIntevel(3000, 3000);
		//设置循环播放
		autoBrocastManager.loop();
		ll_advertis.addView(indicate_view);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



}
