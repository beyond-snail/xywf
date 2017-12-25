package com.yywf.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.AdapterBase;
import com.yywf.R;
import com.yywf.model.VipGrade;

import java.util.ArrayList;
import java.util.List;

public class AdapterVipGrade extends AdapterBase<VipGrade> {


    private final static String TAG = "AdapterVipGrade";
    private List<VipGrade> data = new ArrayList<VipGrade>();

    private Context context;

    public AdapterVipGrade(Context context, List<VipGrade> data) {
        super(data, context);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_vip_grade, null);

            holder.id_vip = (TextView) convertView.findViewById(R.id.id_vip);
            holder.ll_bg = (LinearLayout) convertView.findViewById(R.id.ll_bg);
            holder.id_choose = (ImageView) convertView.findViewById(R.id.id_choose);
            holder.id_hot = (ImageView) convertView.findViewById(R.id.id_hot);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final VipGrade vo = data.get(position);

        if (vo.isDefault()) {
            holder.id_vip.setTextColor(mContext.getResources().getColor(R.color.text_font));
            holder.ll_bg.setBackgroundResource(R.drawable.common_radius_rect_red_text_font);
            holder.id_choose.setVisibility(View.VISIBLE);
            if (vo.isHot()){
                holder.id_hot.setVisibility(View.VISIBLE);
            }else{
                holder.id_hot.setVisibility(View.GONE);
            }

        } else {
            holder.id_vip.setTextColor(mContext.getResources().getColor(R.color.gray));
            holder.ll_bg.setBackgroundResource(R.drawable.common_radius_rect_gray);
            holder.id_choose.setVisibility(View.GONE);
            if (vo.isHot()){
                holder.id_hot.setVisibility(View.VISIBLE);
            }else{
                holder.id_hot.setVisibility(View.GONE);
            }
        }

        holder.id_vip.setText(vo.getGradename());


        

        return convertView;
    }

    private static final class ViewHolder {

        LinearLayout ll_bg;
        TextView id_vip;
        ImageView id_hot;
        ImageView id_choose;
    }

}
