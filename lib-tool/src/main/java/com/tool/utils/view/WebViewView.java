package com.tool.utils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * WebView
 * 
 * eg:
 * 
 * WebViewView webViewUtil = new WebViewView();
 * webViewUtil.setWebView(mWebView); webViewUtil.setContext(mContext);
 * webViewUtil.setJS(true); webViewUtil.setSelfAdaption();
 * webViewUtil.setNoCache();
 * webViewUtil.openUrl(getIntent().getExtras().getString("url"));
 * webViewUtil.alarm404();
 * 
 * @author WG
 *
 */

public class WebViewView {
	protected static final String TAG = "WebViewView";
	private Context mContext;
	private WebView mWebView;
	private View mErrorView;

	/**
	 * 设置webview
	 * 
	 * @param mContext
	 */

	public void setWebView(WebView webView) {
		mWebView = webView;

	}

	/**
	 * 打开URL
	 * 
	 * @param url
	 */
	public void openUrl(String url) {
		mWebView.loadUrl(url);
	}

	/**
	 * 网页提醒
	 */
	public void alarm404() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// showErrorPage();// 显示错误页面
				// Toast.makeText(mContext, "网络异常！", 1).show();
				// ToastUtil.showS(mContext, "Oh no!" + description);
				// Log.e(TAG, "Oh no!" + description);
				super.onReceivedError(view, errorCode, description, failingUrl);
				// showErrorPage();//显示错误页面
			}

		});
	}

	/**
	 * 显示自定义错误提示页面，用一个View覆盖在WebView
	 */
	// boolean mIsErrorPage;
	// protected void showErrorPage() {
	// LinearLayout webParentView = (LinearLayout)mWebView.getParent();
	// initErrorPage();//初始化自定义页面
	// while (webParentView.getChildCount() > 1) {
	// webParentView.removeViewAt(0);
	// }
	// @SuppressWarnings("deprecation")
	// LinearLayout.LayoutParams lp = new
	// LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	// webParentView.addView(mErrorView, 0, lp);
	// mIsErrorPage = true;
	// }
	// /****
	// * 把系统自身请求失败时的网页隐藏
	// */
	// protected void hideErrorPage() {
	// LinearLayout webParentView = (LinearLayout)mWebView.getParent();
	//
	// mIsErrorPage = false;
	// while (webParentView.getChildCount() > 1) {
	// webParentView.removeViewAt(0);
	// }
	// }
	// /***
	// * 显示加载失败时自定义的网页
	// */
	// protected void initErrorPage() {
	// if (mErrorView == null) {
	// mErrorView = View.inflate(mContext, R.layout.activity_url_error, null);
	// RelativeLayout button =
	// (RelativeLayout)mErrorView.findViewById(R.id.online_error_btn_retry);
	// button.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// mWebView.reload();
	// }
	// });
	// mErrorView.setOnClickListener(null);
	// }
	// }
	//
	/**
	 * 显示进度
	 */

	public void showShowProgress() {
		// getActivity().getWindow().requestFeature(Window.FEATURE_PROGRESS);
	}

	/**
	 * 设置进度
	 */

	public void setProgress() {
		setProgress = true;
		mWebView.setWebChromeClient(webChromeClient);
	}

	public void setProgressBar(ProgressBar progressBar) {
		loadProgress = true;
		this.progressBar = progressBar;
		mWebView.setWebChromeClient(webChromeClient);
	}

	private boolean setProgress = false;
	private boolean setSupportMultipleWindows = false;
	private ProgressBar progressBar;
	private boolean loadProgress = false;
	@SuppressLint("SetJavaScriptEnabled")
	private WebChromeClient webChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (setProgress) {
				((Activity) mContext).setProgress(newProgress);
			}
			if (loadProgress) {
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
			if (setSupportMultipleWindows) {
				WebView childView = new WebView(mContext);
				final WebSettings settings = childView.getSettings();
				settings.setJavaScriptEnabled(true);
				childView.setWebChromeClient(this);
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				transport.setWebView(childView);
				resultMsg.sendToTarget();
				return true;
			}
			return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
		}

	};

	/**
	 * 启动JAVASCRIPT
	 * 
	 * @param flag
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void setJS(boolean flag) {
		mWebView.getSettings().setJavaScriptEnabled(flag);
	}

	/**
	 * 支持多窗口
	 * 
	 * @param flag
	 */
	public void setSupportMultipleWindows(boolean flag) {
		setSupportMultipleWindows = flag;
		mWebView.getSettings().setSupportMultipleWindows(flag);
	}

	/**
	 * 是否开启Database存储
	 * 
	 * @param flag
	 */
	public void setDatabaseEnabled(boolean flag) {
		mWebView.getSettings().setDatabaseEnabled(flag);
	}

	/**
	 * 是否开启DOM存储
	 * 
	 * @param flag
	 */
	public void setDomStorageEnabled(boolean flag) {
		mWebView.getSettings().setDomStorageEnabled(flag);
	}

	/**
	 * 禁止缓存
	 * 
	 * @param flag
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void setNoCache() {
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	}
	
	/**
	 * 加载进来的页面自适应手机屏幕
	 * 
	 * @param flag
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void setSelfAdaption() {
		// mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//
		// 支持内容重新布局
		mWebView.getSettings().setUseWideViewPort(true);// 自适应屏幕
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(true);// 支持缩放
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.setInitialScale(25);
	}

	/**
	 * 垂直滚动条是否可见
	 * 
	 * @param verticalScrollBarEnabled
	 */
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		mWebView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}

	/**
	 * 水平滚动条是否可见
	 * 
	 * @param horizontalScrollBarEnabled
	 */
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
		mWebView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}

	/**
	 * 缩放按钮是否可见
	 * 
	 * @param enabled
	 */
	public void setDisplayZoomControls(boolean enabled) {
		mWebView.getSettings().setDisplayZoomControls(enabled);
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

}
