package com.yywf.fuyou.http;

import android.content.Context;

public final class FyHttpConfig {
	private Context context;
	public static boolean release;
	private static FyHttpConfig instance;

	public static final String RSP_CODE_SUCCESS = "0000";
	public static final String RSP_CODE = "Rcd";
	public static final String RSP_DESC = "RDesc";

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new FyHttpConfig();
		}
	}

	public static FyHttpConfig getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	public void setRelease(boolean release) {
		this.release = release;
	}

	public boolean isRelease() {
		return release;
	}

	public String getBaseURL() {
//		"http://www-1.fuiou.com:18670/mobile_pay/sdkpay/";//商户测试的生产环境
//		return "http://192.168.199.188:8080/mobile_pay/findPay/";

		return this.release ? "https://mpay.fuiou.com:16128/findPay/" : "http://www-1.fuiou.com:18670/mobile_pay/findPay/";
		
//		return this.release ? "https://mpay.fuiou.com:16128/sdkpay/"
//		return "http://www-1.fuiou.com:18670/mobile_pay/findPay/";//外网
//		return "http://192.168.8.29:29024/mobile_pay/";//内网
	}

}
