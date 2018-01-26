package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityZy extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityZy";




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_zy);
		MyActivityManager.getInstance().addActivity(this);

		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}



		initView();

	}


	private void initView() {
		int type = getIntent().getIntExtra("type", 0);
		if (type == 1) {
			initTitle("实名认证");
			imageView(R.id.img).setImageDrawable(getResources().getDrawable(R.drawable.certification));
		} else if (type == 2){
			initTitle("快捷收款");
			imageView(R.id.img).setImageDrawable(getResources().getDrawable(R.drawable.collectmoney));
		} else if (type == 3){
			initTitle("自助还款");
			imageView(R.id.img).setImageDrawable(getResources().getDrawable(R.drawable.repay));
		}

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

	}
}
