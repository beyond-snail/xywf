package com.yywf.adapter;

////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//              佛祖保佑       永无BUG     永不修改                  //
//                                                                //
//          佛曰:                                                  //
//                  写字楼里写字间，写字间里程序员；                   //
//                  程序人员写程序，又拿程序换酒钱。                   //
//                  酒醒只在网上坐，酒醉还来网下眠；                   //
//                  酒醉酒醒日复日，网上网下年复年。                   //
//                  但愿老死电脑间，不愿鞠躬老板前；                   //
//                  奔驰宝马贵者趣，公交自行程序员。                   //
//                  别人笑我忒疯癫，我笑自己命太贱；                   //
//                  不见满街漂亮妹，哪个归得程序员？                   //
////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.PlanList;
import com.yywf.model.ZhangDan;

import java.util.List;


/**********************************************************
 *                                                        *
 *                  Created by wucongpeng on 2017/9/12.        *
 **********************************************************/


public class AdapterZhangDan extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ZhangDan> list;

    public AdapterZhangDan(Context context, List<ZhangDan> list) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_zd_list, parent, false);

//            holder.id_tv_amt = (TextView) convertView.findViewById(R.id.id_tv_amt);
//            holder.id_tv_count = (TextView) convertView.findViewById(R.id.id_tv_count);
//            holder.id_tv_fee = (TextView) convertView.findViewById(R.id.id_tv_fee);
//            holder.id_tv_sigle_amt = (TextView) convertView.findViewById(R.id.id_tv_sigle_amt);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ZhangDan vo = list.get(position);

//        holder.id_tv_amt.setText(StringUtils.formatIntMoney(vo.getAmt()));
//        holder.id_tv_count.setText(vo.getTime1());
//        holder.id_tv_fee.setText(StringUtils.formatIntMoney(vo.getFeeAmt()));
//        holder.id_tv_sigle_amt.setText(vo.getTime2());

        return convertView;
    }

    private static final class ViewHolder {
        TextView id_tv_amt;
        TextView id_tv_count;
        TextView id_tv_fee;
        TextView id_tv_sigle_amt;

    }
}
