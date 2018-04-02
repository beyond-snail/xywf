package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tool.utils.banner.AutoPlayManager;
import com.tool.utils.banner.ImageIndicatorView;
import com.tool.utils.utils.ScreenUtils;
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

public class ActivityBanKTiE extends BaseActivity  {

	private final String TAG = "ActivityBanKTiE";

	private LinearLayout ll_advertis;

	private List<Menu> list = new ArrayList<Menu>();
	private MyGridView gridView;
	private MyMenuAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_banka);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("一键提额");
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
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 11));
						break;
					case 2: //民生银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 12));
						break;
					case 3: //兴业银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 13));
						break;
					case 4: //平安银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 14));
						break;
					case 5: //招商银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 15));
						break;
					case 6: //交通银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 16));
						break;
					case 7: //工商银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 17));
						break;
					case 8: //建设银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 18));
						break;
					case 9: //农业银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 19));
						break;
					case 10: //中国银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 20));
						break;
					case 11: //广发银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 21));
						break;
					case 12: //中信银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 22));
						break;
					case 13: //光大银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 23));
						break;
					case 14: //华夏银行
						startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 24));
						break;
				}
			}
		});
	}

	//初始化广告栏
	private void initAdvertis(){
		// 广告栏开始
		ll_advertis = (LinearLayout) findViewById(R.id.advertis);
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
		final Integer[] resArray = new Integer[] {R.drawable.card_banner7, R.drawable.card_banner8, R.drawable.card_banner9
		};
		ImageIndicatorView indicate_view = new ImageIndicatorView(mContext);
		// 把数组交给图片展播组件
		indicate_view.setupLayoutByDrawable(resArray);
		// 展播的风格
//        indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_ARROW_ROUND_STYLE);
		indicate_view.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
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
