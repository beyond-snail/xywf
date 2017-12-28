package com.yywf.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.WordUtil;
import com.tool.utils.view.AutoSplitTextView;
import com.tool.utils.view.MyGridView;
import com.tool.utils.view.MyTableTextView;
import com.yywf.R;
import com.yywf.adapter.AdapterGradeMsg;
import com.yywf.adapter.AdapterVipGrade;
import com.yywf.util.MyActivityManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityReadWord extends BaseActivity {

	private final String TAG = "ActivityReadWord";

	private AdapterGradeMsg adapter;
	private AdapterGradeMsg adapter2;
	private AdapterGradeMsg adapter3;

	private List<String> data1 = Arrays.asList(
			"VIP等级", "VIP0", "VIP1" , "VIP2", "VIP3",
			"等级次数", "无限制", "5", "10", "50",
			"市场价(元)", "199", "199", "199", "199",
			"代理充值价", "199", "199", "199", "199",
			"激活返现(元)", "20", "25", "30", "35",
			"交易分润", "万2", "万3", "万4", "万5",
			"激活返券", "5", "5", "5", "5",
			"业务衍生返现", "/", "/", "/", "/"

	);
	private List<String> data2 = Arrays.asList(
			"VIP等级",  "VIP4", "VIP5", "VIP6", "VIP7" ,
			"等级次数",  "100", "500", "1000", "3000",
			"市场价(元)",  "199", "199","199", "199",
			"代理充值价",  "199", "199","199", "199",
			"激活返现(元)","39", "44", "49", "59",
			"交易分润", "万6",  "万8","万10", "万12",
			"激活返券", "10", "10","10", "20",
			"业务衍生返现",  "/", "/", "/", "/"
	);

	private List<String> data3 = Arrays.asList(
			"VIP等级", "VIP8", "VIP9" , "VIP10",
			"等级次数",   "1万", "3万", "5万",
			"市场价(元)", "199", "199", "199",
			"代理充值价",  "199", "199", "199",
			"激活返现(元)","69", "79", "89",
			"交易分润", "万14", "万18", "万20",
			"激活返券", "20", "20", "20",
			"业务衍生返现", "万0.5", "万0.8", "万1"
	);


	private MyGridView gridview;
	private MyGridView gridview2;
	private MyGridView gridview3;



	private LinearLayout mainLinerLayout;
	private RelativeLayout relativeLayout;
	private String[] name={"代理等级","充值数量(次)","赠送次数(次)","折扣","总价值(元)","代理商分润","推荐返现(元)", "激活奖励(元)", "分润以次(元)", "年均净收益(元)"};
	private String[][] content={
			{"VIP1","5", 	"/",		"/",		"995",			"3",		"100", 		"25", 		"180", 		"305"},
			{"VIP2","10",	"1",		"9折",		"2189",			"4",		"220", 		"55", 		"528", 		"803"},
			{"VIP3","50",	"5",		"9折",		"10945",		"5",		"1100", 	"275", 		"3300", 	"4675"},
			{"VIP4","100",	"10",		"9折",		"2.2万",			"6",		"2200", 	"550", 		"7920", 	"10670"},
			{"VIP5","500",	"60",		"8.8折",		"11.2万",		"8",		"1.12万", 	"5600", 	"5.4万", 	"70560"},
			{"VIP6","1000",	"145",		"8.5折",		"22.8万",		"10",		"2.29万", 	"1.15万", 	"13.8万", 	"171750"},
			{"VIP7","3000",	"590",		"8折",		"71.5万",		"12",		"7.18万", 	"7.18万", 	"51.7万", 	"660560"},
			{"VIP8","1万",	"2460",		"7.5折",		"248万",			"14",		"24.92万", 	"24.92万", 	"209.33万", 	"2591680"},
			{"VIP9","3万",	"8900",		"7折",		"774.1万",		"16",		"77.8万", 	"77.8万", 	"746.9万", 	"9024800"},
			{"VIP10","5万",	"17300",	"6.5折",		"1339.3万",		"18",		"134.6万", 	"134.6万", 	"1453.7万", 	"17228800"},
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.my_table);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("购买等级规则");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
//		initView();
		mainLinerLayout = (LinearLayout) this.findViewById(R.id.MyTable);
		initData();
	}

	private void initData() {
		//初始化标题
		relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table, null);
		MyTableTextView title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_1);
		title.setText(name[0]);
		title.setTextColor(Color.BLACK);

		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_2);
		title.setText(name[1]);
		title.setTextColor(Color.BLACK);
		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_3);
		title.setText(name[2]);
		title.setTextColor(Color.BLACK);
		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_4);
		title.setText(name[3]);
		title.setTextColor(Color.BLACK);
		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_5);
		title.setText(name[4]);
		title.setTextColor(Color.BLACK);
		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_6);
		title.setText(name[5]);
		title.setTextColor(Color.BLACK);

		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_7);
		title.setText(name[6]);
		title.setTextColor(Color.BLACK);

		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_8);
		title.setText(name[7]);
		title.setTextColor(Color.BLACK);

		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_9);
		title.setText(name[8]);
		title.setTextColor(Color.BLACK);

		title = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_10);
		title.setText(name[9]);
		title.setTextColor(Color.BLACK);

		mainLinerLayout.addView(relativeLayout);

		//初始化内容
		for (int i=0;i<10;i++) {

				relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table, null);

				MyTableTextView txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_1);
				txt.setText(content[i][0]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_2);
				txt.setText(content[i][1]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_3);
				txt.setText(content[i][2]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_4);
				txt.setText(content[i][3]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_5);
				txt.setText(content[i][4]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_6);
				txt.setText(content[i][5]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_7);
				txt.setText(content[i][6]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_8);
				txt.setText(content[i][7]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_9);
				txt.setText(content[i][8]);
				txt = (MyTableTextView) relativeLayout.findViewById(R.id.list_1_10);
				txt.setText(content[i][9]);
				mainLinerLayout.addView(relativeLayout);

		}
	}

	private void initView() {


		gridview = (MyGridView) findViewById(R.id.id_gridview);
		adapter = new AdapterGradeMsg(mContext, data1);
		gridview.setAdapter(adapter);


		gridview2 = (MyGridView) findViewById(R.id.id_gridview2);
		adapter2 = new AdapterGradeMsg(mContext, data2);
		gridview2.setAdapter(adapter2);

		gridview3 = (MyGridView) findViewById(R.id.id_gridview3);
		adapter3 = new AdapterGradeMsg(mContext, data3);
		gridview3.setAdapter(adapter3);


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
