package com.yywf.config;

public class ConfigXy {


    /**
     * 您当前登录账号对应的的ApiKey、Token和Secret(回调验签):
     ApiKey:ac8c5018733142a0a66eff8666df550b
     Token:0462a54463cc4eeba97dafab7af6bea8
     Secret(回调验签):27c7e4bc518c48d095d9caf544771876

     魔杖产品对应的CustomerId和Secret:
     CustomerId:1260
     Secret:e795112b552943e799d657af200cc9a9
     */



//    public static final String SERVER_API = "http://app.yunl7.com";
//    public static final String SERVER_API_NEW = "http://app.yunl7.com";
//    public static final String SERVER_API_IAPP = SERVER_API;
//    public static final String SERVER_API_STORE = SERVER_API;

    public static final int UPGRADE_CODE = 11;			//商博士Android_9 商博士IOS_10
    public static final String DOWNLOAD_APK_FILENAME = "xywf.apk";	//

    // 应用包名
    public static final String PACKAGE = "com.yywf";
    // 应用英文名
    public static final String XYWF_APPNAME = "Xywf";
    // APP版本检车链接
    public static final String HC_VERSION_CHECK = "http://download.hp888.com:8787/oem/yunliu/appversions.json";
    // APK下载链接
    public static final String HC_DOWNLOAD_APK = "http://download.hp888.com:8787/oem/yunliu/"
            + XYWF_APPNAME + ".apk";

    // 手机app下载地址
    public static final String HC_DOWNLOAD_APP = "http://app.yunl7.com/static/base/html/downapp.html";

	// 正式 2.0
//	public static final String ZF_SERVER_API = "http://121.40.64.167:9094/xinyiwofu/";
	public static final String ZF_SERVER_API = "http://sbs.eboss007.com/xinyiwofu/";
	public static final String ZF_SERVER_API2 = "http://sbs.eboss007.com/EBosService/";
	public static final String ZF_MID = "103387170609001"; // 正式(云流正式)


    //版本检测
    public static final String XY_VERSION_CHECK = "http://agent.ebank007.com/api/index/getVersion";

    //引导页
    public static final String XY_LOAD_SERVER_IMG = "";



    //登录
    public static final String XY_LOGIN = ZF_SERVER_API+"app/member/login";

    //注册验证码
    public static final String XY_SMSVALDATE = ZF_SERVER_API+"app/member/sendCode";

    //快捷收款预下单
    public static final String XY_KJSK_PREORDER = ZF_SERVER_API+"/app/member/existedCardOrderPay";

    //快捷收款下单
    public static final String XY_KJSK_PAY_SUBMIT = ZF_SERVER_API+"/app/member/orderPaySubmit";

    public static final String XY_LOGOUT = ZF_SERVER_API+"app/member/outLogin";

    //验证手机号是否注册
    public static final String XY_VERITYTELEPHONE = "";

    //注册
    public static final String XY_REGISTER = ZF_SERVER_API+"app/member/register";

    //忘记密码
    public static final String XY_FORGOT_PWD = "";

    //协议
    public static final String XY_PROTOCOL = "";

    //广告
    public static final String ZF_GET_ADS_API = ZF_SERVER_API+"app/member/advertiseList";

    //关于我们
    public static final String ZF_GET_ABOUT_US = ZF_SERVER_API+"app/member/groupInfo";

    //实名认证
    public static final String XY_SMRZ = ZF_SERVER_API+"app/member/realCheck";

    //获取实名认证信息
    public static final String XY_GET_SMRZ_INFO = ZF_SERVER_API+"app/member/queryCardInfo";

    //获取会员信息
    public static final String XY_GET_MEMBER_INFO = ZF_SERVER_API+"app/member/memberInfo";

    //修改个人信息
    public static final String XY_EDIT_MEMBER_INFO = ZF_SERVER_API+"app/member/updateInfo";


    //修改登录密码
    public static final String XY_EDIT_LOGIN_PWD = ZF_SERVER_API+"app/member/updatepassword";

    //修改支付密码
//    public static final String XY_EDIT_PAY_PWD = "";

    //忘记密码
    public static final String XY_FORGOT_PAY_PWD = ZF_SERVER_API+"app/member/resetpassword";

    //信用卡列表
    public static final String XY_BANK_INFO_LIST = ZF_SERVER_API+"app/member/queryCardList";

    //解绑
    public static final String XY_BANK_INFO_REMOVE = ZF_SERVER_API+"app/member/unbindBinkCard";

    //添加信用卡
    public static final String XY_CREDIT_ADD =  ZF_SERVER_API+"/app/member/bindCreditCard";
    //编辑信用卡
    public static final String XY_CREDIT_EDIT =  ZF_SERVER_API+"/app/member/cardUpdate";

    //储蓄卡信息
    public static final String GET_DEBIT_INFO = ZF_SERVER_API+"app/member/queryCardList";

    //更换储蓄卡
    public static final String CHANGE_DEBIT_INFO = ZF_SERVER_API+"app/member/updateDebitCard";

    //查询账单
    public static final String QUERY_BILL = ZF_SERVER_API + "app/member/queryBill";

    //费率说明
    public static final String FEE_DESCRIPTION = ZF_SERVER_API + "app/member/feeDescription";

    public static final String FEE_DESCRIPTION1 = ZF_SERVER_API + "app/member/actualMoney";

    //获取收款二维码
    public static final String GET_CODE_API = "";

    //查询收款二维码
    public static final String GET_CHECK_QR_CODE_API = "";

    //推广二维码
    public static final String GET_QR = ZF_SERVER_API+"app/member/inviteMember";


    //还款计划列表
    public static final String PLAN_LIST = "";

    //补全信息
    public static final String CREDIT_SUPPLY = ZF_SERVER_API+"app/member/bindCardPerfect";
    //信用卡查询
    public static final String CREDIT_QUERY = ZF_SERVER_API+"app/member/queryCreditCardInfo";

    //已绑定银行卡支付
    public static final String CREDIT_PAY = ZF_SERVER_API+"app/pay/existBankGoodsPay";
    //银行卡购买支付提交
    public static final String CREDIT_PAY_CODE = ZF_SERVER_API+"app/pay/paySubmit";




    public static final String HC_GET_RSA_STORE = "/shopapp/alipay/getPayAccount";

    //获取支付宝RSA秘钥
    public static final String GET_RSA_STORE = "";

    //支付宝支付
    public static final String ALY_PAY = ZF_SERVER_API+"/app/pay/aliPreOrder";

    //会员等级列表
    public static final String GRADE_LIST = ZF_SERVER_API+"app/member/queryXyMemberGrade";

    //生成订单
    public static final String GET_ORDER_ID = ZF_SERVER_API + "app/pay/submitOrder";

    //会员等级是否购买
    public static final String IS_GRADE = ZF_SERVER_API+"app/member/isHasGrade";

    //快捷卡支付
    public static final String FY_PAY = ZF_SERVER_API + "app/callback/sdkBankPay";
    public static final String FY_PAY_CALLBACK = ZF_SERVER_API + "app/callback/fySdkPay";

    //会员钱包信息
    public static final String XY_WALLET_INFO = ZF_SERVER_API + "app/member/memberWalletInfo";

    //预览计划
    public static final String XY_PREVIEW_PLAN = ZF_SERVER_API + "app/pay/previewPlan";
    public static final String XY_DO_PLAN = ZF_SERVER_API + "app/pay/yiShengBankCardBind";
    //我的团队
    public static final String XY_MY_TEAM = ZF_SERVER_API + "app/member/myTeam";


}
