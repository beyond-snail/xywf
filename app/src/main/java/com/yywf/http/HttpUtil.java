package com.yywf.http;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tool.utils.utils.ALog;
import com.tool.utils.utils.UtilPreference;
import com.yywf.myapplication.MainApplication;
import com.yywf.util.MyActivityManager;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	private static final String ENCODING = "UTF-8";

	static {
		client.setTimeout(30000); // 设置链接超时，如果不设置，默认为15s
	}

	/**
	 * 用一个完整url获取一个string对象
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		Log.i("HttpUtil", urlString);
		client.get(urlString, res);
	}

	/**
	 * url里面带参数
	 * 
	 * @param url
	 * @param params
	 * @param res
	 */
	public static void get(String url, RequestParams params, final RequestListener res) {
//		String appSid = UtilPreference.getStringValue(MainApplication.applicationContext, "appSid");
//		params.add("app_member_sid", appSid);
		Log.e("HttpUtil", url + "?" + params.toString());
		client.addHeader("User-Agent", "Java");
		client.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				response(statusCode, responseBody, res);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				res.failed(error);
			}
		});
	}

	public static void get(String url, final RequestListener res) {

		Log.e("HttpUtil", url);
		client.addHeader("User-Agent", "Java");
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				response(statusCode, responseBody, res);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				res.failed(error);
			}
		});
	}



	/**
	 * 不带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, JsonHttpResponseHandler res) {
		Log.e("HttpUtil", urlString);
		client.get(urlString, res);
	}

	/**
	 * 带参数，获取json对象或者数组
	 * 
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		Log.e("HttpUtil", urlString + "?" + params.toString());
		client.get(urlString, params, res);
	}

	/**
	 * 下载数据使用，会返回byte数据
	 * 
	 * @param uString
	 * @param bHandler
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		Log.e("HttpUtil", uString);
		client.get(uString, bHandler);
	}

	/**
	 * url里面带参数
	 * 
	 * @param url
	 * @param params
	 * @param res
	 */
	public static void post(String url, RequestParams params, final RequestListener res) {
		String appSid = UtilPreference.getStringValue(MainApplication.applicationContext, "appSid");
		params.add("app_member_sid", appSid);
		Log.e("HttpUtil", url + "?" + params.toString());
		client.addHeader("User-Agent", "Java");
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				response(statusCode, responseBody, res);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				res.failed(error);
			}
		});
	}

	/**
	 * url里面带参数
	 *
	 * @param url
	 * @param params
	 * @param res
	 */
	public static void postJson(Context mContext, String url, Map<String, Object> params, final RequestListener res) {
		JSONObject jsonParams = new JSONObject(params);
		StringEntity entity;
		Log.e("HttpUtil",  jsonParams.toString());
		client.addHeader("User-Agent", "Java");
		try {
			entity = new StringEntity(jsonParams.toString(), "UTF-8");
			entity.setContentType("application/json;charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}

		client.post(mContext, url, entity, null,  new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				response(statusCode, responseBody, res);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				res.failed(error);
			}
		});
	}



	public static AsyncHttpClient getClient() {
		return client;
	}

	public static void response(int statusCode, byte[] responseBody, RequestListener res) {

		if (statusCode == 200) {
			try {
				String result = new String(responseBody, ENCODING);
//				try {
//					result = StringUtil.unescape(result);
//				} catch (NumberFormatException e) {
//					Log.e("HttpUtil", "appid失效");
//				}
				ALog.json("HttpUtil", result);

				res.success(result);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public static abstract interface RequestListener {
		/**
		 * 请求成功
		 * 
		 * @param response
		 */
		public abstract void success(String response);

		/**
		 * 请求失败
		 * 
		 * @param error
		 */
		public abstract void failed(Throwable error);
	}

	/**
	 * 取消请求
	 * 
	 * @param context
	 */
	public static void onCancelRequest(Context context) {
		client.cancelRequests(context, true);
	}
}
