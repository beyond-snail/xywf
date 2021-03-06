package com.yywf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.myokhttp.util.LogUtils;
import com.tool.utils.utils.ALog;
import com.tool.utils.utils.GsonUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MyListView;
import com.yywf.R;
import com.yywf.adapter.AdapterTransRecord;
import com.yywf.adapter.AdapterZhangDanList;
import com.yywf.config.ConfigXy;
import com.yywf.model.MxSdkInfo;
import com.yywf.model.ZhangDanListInfo;
import com.yywf.util.MyActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActivityZhangDanList extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityZhangDanList";




	/**
	 * 数据展示
	 */
	private PullToRefreshScrollView mPullRefreshScrollView;
	private MyListView myListView;
	private AdapterZhangDanList mAdapter;
	private List<ZhangDanListInfo> zhangDanListInfoList = new ArrayList<>();
	private MxSdkInfo info;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_zhangdan_list);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("邮箱选择");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		if (findViewById(R.id.img_right_add) != null) {
			findViewById(R.id.img_right_add).setVisibility(View.VISIBLE);
			findViewById(R.id.img_right_add).setOnClickListener(this);
		}

		initView();
		refshData();
	}


	private void initView() {




		myListView = findViewById(R.id.listview);
		mAdapter = new AdapterZhangDanList(mContext, zhangDanListInfoList);
		myListView.setAdapter(mAdapter);
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				Intent mIntent = new Intent();
//				mIntent.putExtra("agentInfo", zhangDanListInfoList.get(i));
				String sdkName = zhangDanListInfoList.get(i).getUsername();
				String sdkPsw = zhangDanListInfoList.get(i).getPassword();
				String sdkIdpPsw = zhangDanListInfoList.get(i).getIdp_pass();
				setParams(sdkName, sdkPsw, sdkIdpPsw);
			}
		});

	}


	private void refshData(){
		String loginhistory = UtilPreference.getStringValue(mContext, "zhangdan_history");
//		String historyArr = getHistoryArr(loginhistory);
		// json字符串转换成List
//		final List<ZhangDanListInfo> list = (List<ZhangDanListInfo>) GsonUtil.getInstance()
//				.convertJsonStringToList(loginhistory, new TypeToken<List<ZhangDanListInfo>>() {
//				}.getType());

		List<ZhangDanListInfo> list = new Gson().fromJson(loginhistory, new TypeToken<List<ZhangDanListInfo>>() {
		}.getType());
		if (list != null) {
			zhangDanListInfoList.clear();
			zhangDanListInfoList.addAll(list);
			mAdapter.notifyDataSetChanged();
		}
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
		switch (view.getId()) {
			case R.id.img_right_add:
				setParams("", "", "");
				break;
		}
	}

	private void setParams(String sdkName, String sdkPsw, String sdkIdpPsw){
		MxParam mxParam = new MxParam();
		mxParam.setUserId(UtilPreference.getStringValue(mContext, "memberId"));   //必传
		mxParam.setApiKey(ConfigXy.mApiKey);   //必传
		mxParam.setFunction(MxParam.PARAM_FUNCTION_MAIL);
		mxParam.setCallbackTaskInfo(true);
//		mxParam.setQuitLoginDone(MxParam.PARAM_COMMON_YES);
		mxParam.setQuitDisable(true);
		if (!StringUtils.isBlank(sdkName)) {
			String emailName = sdkName;
			String emailPsw = sdkPsw;
			String emailIdpPsw = sdkIdpPsw;
			String emailSuffix = emailName.substring(emailName.indexOf("@") + 1);
			JSONObject loginParams = new JSONObject();
			try {
				loginParams.put("username", emailName);
				loginParams.put("password", emailPsw);
				loginParams.put("sepwd", emailIdpPsw);
				HashMap<String, String> loginCustom = new HashMap<>();
				loginCustom.put(MxParam.PARAM_CUSTOM_LOGIN_PARAMS, loginParams.toString());
				loginCustom.put(MxParam.PARAM_CUSTOM_LOGIN_CODE, emailSuffix);
				mxParam.setLoginCustom(loginCustom);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
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
					String data = moxieCallBackData.getAppendResult();
					if (!StringUtils.isBlank(data)) {
						int index_first = data.indexOf(":");
						int index_end = data.lastIndexOf("}");
						String str = data.substring(index_first + 2, index_end - 1);
						str = str.replace("\\", "");
						ALog.json("BigdataFragment", str);

						try {
							JSONObject object = new JSONObject(str);
							String dataStr = object.getString("param");
							if (!StringUtils.isBlank(dataStr)) {
								info = new Gson().fromJson(dataStr, new TypeToken<MxSdkInfo>() {
								}.getType());
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

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
									if (info != null) {
										saveHistory(info.getArguments().getUsername(), info.getArguments().getPassword(), info.getArguments().getIdp_pass());
										refshData();
									}
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
	}


	/**
	 * 缓存最近登录过的账号
	 *
	 */
	@SuppressWarnings("unchecked")
	private void saveHistory(String sdkName, String sdkPsw, String sdkIdpPsw) {
		String loginhistory = UtilPreference.getStringValue(mContext, "zhangdan_history");
		Log.i(TAG, "本次登录前已经登录过的账号：" + loginhistory);

//		JsonArray array = new JsonArray();
		List<ZhangDanListInfo> zhangDanListInfoList = new ArrayList<>();

		if (StringUtils.isBlank(loginhistory)) {
//			JsonObject json = new JsonObject();
//			json.addProperty("username", sdkName);
//			json.addProperty("password", sdkPsw);
//			json.addProperty("idp_pass", sdkIdpPsw);
//			array.add(json);
			ZhangDanListInfo zhangDanListInfo = new ZhangDanListInfo();
			zhangDanListInfo.setUsername(sdkName);
			zhangDanListInfo.setPassword(sdkPsw);
			zhangDanListInfo.setIdp_pass(sdkIdpPsw);
			zhangDanListInfoList.add(zhangDanListInfo);

		} else {
//			String historyArr = getHistoryArr(loginhistory);
//			if (StringUtils.isBlank(historyArr)) {
//				return;
//			}

			// json字符串转换成List
//			List<ZhangDanListInfo> list = (List<ZhangDanListInfo>) GsonUtil.getInstance()
//					.convertJsonStringToList(historyArr, new TypeToken<List<ZhangDanListInfo>>() {
//					}.getType());
			List<ZhangDanListInfo> list = new Gson().fromJson(loginhistory, new TypeToken<List<ZhangDanListInfo>>() {
			}.getType());



			boolean ishas = false;
			for (ZhangDanListInfo vo : list) {
				if (vo.getUsername().equals(sdkName)) {//标识已有账号
					ishas = true;
				}
//				JsonObject json = new JsonObject();
//				json.addProperty("username", vo.getUsername());
//				json.addProperty("password", vo.getPassword());
//				json.addProperty("idp_pass", vo.getIdp_pass());
//				array.add(json);
				ZhangDanListInfo zhangDanListInfo = new ZhangDanListInfo();
				zhangDanListInfo.setUsername(vo.getUsername());
				zhangDanListInfo.setPassword(vo.getPassword());
				zhangDanListInfo.setIdp_pass(vo.getIdp_pass());
				zhangDanListInfoList.add(zhangDanListInfo);
			}
			// 当前登录账号在本机上没有历史登录记录
			if (!ishas) {
//				JsonObject json = new JsonObject();
//				json.addProperty("username", sdkName);
//				json.addProperty("password", sdkPsw);
//				json.addProperty("idp_pass", sdkIdpPsw);
//				array.add(json);
				ZhangDanListInfo zhangDanListInfo = new ZhangDanListInfo();
				zhangDanListInfo.setUsername(sdkName);
				zhangDanListInfo.setPassword(sdkPsw);
				zhangDanListInfo.setIdp_pass(sdkIdpPsw);
				zhangDanListInfoList.add(zhangDanListInfo);
			}
		}
		String historyStr = new Gson().toJson(zhangDanListInfoList);
		// json对象转换成字符串并缓存在本地
//		UtilPreference.saveString(mContext, "zhangdan_history", GsonUtil.getInstance().convertObjectToJsonString(array));
		UtilPreference.saveString(mContext, "zhangdan_history", historyStr);
	}

	/**
	 * 根据缓存在本地的登录历史记录字符串 获取登录历史记录的数组
	 *
	 * @param loginhistory
	 */
	private String getHistoryArr(String loginhistory) {
		String historyArr = "";
		try {
			JSONObject json = new JSONObject(loginhistory);
			if (json.has("elements")) {
				historyArr = json.getString("elements");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return historyArr;
	}


}
