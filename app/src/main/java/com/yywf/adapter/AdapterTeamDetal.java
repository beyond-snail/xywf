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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tool.utils.utils.StringUtils;
import com.yywf.R;
import com.yywf.model.TeamInfo;
import com.yywf.model.WalletListInfo;

import java.util.List;


/**********************************************************
 *                                                        *
 *                  Created by wucongpeng on 2017/9/12.        *
 **********************************************************/


public class AdapterTeamDetal extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TeamInfo> list;

    public AdapterTeamDetal(Context context, List<TeamInfo> list) {
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
            convertView = mInflater.inflate(R.layout.item_myteam_detail, parent, false);
            holder.id_tv_img = convertView.findViewById(R.id.id_tv_img);
            holder.id_tv_amt = (TextView) convertView.findViewById(R.id.id_tv_amt);
            holder.id_tv_name = (TextView) convertView.findViewById(R.id.id_tv_name);
            holder.id_tv_phone = (TextView) convertView.findViewById(R.id.id_tv_phone);
            holder.id_tv_activate = (TextView) convertView.findViewById(R.id.id_tv_activate);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeamInfo vo = list.get(position);

        ImageLoader.getInstance().displayImage(vo.getImg(), holder.id_tv_img);
        holder.id_tv_amt.setText(StringUtils.formatIntMoney(vo.getAmt()));
        holder.id_tv_name.setText(StringUtils.isBlank(vo.getName()) ? "" : vo.getName());
        holder.id_tv_phone.setText(StringUtils.isBlank(vo.getName()) ? "" : vo.getPhone());
        if (vo.isActivate()){
            holder.id_tv_activate.setText("已激活");
        }else{
            holder.id_tv_activate.setText("未激活");
        }


        return convertView;
    }

    private static final class ViewHolder {
        ImageView id_tv_img;
        TextView id_tv_name;
        TextView id_tv_phone;
        TextView id_tv_activate;
        TextView id_tv_amt;

    }
}
