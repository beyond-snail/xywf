package com.yywf.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.tool.utils.http.HttpUtilKey;
import com.tool.utils.utils.GsonUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.adapter.AdapterLoginHistory;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.LoginHistoryVO;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ActivityMine extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMine";
	private Context mContext;
	private EditText et_user_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_mine);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("个人中心");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();
	}


	private void initView() {
		et_user_name = editText(R.id.et_user_name);
		// 登录
		button(R.id.btn_save).setOnClickListener(this);

	}



	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:// 登录
			doLogin();
			break;

		default:
			break;
		}
	}

	/**
	 * 执行登录
	 */
	private void doLogin() {

		
		showProgress("加载中...");
		RequestParams params = new RequestParams();
//		params.add("account", username);
//		params.add("password", password);
		HttpUtil.get(ConfigXy.XY_LOGIN, params, requestListener);

	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {
				JSONObject obj = new JSONObject(response);


			} catch (Exception e) {
				Log.e(TAG, "doLogin() Exception: " + e.getMessage());
			}
		}

		@Override
		public void failed(Throwable error) {
			disShowProgress();
			showE404();
		}
	};
	




}
