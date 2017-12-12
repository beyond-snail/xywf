package com.yywf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.LoginHistoryVO;

import java.util.List;


public class AdapterLoginHistory extends ArrayAdapter<LoginHistoryVO> {
	private LayoutInflater inflater;
	private int res;

	public AdapterLoginHistory(Context context, int resource, List<LoginHistoryVO> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
		this.res = resource;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(res, null);
		}
		ImageView iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
		TextView tv_account = (TextView) convertView.findViewById(R.id.tv_account);

		LoginHistoryVO vo = getItem(position);

		if (!StringUtils.isBlank(vo.getAvatar())) {
			String avatar = vo.getAvatar();
			ImageLoader.getInstance().displayImage(avatar, iv_avatar);
		} else {
			iv_avatar.setImageResource(R.drawable.login_user_name);
		}
		tv_account.setText(vo.getUsername());

		return convertView;
	}

}
