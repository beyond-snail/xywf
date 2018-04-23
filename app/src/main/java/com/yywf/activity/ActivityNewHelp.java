package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tool.utils.utils.UtilPreference;
import com.yywf.R;
import com.yywf.util.MyActivityManager;

public class ActivityNewHelp extends BaseActivity implements View.OnClickListener {

	private final String TAG = "ActivityNewHelp";
	private RelativeLayout rl_smrz;
	private RelativeLayout rl_kjsk;
	private RelativeLayout rl_self_hk;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_new_help);
		MyActivityManager.getInstance().addActivity(this);
		initTitle("新手指引");
		if (findViewById(R.id.backBtn) != null) {
			findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
		}

		initView();

	}


	private void initView() {
		rl_smrz = relativeLayout(R.id.rl_smrz);
		rl_smrz.setOnClickListener(this);
		rl_kjsk = relativeLayout(R.id.rl_kjsk);
		rl_kjsk.setOnClickListener(this);
		rl_self_hk = relativeLayout(R.id.rl_self_hk);
		rl_self_hk.setOnClickListener(this);

		relativeLayout(R.id.rl_smart_hk).setOnClickListener(this);

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
			case R.id.rl_smrz:
				startActivity(new Intent(mContext, ActivityZy.class).putExtra("type", 1));
				break;
			case R.id.rl_kjsk:
				startActivity(new Intent(mContext, ActivityZy.class).putExtra("type", 2));
				break;
			case R.id.rl_self_hk:
				startActivity(new Intent(mContext, ActivityZy.class).putExtra("type", 3));
				break;
			case R.id.rl_smart_hk:
				startActivity(new Intent(mContext, ActivityZy.class).putExtra("type", 4));
				break;
		}
	}
}
