package com.yywf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.DbPlan;

import java.util.List;

public class AdapterDbPlan extends BaseAdapter {

	private Context mContext;
	List<DbPlan> messageVOs;
	private LayoutInflater mInflater;

	public AdapterDbPlan(final Context context, final List<DbPlan> messageVOs) {
		this.mContext = context;
		this.messageVOs = messageVOs;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return messageVOs == null ? 0 : messageVOs.size();
	}

	@Override
	public Object getItem(int position) {
		return messageVOs == null ? null : messageVOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return messageVOs == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_db_plan_list, null);

			holder.tv_card_no = (TextView) convertView.findViewById(R.id.tv_card_no);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DbPlan vo = messageVOs.get(position);

		holder.tv_card_no.setText(StringUtils.formatCardNo(vo.getCardNum()));
		holder.tv_amount.setText(StringUtils.formatIntMoney(vo.getAmount()));
		holder.tv_time.setText(vo.getCreateTime());

		return convertView;
	}

	private static final class ViewHolder {

		TextView tv_card_no;
		TextView tv_amount;
		TextView tv_time;
	}

}
