package com.views.commonlysearchview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tool.R;
import com.views.commonlysearchview.bean.SearchBean;

import java.util.List;

/**
 * Created by junweiliu on 16/5/31.
 */
public class SearchAdapter extends BaseAdapter {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 数据源
     */
    private List<SearchBean> mDatas;

    /**
     * 构造函数
     *
     * @param context
     * @param datas
     */
    public SearchAdapter(Context context, List<SearchBean> datas) {
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
            view = mInflater.inflate(R.layout.search_item, null);
            vh.mNameTv = (TextView) view.findViewById(R.id.tv_name);
            vh.mDesTv = (TextView) view.findViewById(R.id.tv_des);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        SearchBean bean = (SearchBean) getItem(position);
        if (null != bean) {
            vh.mNameTv.setText(bean.getSearchName());
            vh.mDesTv.setText(bean.getSearchDescribe());
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
