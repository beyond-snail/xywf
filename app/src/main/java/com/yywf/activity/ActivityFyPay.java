package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuiou.mobile.FyPay;
import com.fuiou.mobile.FyPayCallBack;
import com.fuiou.mobile.util.AppConfig;
import com.fuiou.mobile.util.EncryptUtils;
import com.fuiou.mobile.util.MD5UtilString;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.MapUtil2;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.utils.XmlUtils;
import com.tool.utils.view.Split4EditTextWatcher;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.fuyou.http.FyHttpClient;
import com.yywf.fuyou.http.FyHttpConfig;
import com.yywf.fuyou.http.FyHttpInterface;
import com.yywf.fuyou.http.FyHttpResponse;
import com.yywf.fuyou.http.FyXmlNodeData;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

public class ActivityFyPay extends BaseActivity  {

	private final String TAG = "ActivityFyPay";
	private TextView tv_user_name;
	private TextView tv_user_id;
	private EditText et_card_no;
	private LinearLayout ll_success;
	private LinearLayout id_card;
	private String orderNo;
	String idCard;
	String userName;

//	private String mchnt_cd = "0003050F0363796";
//	private String mchnt_key = "mht0zrefxyyspytctz9hdj3bcqzx6orw";
	private String mchnt_cd = "0003310F1063978";
	private String mchnt_key = "l3qh715s9wysmkhk1zvnfagenn6afkq7";
	private String amount = "19900";
	public  String mURL = ConfigXy.FY_PAY_CALLBACK;//"http://180.168.100.155:14652/mobile_pay/applePay/backNotify.pay";
	private String temp, ordernumber;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_fy_pay);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("支付");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();

		FyPay.setDev(true);//此代码是配置jar包为环境配置，true是生产   false测试
		FyPay.init(mContext);




	}





	private void initView() {

		userId = UtilPreference.getStringValue(mContext, "memberId");

		ll_success = linearLayout(R.id.ll_success);
		id_card = linearLayout(R.id.id_card);
//
//		tv_user_name = textView(R.id.tv_user_name);
//		tv_user_id = textView(R.id.tv_user_id);
//		et_card_no = editText(R.id.tv_card_no);
//		et_card_no.addTextChangedListener(new Split4EditTextWatcher(et_card_no));

		button(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (id_card.getVisibility() == View.VISIBLE) {
//					Pay();
					loadOrderId();
				}else {
					finish();
				}
			}
		});
	}





	@Override
	protected void onResume() {
		super.onResume();
		//去获取认证的信息
//		doGetApproveInfo();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



	private void doGetApproveInfo() {
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.get(ConfigXy.XY_GET_SMRZ_INFO, params, requestListener1);
	}


	private HttpUtil.RequestListener requestListener1 = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {

				JSONObject result = new JSONObject(response);
				if (result.optInt("code") == -2){
					UtilPreference.clearNotKeyValues(mContext);
					// 退出账号 返回到登录页面
					MyActivityManager.getInstance().logout(mContext);
					return;
				}
				if (!result.optBoolean("status")) {
					ToastUtils.CustomShow(mContext, result.optString("message"));
				}else{
					JSONObject dataStr = result.getJSONObject("data");
					String card_num = dataStr.optString("card_num");
					idCard = dataStr.optString("id_no");
					String phone = dataStr.optString("phone");
					userName = dataStr.optString("member_name");

					tv_user_name.setText(userName);
					tv_user_id.setText(StringUtils.formatCardNo(idCard));

				}



			} catch (Exception e) {
				Log.e(TAG, "doCommit() Exception: " + e.getMessage());
			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};

	private void loadOrderId() {
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		params.add("gradeId", "1");
		params.add("orderAmount", amount.trim()+"");
		HttpUtil.get(ConfigXy.GET_ORDER_ID, params, new HttpUtil.RequestListener() {
			@Override
			public void success(String response) {
				disShowProgress();
				try {
					JSONObject result = new JSONObject(response);
					if (result.optInt("code") == -2){
						UtilPreference.clearNotKeyValues(mContext);
						// 退出账号 返回到登录页面
						MyActivityManager.getInstance().logout(mContext);
						return;
					}
					if (!result.optBoolean("status")) {
						ToastUtils.CustomShow(mContext, result.optString("message"));
					}else{
						JSONObject dataStr = result.getJSONObject("data");
						orderNo = dataStr.optString("orderId");
						String sign = MD5UtilString.MD5Encode(mchnt_cd.trim() + "|"
								+ orderNo + "|"
								+ userId + "|"
								+ amount.trim() + "|"
								+ mURL + "|"
								+ "08"+ "|" + "2.0" + "|"
								+ mchnt_key);

						Bundle bundle = new Bundle();//bundle的key值固定，照传即可
						bundle.putString(FyPay.KEY_MCHNTORDERID, orderNo);
						bundle.putString(FyPay.KEY_MAC, getMac());
						bundle.putString(FyPay.KEY_MCHNT_CD, mchnt_cd.trim());
						bundle.putString(FyPay.KEY_ACTUAL_MONEY, amount.trim());
						bundle.putString(FyPay.KEY_USER_ID, userId);
						bundle.putString(FyPay.KEY_BACKURL, mURL);
						bundle.putString(FyPay.KEY_SIGN, sign);
						Log.e("http", "payBefore >>>"+ bundle.toString());
						//开始调起sdk做支付
						int i = FyPay.pay(mContext, bundle,
								new FyPayCallBack() {

									@Override
									public void onPayBackMessage(String datas) {
										//交易结果后台返回数据datas,商户自行解析数据展示，这里不再处理
										Log.e("http", "onPayBackMessage >>>"+ datas);
//										setCallBackToSbs(datas);
										ll_success.setVisibility(View.VISIBLE);
										id_card.setVisibility(View.GONE);
										button(R.id.btn_commit).setText("完成");
									}
									@Override
									public void onPayComplete(String rspCode, String rspDesc, Bundle extraData) {
										// rspCode: 0001（唯一）；
										// rspDesc：用户取消支付（唯一）；
										// extraData：支付传递的参数。
										// 考虑不同的商户ui设计的不同，所以这里商户自行根据响应对界面做成功或者失败的展示
										Log.e("http", "rspCode = " + rspCode + " ; rspDesc = " + rspDesc);
									}
								});

					}


				} catch (Exception e) {
					Log.e(TAG, "doCommit() Exception: " + e.getMessage());
				}
			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();
			}
		});
	}

//	public void testPay(){
//		orderNo = "20180125140833";//(String) data.get("OrderId");//OrderId：返回的订单号，拿到这个值塞到sdk中
//
//		String sign = MD5UtilString.MD5Encode(mchnt_cd.trim() + "|"
//				+ orderNo + "|"
//				+ userId + "|"
//				+ amount.trim() + "|"
//				+ mURL + "|"
//				+ "08"+ "|" + "2.0" + "|"
//				+ mchnt_key);
//
//		Bundle bundle = new Bundle();//bundle的key值固定，照传即可
//		bundle.putString(FyPay.KEY_MCHNTORDERID, orderNo);
//		bundle.putString(FyPay.KEY_MAC, getMac());
//		bundle.putString(FyPay.KEY_MCHNT_CD, mchnt_cd.trim());
//		bundle.putString(FyPay.KEY_ACTUAL_MONEY, amount.trim());
//		bundle.putString(FyPay.KEY_USER_ID, userId);
//		bundle.putString(FyPay.KEY_BACKURL, mURL);
//		bundle.putString(FyPay.KEY_SIGN, sign);
//		Log.e("http", "payBefore >>>"+ bundle.toString());
//		//开始调起sdk做支付
//		int i = FyPay.pay(mContext, bundle,
//				new FyPayCallBack() {
//
//					@Override
//					public void onPayBackMessage(String datas) {
//						//交易结果后台返回数据datas,商户自行解析数据展示，这里不再处理
//						Log.e("http", "onPayBackMessage >>>"+ datas);
////										setCallBackToSbs(datas);
//						ll_success.setVisibility(View.VISIBLE);
//						id_card.setVisibility(View.GONE);
//						button(R.id.btn_commit).setText("完成");
//					}
//					@Override
//					public void onPayComplete(String rspCode, String rspDesc, Bundle extraData) {
//						// rspCode: 0001（唯一）；
//						// rspDesc：用户取消支付（唯一）；
//						// extraData：支付传递的参数。
//						// 考虑不同的商户ui设计的不同，所以这里商户自行根据响应对界面做成功或者失败的展示
//						Log.e("http", "rspCode = " + rspCode + " ; rspDesc = " + rspDesc);
//					}
//				});
//	}



	public void Pay() {


		temp = EncryptUtils.md5Encrypt(
				mchnt_cd.trim() + "|"
						+ amount.trim() + "|"
						+ mchnt_key).toLowerCase();

		final HashMap<String, String> mhashMap = new HashMap<String, String>();
		mhashMap.put("Rmk1", "");
		mhashMap.put("Rmk2", "");
		mhashMap.put("Rmk3", "");
		mhashMap.put("Sign", temp);
		mhashMap.put("MchntCd", mchnt_cd.trim());
		mhashMap.put("Amt", amount.trim());
		FyHttpConfig.release = true;
		FyHttpClient.getXMLWithPostUrl("createOrder.pay", mhashMap,
				new FyHttpInterface() {

					@Override
					public void requestSuccess(FyHttpResponse resData) {
						FyXmlNodeData data = resData.getXml();
						orderNo = (String) data.get("OrderId");//OrderId：返回的订单号，拿到这个值塞到sdk中

						String sign = MD5UtilString.MD5Encode(mchnt_cd.trim() + "|"
								+ orderNo + "|"
								+ userId + "|"
								+ amount.trim() + "|"
								+ mURL + "|"
								+ "08"+ "|" + "2.0" + "|"
								+ mchnt_key);

						Bundle bundle = new Bundle();//bundle的key值固定，照传即可
						bundle.putString(FyPay.KEY_MCHNTORDERID, orderNo);
						bundle.putString(FyPay.KEY_MAC, getMac());
						bundle.putString(FyPay.KEY_MCHNT_CD, mchnt_cd.trim());
						bundle.putString(FyPay.KEY_ACTUAL_MONEY, amount.trim());
						bundle.putString(FyPay.KEY_USER_ID, userId);
						bundle.putString(FyPay.KEY_BACKURL, mURL);
						bundle.putString(FyPay.KEY_SIGN, sign);
						Log.e("http", "payBefore >>>"+ bundle.toString());
						//开始调起sdk做支付
						int i = FyPay.pay(mContext, bundle,
								new FyPayCallBack() {

									@Override
									public void onPayBackMessage(String datas) {
										//交易结果后台返回数据datas,商户自行解析数据展示，这里不再处理
										Log.e("http", "onPayBackMessage >>>"+ datas);
//										setCallBackToSbs(datas);
										ll_success.setVisibility(View.VISIBLE);
										id_card.setVisibility(View.GONE);
										button(R.id.btn_commit).setText("完成");
									}
									@Override
									public void onPayComplete(String rspCode, String rspDesc, Bundle extraData) {
										// rspCode: 0001（唯一）；
										// rspDesc：用户取消支付（唯一）；
										// extraData：支付传递的参数。
										// 考虑不同的商户ui设计的不同，所以这里商户自行根据响应对界面做成功或者失败的展示
										Log.e("http", "rspCode = " + rspCode + " ; rspDesc = " + rspDesc);
									}
								});
					}

					@Override
					public void requestFailed(FyHttpResponse resData) {
						// TODO Auto-generated method stub
						ToastUtils.CustomShow(mContext, "请求失败");
					}

					@Override
					public void executeFailed(FyHttpResponse resData) {
						// TODO Auto-generated method stub
						ToastUtils.CustomShow(mContext, "执行失败");
					}

				});

	}

	private void setCallBackToSbs(String datas)  {

		String str = datas.substring(1, datas.length()-1).toLowerCase();
		str = str.replace("\\\"", "\"");
		LogUtils.e(str);

		try {
			HashMap<String, String> map = MapUtil2.xmlStringToMap(str);
			LogUtils.e(map.toString());
			String orderId =  map.get("orderid");
			String amt = map.get("amt");
			String mchntch =  map.get("mchntcd");
			String bankCard =  map.get("bankcard");
			String md5Str = orderId + amt + mchntch + bankCard;
			String sign = EncryptUtils.md5Encrypt(md5Str + mchnt_key).toLowerCase();

			showProgress("加载中...");
			RequestParams params = new RequestParams();
			params.add("orderId", orderId);
			params.add("Amt", amt);
			params.add("mchntcd", mchntch);
			params.add("bankCard", bankCard);
			params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
			params.add("token", UtilPreference.getStringValue(mContext, "token"));
			params.add("sign", sign);

			HttpUtil.get(ConfigXy.FY_PAY, params, new HttpUtil.RequestListener() {
				@Override
				public void success(String response) {
					disShowProgress();
					try {
						JSONObject result = new JSONObject(response);

						if (result.optInt("code") == -2){
							UtilPreference.clearNotKeyValues(mContext);
							// 退出账号 返回到登录页面
							MyActivityManager.getInstance().logout(mContext);
							return;
						}
						if (!result.optBoolean("status")) {
							showErrorMsg(result.getString("message"));
							return;
						}



//						startActivity(new Intent(mContext, ActivityHome.class));
						finish();

					} catch (Exception e) {
						Log.e(TAG, "doLogin() Exception: " + e.getMessage());
					}
				}

				@Override
				public void failed(Throwable error) {
					disShowProgress();
					showE404();
				}
			});
		} catch (DocumentException e) {
			e.printStackTrace();
		}




	}

	private String getMac() {
		return EncryptUtils.md5Encrypt("" + "|"
				+ orderNo + "|" + mchnt_key).toLowerCase();
	}


}
