package com.yywf.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.WordUtil;
import com.tool.utils.view.AutoSplitTextView;
import com.tool.utils.view.MyGridView;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_read_word);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("等级购买规则");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();

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
