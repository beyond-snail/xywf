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

			Menu menu = new Menu();
			menu.setBg(EnumConsts.BankMenuType.values()[i].getBg());
			menu.setName(EnumConsts.BankMenuType.values()[i].getName());
			list.add(menu);
//			switch (i){
//				case 0:
//				case 1:
//				case 2:
////				case 3:
//				case 4:
//				case 5:
//				case 6:
//				case 7:
//				case 9:
//				case 12:
//					Menu menu = new Menu();
//					menu.setBg(EnumConsts.BankMenuType.values()[i].getBg());
//					menu.setName(EnumConsts.BankMenuType.values()[i].getName());
//					list.add(menu);
//					break;
//				default:
//					break;
//			}
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
						String url_xy = "https://ccshop.cib.com.cn:8010/application/cardapp/newfast/ApplyCard/toSelectCard?id=11f4966a588340a3acca4ca861f2b78e";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_xy).putExtra("title",
								list.get(position).getName()));
						break;
					case 4: //平安银行

						String url_pa = "https://c.pingan.com/apply/mobile/apply/index.html?showt=1&scc=203100001&ccp=3a2a5a4a1a11a8a10a12a13a9a7a15a20&p=sn%3D%25E8%2582%2596%25E9%259B%2584sc%3DE00010242604ro%3DNcl%3D2&bt8=m_Q0Lp4Y911ouA2Img&isApp=true&bt2=E00010242604&bt5=employmee_edm&bt7=V0182&from=timeline&isappinstalled=0";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_pa).putExtra("title",
								list.get(position).getName()));
						break;
					case 5: //招商银行
						String url_zs = "https://xyk.cmbchina.com/card/cardList?WT.mc_id=N17WPGWA055C886100CC";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_zs).putExtra("title",
								list.get(position).getName()));
						break;
					case 6: //交通银行
						String url_jt = "https://creditcardapp.bankcomm.com/applynew/front/apply/new/index.html?trackCode=A032710206072&commercial_id=null&telecom_id=null";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_jt).putExtra("title",
								list.get(position).getName()));
						break;
					case 7: //工商银行
						String url_gs = "http://m.icbc.com.cn/ICBC/%e4%bf%a1%e7%94%a8%e5%8d%a1/%e7%bd%91%e4%b8%8a%e5%8a%9e%e5%8d%a1/bkhq.htm";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_gs).putExtra("title",
								list.get(position).getName()));
						break;
					case 8: //建设银行
						String url_js = "http://creditcard.ccb.com/cn/creditcard/apply/wechat/apply.html?SID=SNS&wParam=QMerSTmogaHKx%2FJD4S7Cw%2Fz3H6ozDNcQ9YMTDpyWFKLUZH45N2hX7Kr1UGfM%2BWKMIqmDK2CBOqQwghar3PvBfVK0MpSR9vSzJI1g0QXOJzX7jwKqd7s24%2FAq0e7YHoYI%2Bnm%2F9pGoxD52yx0WFyVwGehHB8PDPAwW9t7cdtoc8RY2ZyB%2B0yeFbOvn47dX5iyL1wMw%2BQbcW%2Fo%3D";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_js).putExtra("title",
								list.get(position).getName()));
						break;
					case 9: //农业银行
						String url_ny = "http://abccc.vfengche.cn/memu/card/page1.html";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_ny).putExtra("title",
								list.get(position).getName()));
						break;
					case 10: //中国银行
						String url_zg = "https://apply.mcard.boc.cn/apply/mobile/product/familyProductList/family";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_zg).putExtra("title",
								list.get(position).getName()));
						break;
					case 11: //广发银行
						String url_gf = "http://m.cgbchina.com.cn/subsite/201603/20423588/wx/index.html";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_gf).putExtra("title",
								list.get(position).getName()));
						break;
					case 12: //中信银行
						String url_zx = "https://creditcard.ecitic.com/h5/shenqing/index.html?sid=SJWXYBK&foot_s=0&llqf=wx";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_zx).putExtra("title",
								list.get(position).getName()));
						break;
					case 13: //光大银行
						String url_gd = "https://xyk.cebbank.com/cebmms/apply/ps/card-list.htm?pro_code=ZHTG03BKRK0001WXXB";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_gd).putExtra("title",
								list.get(position).getName()));
						break;
					case 14: //华夏银行
						String url_hx = "https://creditapply.hxb.com.cn/weixin/cardChoice.html";
						startActivity(new Intent(mContext, ActivityPublicWeb.class).putExtra("url", url_hx).putExtra("title",
								list.get(position).getName()));
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
		final Integer[] resArray = new Integer[] {R.drawable.card_banner4, R.drawable.card_banner5, R.drawable.card_banner6
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
