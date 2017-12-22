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

public class AdapterGradeMsg extends AdapterBase<String> {


    private final static String TAG = "AdapterGradeMsg";
    private List<String> data = new ArrayList<String>();

    private Context context;

    public AdapterGradeMsg(Context context, List<String> data) {
        super(data, context);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_grade_msg, null);

            holder.id_vip = (TextView) convertView.findViewById(R.id.id_vip);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.id_vip.setText(data.get(position));


        

        return convertView;
    }

    private static final class ViewHolder {


        TextView id_vip;
    }

}
