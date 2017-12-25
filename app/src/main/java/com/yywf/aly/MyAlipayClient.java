package com.yywf.aly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.alipay.sdk.app.PayTask;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调用支付宝sdk进行支付
 * 
 * @author Ivan
 */
public class MyAlipayClient {

    private static final String TAG = "MyAlipayClient";

    /**
     * 请求支付失败
     */
    public static final int RQF_ERROR = 0;

    /**
     * 请求成功
     */
    public static final int RQF_PAY = 1;

    /**
     * 调用快捷支付接口支付
     * 
     * @param context
     * @param orderId
     *            商户订单唯一标识
     * @param subject
     *            商品名称
     * @param body
     *            商品详细描述
     * @param total_fee
     *            付款总金额
     * @param mHandler
     * @param pay_path
     *            支付对象
     */
    public static void pay(final Activity context, String orderId, String subject, String body, String total_fee,
            final Handler mHandler, String pay_path) {
        try {
            Log.i(TAG, "准备支付数据");
            String orderInfo = getOrderInfo(orderId, subject, body, total_fee, pay_path);

            /**
             * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
             */
            String sign = SignUtils.sign(orderInfo, Keys.RSA_PRIVATE);

            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");

            /**
             * 完整的符合支付宝参数规范的订单信息
             */
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

            // start the pay.
            Log.i(TAG, "开始支付");
            Log.i(TAG, "支付信息：" + payInfo);

            // 必须异步调用
            new Thread() {

                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(context);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

                    Log.i(TAG, "支付返回结果：" + result);
                    Message msg = new Message();
                    msg.what = RQF_PAY;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }.start();

        } catch (Exception ex) {
            ex.printStackTrace();
            mHandler.sendEmptyMessage(RQF_ERROR);
        }
    }

    private static String getOrderInfo(String orderId, String subject, String body, String total_fee, String pay_path)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        // 签约合作者身份ID
        sb.append("partner=\"").append(Keys.PARTNER).append("\"");

        // 商户网站唯一订单号
        sb.append("&out_trade_no=\"").append(orderId).append("\"");

        // 商品名称
        sb.append("&subject=\"").append(subject).append("\"");

        // 商品详情
        sb.append("&body=\"").append(body).append("\"");

        // 商品金额
        sb.append("&total_fee=\"").append(total_fee).append("\"");

        // 服务器异步通知页面路径 //需修改
        sb.append("&notify_url=\"").append(URLEncoder.encode(pay_path, "UTF-8"))
        .append("\"");

        // 服务接口名称， 固定值
        sb.append("&service=\"mobile.securitypay.pay\"");

        // 参数编码， 固定值
        sb.append("&_input_charset=\"UTF-8\"");

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        sb.append("&return_url=\"").append(URLEncoder.encode("http://m.alipay.com", "UTF-8")).append("\"");

        // 支付类型， 固定值
        sb.append("&payment_type=\"1\"");

        // 签约卖家支付宝账号
        sb.append("&seller_id=\"").append(Keys.SELLER).append("\"");

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        sb.append("&it_b_pay=\"30m\"");

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // sb.append("&paymethod=\"expressGateway\"");

        return sb.toString();
    }

    @SuppressLint("SimpleDateFormat")
	private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        Log.d(TAG, "outTradeNo: " + key);
        return key;
    }

    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
