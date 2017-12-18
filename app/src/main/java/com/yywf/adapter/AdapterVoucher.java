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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.VoucherInfo;
import com.yywf.model.ZhangDan;

import java.util.List;


/**********************************************************
 *                                                        *
 *                  Created by wucongpeng on 2017/9/12.        *
 **********************************************************/


public class AdapterVoucher extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<VoucherInfo> list;

    public AdapterVoucher(Context context, List<VoucherInfo> list) {
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
            convertView = mInflater.inflate(R.layout.item_voucher, parent, false);
            holder.ll_title_bg = convertView.findViewById(R.id.ll_title_bg);
            holder.id_v_title = (TextView) convertView.findViewById(R.id.id_v_title);
            holder.id_v_text = (TextView) convertView.findViewById(R.id.id_v_text);
            holder.id_v_time = (TextView) convertView.findViewById(R.id.id_v_time);
            holder.id_v_img = (ImageView) convertView.findViewById(R.id.id_v_img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VoucherInfo vo = list.get(position);

        holder.id_v_title.setText(StringUtils.formatIntMoney(vo.getAmt()));
        holder.id_v_text.setText("使用规则："+vo.getUseText());
        holder.id_v_time.setText("截止时间："+vo.getUseTime());
        if (vo.getUseStatus() == 1) { //待使用
            holder.id_v_img.setBackgroundResource(R.drawable.icon_voucher_using);
            holder.ll_title_bg.setBackgroundResource(R.drawable.bar_voucher_using);
        }else if (vo.getUseStatus() == 2){ //已使用
            holder.id_v_img.setBackgroundResource(R.drawable.icon_voucher_used);
            holder.ll_title_bg.setBackgroundResource(R.drawable.bar_voucher_used);
        }else if (vo.getUseStatus() == 3){ //已过期
            holder.id_v_img.setBackgroundResource(R.drawable.icon_voucher_past);
            holder.ll_title_bg.setBackgroundResource(R.drawable.bar_voucher_past);
        }

        return convertView;
    }

    private static final class ViewHolder {
        LinearLayout ll_title_bg;
        TextView id_v_title;
        TextView id_v_text;
        TextView id_v_time;
        ImageView id_v_img;

    }
}
