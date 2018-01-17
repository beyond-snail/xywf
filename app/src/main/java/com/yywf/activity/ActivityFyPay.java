package com.yywf.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuiou.mobile.FyPay;
import com.fuiou.mobile.FyPayCallBack;
import com.fuiou.mobile.bean.MchantMsgBean;
import com.fuiou.mobile.util.AppConfig;
import com.fuiou.mobile.util.EncryptUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.LogUtils;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.Split4EditTextWatcher;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.fuyou.http.FyHttpClient;
import com.yywf.fuyou.http.FyHttpInterface;
import com.yywf.fuyou.http.FyHttpResponse;
import com.yywf.fuyou.http.FyXmlNodeData;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

import java.util.HashMap;

public class ActivityFyPay extends BaseActivity  {

	private final String TAG = "ActivityFyPay";
	private TextView tv_user_name;
	private TextView tv_user_id;
	private EditText et_card_no;

	String idCard;
	String userName;

	private String mchnt_cd = "0003310F1063978";
	private String mchnt_key = "4i8p32yfyaup2e9n3okak4xfftjgiu1t";
	private String amount = "19900";
	private String temp, ordernumber;

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


		FyPay.setDev(true);//此代码是配置jar包为环境配置，true是生产   false测试
		FyPay.init(mContext);

		initView();


	}





	private void initView() {

		tv_user_name = textView(R.id.tv_user_name);
		tv_user_id = textView(R.id.tv_user_id);
		et_card_no = editText(R.id.tv_card_no);
		et_card_no.addTextChangedListener(new Split4EditTextWatcher(et_card_no));

		button(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Pay();
			}
		});
	}





	@Override
	protected void onResume() {
		super.onResume();
		//去获取认证的信息
		doGetApproveInfo();
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

		FyHttpClient.getXMLWithPostUrl("createOrder.pay", mhashMap,
				new FyHttpInterface() {

					@Override
					public void requestSuccess(FyHttpResponse resData) {
						FyXmlNodeData data = resData.getXml();
						Log.e("wyl", "订单请求成功返回的响应数据：" + data.toString());
						ordernumber = (String) data.get("OrderId");
					/*	Bundle bundle = new Bundle();
						bundle.putString(FyPay.KEY_ORDER_NO, ordernumber);
//						bundle.putString(FyPay.KEY_MOBILE_NO, "");
						bundle.putString(FyPay.KEY_MAC, getMac());*/


						String bandCard = StringUtils.replaceBlank(et_card_no.getText().toString().trim());

						MchantMsgBean bean = new MchantMsgBean();
						bean.setOrderId(ordernumber);
						bean.setKey(mchnt_key);
						bean.setMchntCd(mchnt_cd.trim());//设置商户号
						bean.setAmt(amount.trim());
						bean.setUserId("12123323");
						bean.setCardNo(bandCard.trim());
						bean.setIDcardType("0");
						bean.setIDNo(idCard.trim());
						bean.setUserName(userName.trim());
						bean.setBackUrl(AppConfig.backUrl);
						bean.setPayType("mobilePay");

						int i = FyPay.pay(mContext, bean,
								new FyPayCallBack() {

									@Override
									public void onPayComplete(String arg0,
															  String arg1, Bundle arg2) {
										//考虑不同的商户ui设计的不同，所以这里商户自行根据响应对界面做成功或者失败的展示
										ToastUtils.CustomShow(mContext, "success");
									}

									@Override
									public void onPayBackMessage(
											String paramString) {

										LogUtils.e(paramString);
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




}
