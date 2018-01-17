package com.views.commonlysearchview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.R;
import com.views.commonlysearchview.bean.SearchDemoBeanTwo;

import java.util.List;

/**
 * Created by junweiliu on 17/3/29.
 */
public class SearchDemoAdapterTwo extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据源
     */
    private List<SearchDemoBeanTwo> mDatas;


    /**
     * 构造函数
     *
     * @param context
     * @param datas
     */
    public SearchDemoAdapterTwo(Context context, List<SearchDemoBeanTwo> datas) {
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh = null;
        if (null == view) {
            vh = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.item_search_two, null);
            vh.mNameTv = (TextView) view.findViewById(R.id.tv_name);
            vh.mPhoneTv = (TextView) view.findViewById(R.id.tv_phone);
            vh.mEmilTv = (TextView) view.findViewById(R.id.tv_email);
            vh.mHeadImg = (ImageView) view.findViewById(R.id.iv_head);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        SearchDemoBeanTwo bean = (SearchDemoBeanTwo) getItem(position);
        if (null != bean) {
            vh.mNameTv.setText(bean.getName());
            vh.mPhoneTv.setText(bean.getPhone());
            vh.mEmilTv.setText(bean.getEmail());
            vh.mHeadImg.setImageResource(bean.getIconRes());
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
         * 邮箱
         */
        TextView mEmilTv;
        /**
         * 电话
         */
        TextView mPhoneTv;
        /**
         * 头像
         */
        ImageView mHeadImg;
    }
}
