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
				case 3:
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
						String url_ms = "https://creditcard.cmbc.com.cn/wsv2/?etr=APXicO3mUhTAlWEAqQMJa4FVgFQTqg8ipFii6OA1uBbzF2z6BaCVuvZdc/nWOqWpSgIb2+HOyCA7QI5YU1MSz+Z3kCCeUGcReAjvcPz69bDSh8hWe4dL5E+QoXkNiepDWEX2QE1uycQOgxm4kX2Vio5AHWL+6lFyNtWwyyHaydVReAVYdAqzOv/rG+B7jAv/IhbMI+LwBmKaeChh0nuX7cAbkR0HyddQXzBVsec3i87Z2MwD12pnReGT5BTH4QfgDekN0KKplavPuU6zDHL77e1sEF2ssh5yUroax6sczK6lVRavAa0BIxYlpJ2qXABNZIKLjyDRw6id+GynnYn1QKSrIoDrAEDDX3awd4d2qDV4hGXe+M45nlLDkAv7zFS6tTghtahxXLOfHQLa1OPeYHoPKCMj9ggHAOG/nWsOFEVfRYbUXY8UrmyEJC6j5tYkOlpl8gE+SDZ6XRG8GHnTcuXD45+EThksLUvnfm/KEwDL3+58tEu/NyqB6o8IrxME";
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
		// 设置宽度高度一致
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll_advertis.getLayoutParams();
//		linearParams.height = (int) (ScreenUtils.getScreenWidth(mContext) / 2);// 640-370
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


		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(R.drawable.banner_yijiantie);
		ll_advertis.addView(imageView);


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
