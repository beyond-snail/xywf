package com.views.commonlysearchview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.R;
import com.views.commonlysearchview.bean.SanGuoBean;

import java.util.List;

/**
 * Created by junweiliu on 16/6/1.
 */
public class SGAdapter extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据源
     */
    private List<SanGuoBean> mDatas;


    /**
     * 构造函数
     *
     * @param context
     * @param datas
     */
    public SGAdapter(Context context, List<SanGuoBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (null == view) {
            vh = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.sg_item, null);
            vh.mNameTv = (TextView) view.findViewById(R.id.tv_sgname);
            vh.mPetNmaeTv = (TextView) view.findViewById(R.id.tv_sgpetname);
            vh.mDesTv = (TextView) view.findViewById(R.id.tv_sgdes);
            vh.mHeadImg = (ImageView) view.findViewById(R.id.iv_sghead);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        SanGuoBean bean = (SanGuoBean) getItem(position);
        if (null != bean) {
            vh.mNameTv.setText(bean.getSgName());
            vh.mDesTv.setText(bean.getSgDescribe());
            vh.mPetNmaeTv.setText(bean.getSgPetName());
            vh.mHeadImg.setImageResource(bean.getSgHeadBp());
        }
        return view;
    }


    /**
     * vh
     */
    class ViewHolder {
        /**
         * 姓名
         */
        TextView mNameTv;
        /**
         * 描述
         */
        TextView mDesTv;
        /**
         * 字
         */
        TextView mPetNmaeTv;
        /**
         * 头像
         */
        ImageView mHeadImg;
    }
}
