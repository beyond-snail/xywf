package com.yywf.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tool.utils.dialog.CustomAlertDialog;
import com.tool.utils.passwordView.KeyBoardDialog;
import com.tool.utils.passwordView.PayPasswordView;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;
import com.yywf.widget.dialog.MyCustomDialog;

import org.json.JSONObject;

public class ActivityTest extends BaseActivity implements OnClickListener {

	private final String TAG = "ActivityMine";
	private Context mContext;
	private EditText et_user_name;


	KeyBoardDialog keyboard;

	MyCustomDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_test);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("个人中心");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		initView();
	}


	private void initView() {
		// 登录
		button(R.id.btn_pwd).setOnClickListener(this);

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
		case R.id.btn_pwd:// 登录
//			keyboard = new KeyBoardDialog(ActivityTest.this, getDecorViewDialog());
//			keyboard.show();
//			dialog = DialogUtils.showDialog(mContext, "温馨提示", "您还未成为沃富会员，是否支付199元成为会员", new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					ToastUtils.CustomShow(mContext, "左边按钮");
//					dialog.dismiss();
//				}
//			}, new OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					ToastUtils.CustomShow(mContext, "右边按钮");
//					dialog.dismiss();
//				}
//			});
			break;

		default:
			break;
		}
	}

	protected View getDecorViewDialog() {

		return PayPasswordView.getInstance("", ActivityTest.this, new PayPasswordView.OnPayListener() {

			@Override
			public void onSurePay(final String password) {// 这里调用验证密码是否正确的请求

				// TODO Auto-generated method stub
//				keyboard.dismiss();
//				keyboard = null;

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (StringUtils.isEquals(password, "123456")) {
							ToastUtils.showShort(getApplicationContext(), "交易成功");
						} else {
//							final NotiDialog dialog = new NotiDialog(MainActivity.this, tips);
//							dialog.show();
//							dialog.setTitleStr("密码错误");
//							dialog.setOkButtonText("忘记密码");
//							dialog.setCancelButtonText("重试");
//							dialog.setPositiveListener(new View.OnClickListener() {// 忘记密码操作
//								@Override
//								public void onClick(View v) {
//
//									ToastUtils.showShortToast(getApplicationContext(), "再好好想想");
//								}
//							}).setNegativeListener(new OnClickListener() {// 重试操作
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									keyboard = new KeyBoardDialog(MainActivity.this, getDecorViewDialog());
//									keyboard.show();
//								}
//							});

						}

					}
				}, 1500);

			}

			@Override
			public void onForgotPwd() {

			}

			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				keyboard.dismiss();
				keyboard = null;
				ToastUtils.showShort(getApplicationContext(), "交易已取消");
			}
		}).getView();
	}





}
