package com.yywf.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.view.AutoSplitTextView;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.util.MyActivityManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ActivityReadTxt extends BaseActivity {

	private final String TAG = "ActivityReadTxt";
	private AutoSplitTextView tvContent;

	private BufferedReader br;
	private InputStream is;
	private String s = "";

	private int type = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_read_txt);
		MyActivityManager.getInstance().addActivity(this);

		type = getIntent().getIntExtra("type", 0);
		if (type == 0){
			onBackPressed();
			return;
		}


		if (type == 1) {
			initTitle("信易沃富网络服务协议");
		}else if (type == 2){
			initTitle("信易沃富扣款、转账授权协议");
		}else if (type == 3){
			initTitle("快捷收款用户需知");
		}else if (type == 4){
			initTitle("还信用卡用户需知");
		}else if (type == 5){
			initTitle("提现规则");
		}else if (type == 6){
			initTitle("抵用券使用需知");
		}else if (type == 7){
			initTitle("扫码收钱使用需知");
		}else if (type == 8){
			initTitle("安全保障");
		}else{
			initTitle("我要提额");
		}
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {

		try {
			if (type == 1) {
				is = getAssets().open("yy.TXT");
			}else if (type == 2){
				is = getAssets().open("zz.TXT");
			}else if (type == 3){
				is = getAssets().open("kj.TXT");
			}else if (type == 4){
				is = getAssets().open("hk.TXT");
			}else if (type == 5){
				is = getAssets().open("tx.TXT");
			}else if (type == 6){
				is = getAssets().open("dyq.TXT");
			}else if (type == 7){
				is = getAssets().open("smsq.TXT");
			}else if (type == 8){
				is = getAssets().open("tb.TXT");
			}else if (type == 11){
				is = getAssets().open("pfyh.TXT");
			}else if (type == 12){
				is = getAssets().open("msyh.TXT");
			}else if (type == 13){
				is = getAssets().open("xyyh.TXT");
			}else if (type == 14){
				is = getAssets().open("payh.TXT");
			}else if (type == 15){
				is = getAssets().open("zsyh.TXT");
			}else if (type == 16){
				is = getAssets().open("jtyh.TXT");
			}else if (type == 17){
				is = getAssets().open("gsyh.TXT");
			}else if (type == 18){
				is = getAssets().open("jsyh.TXT");
			}else if (type == 19){
				is = getAssets().open("nyyh.TXT");
			}else if (type == 20){
				is = getAssets().open("zgyh.TXT");
			}else if (type == 21){
				is = getAssets().open("gfyh.TXT");
			}else if (type == 22){
				is = getAssets().open("zxyh.TXT");
			}else if (type == 23){
				is = getAssets().open("gdyh.TXT");
			}else if (type == 24){
				is = getAssets().open("hxyh.TXT");
			}
			br = new BufferedReader(new InputStreamReader(is));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				temp += "\n";
				s = s + temp;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		tvContent = (AutoSplitTextView) textView(R.id.content);
		tvContent.setText(s);

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
