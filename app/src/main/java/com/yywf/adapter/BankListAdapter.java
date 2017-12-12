package com.yywf.adapter;

import android.app.Activity;
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
import com.yywf.model.BankCardInfo;

import java.util.List;


public class BankListAdapter extends BaseAdapter{
	private Activity activity;// 上下文
	private List<BankCardInfo> list;
	private LayoutInflater inflater = null;// 导入布局

	public BankListAdapter(Activity context, List<BankCardInfo> list) {
		this.activity = context;
		this.list = list;
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

		holder.tvBankName.setText(list.get(position).getBankName());
		holder.tvWh.setText(list.get(position).getwH());
		holder.tvUserName.setText(list.get(position).getName());
		holder.tvAmt.setText(StringUtils.formatIntMoney(list.get(position).getAmt()));
		holder.tvZdDay.setText(list.get(position).getZdDay()+"日");
		holder.tvHkDay.setText(list.get(position).getHkDay()+"日");
		holder.iconBank.setImageResource(R.drawable.bank_jiaotong);
		holder.tvHk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ToastUtils.showShort(activity, position+"");
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
