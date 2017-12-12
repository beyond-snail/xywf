package com.yywf.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.util.MyActivityManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ActivityAgreement extends BaseActivity {

	@SuppressWarnings("unused")
	private Context mContext;
	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;

		setContentView(R.layout.activity_agreement);

		MyActivityManager.getInstance().addActivity(this);
		((TextView) findViewById(R.id.tv_header)).setText("协议内容");

		// 返回
		Button btnBack = (Button) findViewById(R.id.backBtn);
		btnBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		webView = (WebView) this.findViewById(R.id.webview);
		
		String name = getResources().getString(R.string.app_name);
		try {
		    name = URLEncoder.encode(name,"utf-8");
		    String url = ConfigXy.XY_PROTOCOL + "name=";
	        webView.getSettings().setJavaScriptEnabled(true);
	        webView.loadUrl(url+name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       
	}

}
