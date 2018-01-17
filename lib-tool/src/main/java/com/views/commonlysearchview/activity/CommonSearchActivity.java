package com.views.commonlysearchview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tool.R;
import com.views.commonlysearchview.adapter.SearchDemoAapterOne;
import com.views.commonlysearchview.adapter.SearchDemoAdapterTwo;
import com.views.commonlysearchview.bean.BaseSearch;
import com.views.commonlysearchview.bean.CommonSearchBean;
import com.views.commonlysearchview.bean.SearchDemoBeanOne;
import com.views.commonlysearchview.bean.SearchDemoBeanTwo;
import com.views.commonlysearchview.widget.CommolySearchView;

import java.util.List;


/**
 * Created by junweiliu on 17/3/29.
 */
public class CommonSearchActivity extends AppCompatActivity {
    /**
     * 搜索框
     */
    CommolySearchView<BaseSearch> mCsvShow;
    /**
     * 数据展示
     */
    ListView mLvShow;
    /**
     * 适配器
     */
    private BaseAdapter mAdapter;
    /**
     * 封装的搜索bean
     */
    private CommonSearchBean mCommonSearchBean = new CommonSearchBean();
    /**
     * 搜索类型(用于区分不同的搜索)
     */
    private int searchType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_search);
        initData();
        initView();
        initAdapter();
        initSearch();
    }


    /**
     * 初始化适配器,一般的扩展只需修改该方法即可
     */
    private void initAdapter() {
        if (1 == searchType) {              // 测试例子1
            // 适配器1
            mAdapter = new SearchDemoAapterOne(this, mCommonSearchBean.getList(), R.layout.item_search_one);
            // 点击事件
            mLvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(CommonSearchActivity.this, "点击的是:" + ((SearchDemoBeanOne) mCommonSearchBean.getList().get(position)).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (2 == searchType) {       // 测试例子2
            // 适配器2
            mAdapter = new SearchDemoAdapterTwo(this, mCommonSearchBean.getList());
            // 点击事件
            mLvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(CommonSearchActivity.this, "点击的是:" + ((SearchDemoBeanTwo) mCommonSearchBean.getList().get(position)).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mCommonSearchBean = (CommonSearchBean) getIntent().getSerializableExtra("seachBean");
        searchType = mCommonSearchBean.getSearchType();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mCsvShow = (CommolySearchView<BaseSearch>) findViewById(R.id.csv_show);
        mLvShow = (ListView) findViewById(R.id.lv_show);
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        mLvShow.setAdapter(mAdapter);
        // 设置数据源
        mCsvShow.setDatas(mCommonSearchBean.getList());
        // 设置适配器
        mCsvShow.setAdapter(mAdapter);
        // 设置搜索
        mCsvShow.setSearchDataListener(new CommolySearchView.SearchDatas<BaseSearch>() {
            @Override
            public List<BaseSearch> filterDatas(List<BaseSearch> datas, List<BaseSearch> filterdatas, String inputstr) {
                for (int i = 0; i < datas.size(); i++) {
                    // 筛选条件,如果有必要,在此做修改
                    if (datas.get(i).getSearchCondition().contains(inputstr)) {
                        filterdatas.add(datas.get(i));
                    }
                }
                return filterdatas;
            }
        });
    }
}
