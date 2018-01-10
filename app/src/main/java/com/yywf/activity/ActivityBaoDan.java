package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.util.MyActivityManager;
import com.yywf.widget.dialog.DialogUtils;

public class ActivityBaoDan extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityBaoDan";
	private TextView id_bd_no;
	private TextView tv_account;
	private TextView tv_name;
	private TextView tv_start_time;
	private TextView tv_end_time;

	private ImageView id_img;

	private Button btnCommit;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_mytoubao);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("安全保障");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}


		initView();

	}


	private void initView() {

		id_bd_no = textView(R.id.id_bd_no);
		tv_account = textView(R.id.tv_account);
		tv_name = textView(R.id.tv_name);
		tv_start_time = textView(R.id.tv_start_time);
		tv_end_time = textView(R.id.tv_end_time);

		linearLayout(R.id.ll_xy_tb).setOnClickListener(this);
		btnCommit = button(R.id.btn_commit);
		btnCommit.setOnClickListener(this);
		id_img = imageView(R.id.id_img);



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
			case R.id.ll_xy_tb:
				startActivity( new Intent(mContext, ActivityReadTxt.class).putExtra("type", 8));
				break;
			case R.id.btn_commit:
				DialogUtils.showDialogTb(mContext, false);
				break;
		}
	}
}
