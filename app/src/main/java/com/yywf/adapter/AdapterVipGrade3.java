package com.yywf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.AdapterBase;
import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.VipGrade;

import java.util.ArrayList;
import java.util.List;

public class AdapterVipGrade3 extends AdapterBase<VipGrade> {


    private final static String TAG = "AdapterVipGrade";
    private List<VipGrade> data = new ArrayList<VipGrade>();

    private Context context;

    public AdapterVipGrade3(Context context, List<VipGrade> data) {
        super(data, context);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_vip_grade3, null);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
            holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            holder.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
            holder.ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final VipGrade vo = data.get(position);
        holder.tv_name.setText(vo.getGradename());
        holder.tv_name2.setText("返利"+ StringUtils.formatIntMoney(vo.getCashback())+"元, "+"分润万"+vo.getProfitratio()+", 且享受奖励金");

        if (position == 0) {
//            holder.ll_item.setBackgroundResource(R.drawable.yuanjiaojuxinger);
            holder.ll_left.setBackgroundResource(R.mipmap.cheng02);
            holder.ll_right.setBackgroundResource(R.mipmap.cheng01);
        }else{
//            holder.ll_item.setBackgroundResource(R.drawable.yuanjiaojuxingsan);
            holder.ll_left.setBackgroundResource(R.mipmap.lan02);
            holder.ll_right.setBackgroundResource(R.mipmap.lan01);
        }

        return convertView;
    }

    private static final class ViewHolder {
        LinearLayout ll_item;
        LinearLayout ll_left;
        LinearLayout ll_right;
        TextView tv_name;
        TextView tv_name2;
    }

}
