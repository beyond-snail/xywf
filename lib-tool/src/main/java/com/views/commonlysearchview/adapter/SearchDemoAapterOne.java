package com.views.commonlysearchview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tool.R;
import com.views.commonlysearchview.bean.SearchDemoBeanOne;

import java.util.List;

/**
 * Created by junweiliu on 17/3/29.
 */
public class SearchDemoAapterOne extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据源
     */
    private List<SearchDemoBeanOne> mDatas;
    /**
     * 布局文件ID
     */
    private final int mLayoutId;


    /**
     * 构造函数
     *
     * @param context
     * @param datas
     */
    public SearchDemoAapterOne(Context context, List<SearchDemoBeanOne> datas, int layoutId) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
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
            view = mInflater.inflate(mLayoutId, null);
            vh.mNameTv = (TextView) view.findViewById(R.id.tv_name);
            vh.mDesTv = (TextView) view.findViewById(R.id.tv_des);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        SearchDemoBeanOne bean = (SearchDemoBeanOne) getItem(position);
        if (null != bean) {
            vh.mNameTv.setText(bean.getName());
            vh.mDesTv.setText(bean.getDes());

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
    }
}