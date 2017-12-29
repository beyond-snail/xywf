package com.yywf.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.http.HttpUtilKey;
import com.tool.utils.utils.AlertUtils;
import com.tool.utils.utils.FileUtils;
import com.tool.utils.utils.GsonUtil;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.RoundImageView;
import com.yywf.R;
import com.yywf.adapter.AdapterLoginHistory;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.model.LoginHistoryVO;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class ActivityMine extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMine";
	private Context mContext;
	private EditText et_user_name;
	// 头像
	private RoundImageView iv_avatar;

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

		String username = getIntent().getStringExtra("userName");

		et_user_name = editText(R.id.et_user_name);
		et_user_name.setEnabled(false);
		if (!StringUtils.isBlank(username)){
			et_user_name.setText(username);
		}
		iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
		// 更换头像
		relativeLayout(R.id.rl_avatar).setOnClickListener(this);
		// 登录
		button(R.id.btn_save).setOnClickListener(this);


		getMemberInfo();
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
		case R.id.rl_avatar:
			String[] array = { "拍照", "本地相册" };
			AlertUtils.selectItems(mContext, array, "上传头像", cameraListener);
			break;
		case R.id.btn_save:// 修改
			doLogin();
			break;

		default:
			break;
		}
	}



	/**
	 * 选择拍照或从相册中选择
	 */
	private DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == 0) {
				// 拍照
				photo();
			} else {
				// 从相册中选择
				pick();
			}
			dialog.dismiss();
		}
	};

	public static final String IMAGE_PATH = "xy";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL, "images/screenshots");
	private File cameraFile;

	/**
	 * 拍照
	 */
	public void photo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				localTempImageFileName = "";
				localTempImageFileName = String.valueOf((new Date()).getTime()) + ".png";
				File filePath = FILE_PIC_SCREENSHOT;
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
//				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				File f = new File(filePath, localTempImageFileName);
//				// localTempImgDir和localTempImageFileName是自己定义的名字
//				Uri u = Uri.fromFile(f);
//				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
//				startActivityForResult(intent, FLAG_CHOOSE_PHONE);
				File f = new File(filePath, localTempImageFileName);
				FileUtils.startActionCapture((Activity) mContext, f, FLAG_CHOOSE_PHONE);
			} catch (ActivityNotFoundException e) {
				//
			}
		}
	}

	/**
	 * 从相册选取照片
	 */
	public void pick() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_IMG);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null,
							null, null);
					if (null == cursor) {
						showErrorMsg("图片没找到");
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					Log.i(TAG, "path=" + path);
					Intent intent = new Intent(mContext, ActivityCropImage.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Log.i(TAG, "path=" + uri.getPath());
					Intent intent = new Intent(mContext, ActivityCropImage.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}

		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(mContext, ActivityCropImage.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);

		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				Log.i(TAG, "截取到的图片路径是 = " + path);
				Bitmap b = BitmapFactory.decodeFile(path);
				iv_avatar.setImageBitmap(b);
				cameraFile = new File(path);

//				uploadImg(cameraFile);
			}

		}
	}

	/**
	 * 执行登录
	 */
	private void doLogin() {

		if (StringUtils.isBlank(et_user_name.getText().toString().trim())){
			ToastUtils.showShort(this, "请输入姓名");
			et_user_name.requestFocus();
			return;
		}

		
		showProgress("加载中...");
		RequestParams params = new RequestParams();
		try {
			params.put("file", cameraFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		params.put("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.put("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.post(ConfigXy.XY_EDIT_MEMBER_INFO, params, new HttpUtil.RequestListener() {

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
					showErrorMsg(result.getString("message"));
					finish();



				} catch (Exception e) {
				}

			}

			@Override
			public void failed(Throwable error) {
				disShowProgress();
				showE404();

			}
		});

	}



	private void getMemberInfo() {

		showProgress("加载中...");
		RequestParams params = new RequestParams();
		params.add("memberId", UtilPreference.getStringValue(mContext, "memberId"));
		params.add("token", UtilPreference.getStringValue(mContext, "token"));
		HttpUtil.get(ConfigXy.XY_GET_MEMBER_INFO, params, requestListener);
	}

	private HttpUtil.RequestListener requestListener = new HttpUtil.RequestListener() {

		@Override
		public void success(String response) {
			disShowProgress();
			try {
				JSONObject result = new JSONObject(response);
				if (!result.optBoolean("status")) {
					showErrorMsg(result.getString("message"));
					return;
				}

				JSONObject dataStr = result.getJSONObject("data");

				String icon = dataStr.optString("icon");


				if (!StringUtils.isBlank(icon)){
					ImageLoader.getInstance().displayImage(icon, iv_avatar);
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


	




}
