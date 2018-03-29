package com.yywf.adapter;

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

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final VipGrade vo = data.get(position);
        holder.tv_name.setText(vo.getGradename());


        

        return convertView;
    }

    private static final class ViewHolder {

        TextView tv_name;
    }

}
