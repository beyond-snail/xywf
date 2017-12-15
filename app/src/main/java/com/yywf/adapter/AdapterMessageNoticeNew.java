package com.yywf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yywf.R;
import com.yywf.model.Message;

import java.util.List;

public class AdapterMessageNoticeNew extends BaseAdapter {

	private Context mContext;
	List<Message> messageVOs;
	private LayoutInflater mInflater;

	public AdapterMessageNoticeNew(final Context context, final List<Message> messageVOs) {
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
			convertView = mInflater.inflate(R.layout.item_message_notice, null);

			holder.time = (TextView) convertView.findViewById(R.id.tv_msg_time);
			holder.content = (TextView) convertView.findViewById(R.id.tv_msg_content);
			holder.tv_du = (TextView) convertView.findViewById(R.id.tv_du);
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message vo = messageVOs.get(position);
		String type = "";

		if (vo.isHasRead()) {
			holder.iv_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.daibanshixiang_icon_read));
			holder.tv_du.setBackgroundResource(R.drawable.common_radius_rect_gray);
			holder.tv_du.setText("已读");
			holder.tv_du.setTextColor(mContext.getResources().getColor(R.color.grey_999));
		}else {
			holder.iv_img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.daibanshixiang_icon_unread));
			holder.tv_du.setBackgroundResource(R.drawable.common_radius_rect_red_text_font);
			holder.tv_du.setBackgroundResource(R.drawable.common_radius_rect_red_text_font);
			holder.tv_du.setText("未读");
			holder.tv_du.setTextColor(mContext.getResources().getColor(R.color.text_font));
		}

		holder.content.setText(vo.getMemo());
		holder.time.setText(vo.getGmtCreate());

		return convertView;
	}

	private static final class ViewHolder {

		TextView time;
		TextView content;
		TextView tv_du;
		ImageView iv_img;
	}

}
