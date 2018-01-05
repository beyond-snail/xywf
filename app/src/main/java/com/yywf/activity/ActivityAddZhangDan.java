package com.yywf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.myokhttp.util.LogUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityAddZhangDan extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivitySetting";
	private RelativeLayout ll_email;
	private RelativeLayout ll_hand;


	private String mUserId = "xinyi_test";
	private String mApiKey = "ac8c5018733142a0a66eff8666df550b";

	private String mThemeColor = "#ff9500";
	private String mAgreementUrl = "https://api.51datakey.com/h5/agreement.html";




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_add_zhangdan);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("导入账单");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		ll_email = relativeLayout(R.id.ll_email);
		ll_email.setOnClickListener(this);
		ll_hand = relativeLayout(R.id.ll_hand);
		ll_hand.setOnClickListener(this);


	}




	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//用来清理数据或解除引用
		MoxieSDK.getInstance().clear();
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ll_email:
//				startActivity(new Intent(mContext, ActivityHandCredit.class));
				MxParam mxParam = new MxParam();
				mxParam.setUserId(UtilPreference.getStringValue(mContext, "memberId"));   //必传
				mxParam.setApiKey(mApiKey);   //必传
				mxParam.setFunction(MxParam.PARAM_FUNCTION_MAIL);

				MoxieSDK.getInstance().start((Activity)mContext, mxParam, new MoxieCallBack() {
					/**
					 *
					 *  物理返回键和左上角返回按钮的back事件以及sdk退出后任务的状态都通过这个函数来回调
					 *
					 * @param moxieContext       可以用这个来实现在魔蝎的页面弹框或者关闭魔蝎的界面
					 * @param moxieCallBackData  我们可以根据 MoxieCallBackData 的code来判断目前处于哪个状态，以此来实现自定义的行为
					 * @return                   返回true表示这个事件由自己全权处理，返回false会接着执行魔蝎的默认行为(比如退出sdk)
					 *
					 *   # 注意，假如设置了MxParam.setQuitOnLoginDone(MxParam.PARAM_COMMON_YES);
					 *   登录成功后，返回的code是MxParam.ResultCode.IMPORTING，不是MxParam.ResultCode.IMPORT_SUCCESS
					 */
					@Override
					public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
						/**
						 *  # MoxieCallBackData的格式如下：
						 *  1.1.没有进行账单导入，未开始！(后台没有通知)
						 *      "code" : MxParam.ResultCode.IMPORT_UNSTART, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "", "loginDone": false, "businessUserId": ""
						 *  1.2.平台方服务问题(后台没有通知)
						 *      "code" : MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "xxx", "loginDone": false, "businessUserId": ""
						 *  1.3.魔蝎数据服务异常(后台没有通知)
						 *      "code" : MxParam.ResultCode.MOXIE_SERVER_ERROR, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "xxx", "loginDone": false, "businessUserId": ""
						 *  1.4.用户输入出错（密码、验证码等输错且未继续输入）
						 *      "code" : MxParam.ResultCode.USER_INPUT_ERROR, "taskType" : "mail", "taskId" : "", "message" : "密码错误", "account" : "xxx", "loginDone": false, "businessUserId": ""
						 *  2.账单导入失败(后台有通知)
						 *      "code" : MxParam.ResultCode.IMPORT_FAIL, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": false, "businessUserId": ""
						 *  3.账单导入成功(后台有通知)
						 *      "code" : MxParam.ResultCode.IMPORT_SUCCESS, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": true, "businessUserId": "xxxx"
						 *  4.账单导入中(后台有通知)
						 *      "code" : MxParam.ResultCode.IMPORTING, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": true, "businessUserId": "xxxx"
						 *
						 *  code           :  表示当前导入的状态
						 *  taskType       :  导入的业务类型，与MxParam.setFunction()传入的一致
						 *  taskId         :  每个导入任务的唯一标识，在登录成功后才会创建
						 *  message        :  提示信息
						 *  account        :  用户输入的账号
						 *  loginDone      :  表示登录是否完成，假如是true，表示已经登录成功，接入方可以根据此标识判断是否可以提前退出
						 *  businessUserId :  第三方被爬取平台本身的userId，非商户传入，例如支付宝的UserId
						 */
						if (moxieCallBackData != null) {
							LogUtils.e("BigdataFragment", "MoxieSDK Callback Data : "+ moxieCallBackData.toString());
							switch (moxieCallBackData.getCode()) {
								/**
								 * 账单导入中
								 *
								 * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
								 * 魔蝎后台会向贵方后台推送Task通知和Bill通知
								 * Task通知：登录成功/登录失败
								 * Bill通知：账单通知
								 */
								case MxParam.ResultCode.IMPORTING:
									if(moxieCallBackData.isLoginDone()) {
										//状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
										LogUtils.e(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

									} else {
										//状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
										LogUtils.e(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
									}
									break;
								/**
								 * 任务还未开始
								 *
								 * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
								 *
								 * example:
								 *  case MxParam.ResultCode.IMPORT_UNSTART:
								 *      showDialog(moxieContext);
								 *      return true;
								 * */
								case MxParam.ResultCode.IMPORT_UNSTART:
									LogUtils.e(TAG, "任务未开始");
									break;
								case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
									Toast.makeText(mContext, "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
									break;
								case MxParam.ResultCode.MOXIE_SERVER_ERROR:
									Toast.makeText(mContext, "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
									break;
								case MxParam.ResultCode.USER_INPUT_ERROR:
									Toast.makeText(mContext, "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
									break;
								case MxParam.ResultCode.IMPORT_FAIL:
									Toast.makeText(mContext, "导入失败", Toast.LENGTH_SHORT).show();
									break;
								case MxParam.ResultCode.IMPORT_SUCCESS:
									LogUtils.e(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

									//根据taskType进行对应的处理
									switch (moxieCallBackData.getTaskType()) {
										case MxParam.PARAM_FUNCTION_EMAIL:
											Toast.makeText(mContext, "邮箱导入成功", Toast.LENGTH_SHORT).show();
											break;
										case MxParam.PARAM_FUNCTION_ONLINEBANK:
											Toast.makeText(mContext, "网银导入成功", Toast.LENGTH_SHORT).show();
											break;
										//.....
										default:
											Toast.makeText(mContext, "导入成功", Toast.LENGTH_SHORT).show();
									}
									moxieContext.finish();
									return true;
							}
						}
						return false;
					}
				});
				break;
			case R.id.ll_hand:
				startActivity(new Intent(mContext, ActivityCreditAdd.class).putExtra("type", 2));
				break;
		}
	}
}
