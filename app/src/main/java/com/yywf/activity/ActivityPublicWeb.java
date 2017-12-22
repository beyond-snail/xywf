package com.yywf.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tool.utils.utils.StringUtils;
import com.tool.utils.view.WebViewView;
import com.yywf.R;


/**
 * 公用的网页 通过activity传入url就可以打开并访问 有返回功能
 * 
 * @author WG
 * 
 */
public class ActivityPublicWeb extends BaseActivity {
	private WebView mWebView;

	// WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		mContext = this;
		Bundle bundle = getIntent().getExtras();
		if (bundle == null || StringUtils.isBlank(bundle.getString("url"))) {
			showErrorMsg("没有url参数！");
			finish();
			return;
		}
		boolean title = bundle.getBoolean("title", true);//默认显示标题栏，false不显示
		if (!title) {
			findViewById(R.id.titlelayout).setVisibility(View.GONE);
		}
		// mWebView = new WebView(this.getApplicationContext());
		// LinearLayout mll = (LinearLayout) findViewById(R.id.id_webView);
		// mll.addView(mWebView);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mWebView = (WebView) findViewById(R.id.webview);
		WebViewView webViewUtil = new WebViewView();
		webViewUtil.setWebView(mWebView);
		webViewUtil.setContext(mContext);
		webViewUtil.setProgressBar(progressBar);
		webViewUtil.setJS(true);
		webViewUtil.setSelfAdaption();
		webViewUtil.setNoCache();
		webViewUtil.setDomStorageEnabled(true);
		webViewUtil.setDisplayZoomControls(false);
		webViewUtil.setDatabaseEnabled(true);
		webViewUtil.openUrl(getIntent().getExtras().getString("url"));
		webViewUtil.alarm404();

		initView();
	}

	private void initView() {
		// 是否设置标题
		if (StringUtils.isBlank(getIntent().getExtras().getString("title"))) {
			return;
		}
		textView(R.id.tv_header).setText(
				getIntent().getExtras().getString("title"));
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
			findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					finish();
				}
			});
		}

	}

	@Override
	protected void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// mWebView.destroy();
		if (mWebView != null) {
			try {
				// mWebView.removeAllViews();
				mWebView.destroy();
				mWebView = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onPause() {
		mWebView.onPause();
		super.onPause();
	}

	public void comeBack(View v) {
		finish();
	}

}
