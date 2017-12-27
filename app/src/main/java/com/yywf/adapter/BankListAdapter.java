package com.yywf.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.utils.utils.StringUtils;
import com.tool.utils.utils.ToastUtils;
import com.yywf.R;
import com.yywf.activity.ActivitySecuritySetting;
import com.yywf.activity.ActivitySmartCreditNow;
import com.yywf.activity.ActivitySmartCreditPlan;
import com.yywf.config.EnumConsts;
import com.yywf.model.BankCardInfo;

import java.util.List;


public class BankListAdapter extends BaseAdapter{
	private Activity activity;// 上下文
	private List<BankCardInfo> list;
	private LayoutInflater inflater = null;// 导入布局
	private int UiType = 0; //1：普通， 2：智能还款 , 3:隐藏，4：微代还

	public BankListAdapter(Activity context, List<BankCardInfo> list, int UiType) {
		this.activity = context;
		this.list = list;
		this.UiType = UiType;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// listview每显示一行数据,该函数就执行一次
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {// 当第一次加载ListView控件时 convertView为空
			convertView = inflater.inflate(R.layout.activity_bank_card_item, null);// 所以当ListView控件没有滑动时都会执行这条语句
			holder = new ViewHolder();
			holder.tvBankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
			holder.tvWh = (TextView) convertView.findViewById(R.id.tv_wh);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			holder.tvAmt = (TextView) convertView.findViewById(R.id.tv_amt);
			holder.tvZdDay = (TextView) convertView.findViewById(R.id.tv_zd_day);
			holder.tvHkDay = (TextView) convertView.findViewById(R.id.tv_hk_day);
			holder.tvHk = (TextView) convertView.findViewById(R.id.tv_hk);

			holder.iconBank = (ImageView) convertView.findViewById(R.id.icon_bank);
			convertView.setTag(holder);// 为view设置标签
		} else {// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvBankName.setText(list.get(position).getBank_name());

		holder.tvWh.setText(StringUtils.getEndStr(list.get(position).getCard_num(), 4));
		holder.tvUserName.setText(list.get(position).getMember_name());

		holder.tvAmt.setText(StringUtils.formatIntMoney(list.get(position).getAmt()));
		holder.tvZdDay.setText(list.get(position).getZdDay()+"日");
		holder.tvHkDay.setText(list.get(position).getHkDay()+"日");

		if (EnumConsts.BankUi.getTypeByName(list.get(position).getBank_name()) != null){
			holder.iconBank.setImageResource(EnumConsts.BankUi.getTypeByName(list.get(position).getBank_name()).getIcon_id());
		}
		if (UiType == 2){
			holder.tvHk.setText("智能还款");
		} else if (UiType == 3){
			holder.tvHk.setVisibility(View.GONE);
		} else if (UiType == 4){
			holder.tvHk.setText("微代还");
		} else if (UiType == 1){
			holder.tvHk.setText("立即还款");
		}
		holder.tvHk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				ToastUtils.showShort(activity, position+"");
				if (UiType == 2){
					activity.startActivity(new Intent(activity, ActivitySmartCreditPlan.class));
				}else if (UiType == 1){
					activity.startActivity(new Intent(activity, ActivitySmartCreditNow.class));
				}
			}
		});


		return convertView;
	}





	public static class ViewHolder {
		public TextView tvBankName;
		public TextView tvWh;
		public TextView tvUserName;
		public TextView tvAmt;
		public TextView tvZdDay;
		public TextView tvHkDay;
		public TextView tvHk;
		public ImageView iconBank;
	}
}
