package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.UtilPreference;
import com.tool.utils.view.MoneyEditText;
import com.yywf.R;
import com.yywf.config.ConfigXy;
import com.yywf.http.HttpUtil;
import com.yywf.util.MyActivityManager;

import org.json.JSONObject;

public class ActivityAgent extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityAgent";
	private TextView tv_web;
	private TextView tv_phone;
	private TextView tv_text;

	private LinearLayout ll_agent;
	private MoneyEditText et_amt;
	private EditText et_fr;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_agent);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("代理商专属通道");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}
		if (findViewById(R.id.img_right) != null){
			findViewById(R.id.img_right).setVisibility(View.VISIBLE);
			findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 9));
				}
			});
		}

		initView();



	}




	private void initView(){

		linearLayout(R.id.ll_agent).setOnClickListener(this);
		button(R.id.btn_commit).setOnClickListener(this);
//		tv_text = textView(R.id.tv_text);
//		tv_text.setText(Html.fromHtml(getResources().getString(R.string.set_fee)));

		et_fr = editText(R.id.et_fr);
		et_amt = (MoneyEditText) editText(R.id.et_amt);
		et_amt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

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
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ll_agent:
				startActivity(new Intent(mContext, ActivityAgentSearch.class));
				break;
			case R.id.btn_commit:

				break;
		}
	}
}
