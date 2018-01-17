package com.views.commonlysearchview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tool.R;
import com.views.commonlysearchview.bean.CommonSearchBean;
import com.views.commonlysearchview.bean.SearchDemoBeanOne;
import com.views.commonlysearchview.bean.SearchDemoBeanTwo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junweiliu on 17/3/30.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 老版本按钮
     */
    private Button oldVersion;
    /**
     * 例子1
     */
    private Button demoOne;
    /**
     * 例子2
     */
    private Button demoTwo;
    /**
     * 测试公共搜索数据1
     */
    private List<SearchDemoBeanOne> mDatas = new ArrayList<>();
    /**
     * 测试公共搜索数据2
     */
    private List<SearchDemoBeanTwo> mDatasTwo = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initSearchData();
        initView();
    }

    private void initView() {
        oldVersion = (Button) findViewById(R.id.btn_test_search_old);
        demoOne = (Button) findViewById(R.id.btn_test_search_one);
        demoTwo = (Button) findViewById(R.id.btn_test_search_two);
        oldVersion.setOnClickListener(this);
        demoOne.setOnClickListener(this);
        demoTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // 封装好的搜索bean
        CommonSearchBean commonSearchBean = new CommonSearchBean();
        Intent intent = new Intent();
        if (id == R.id.btn_test_search_old) {
            intent.setClass(this, MainActivity.class);

        } else if (id == R.id.btn_test_search_one) {
            intent.setClass(this, CommonSearchActivity.class);
            commonSearchBean.setList(mDatas);
            commonSearchBean.setSearchType(1);

        } else if (id == R.id.btn_test_search_two) {
            intent.setClass(this, CommonSearchActivity.class);
            commonSearchBean.setList(mDatasTwo);
            commonSearchBean.setSearchType(2);

        }
        intent.putExtra("seachBean", (Serializable) commonSearchBean);
        startActivity(intent);
    }

    /**
     * 初始化搜索数据
     *
     */
    private void initSearchData() {
        SearchDemoBeanOne demo1 = new SearchDemoBeanOne();
        demo1.setName("熊大");
        demo1.setDes("智慧无限");
        SearchDemoBeanOne demo2 = new SearchDemoBeanOne();
        demo2.setName("熊二");
        demo2.setDes("力大无穷");
        SearchDemoBeanOne demo3 = new SearchDemoBeanOne();
        demo3.setName("张三");
        demo3.setDes("一个好孩子");
        SearchDemoBeanOne demo4 = new SearchDemoBeanOne();
        demo4.setName("李四");
        demo4.setDes("一个坏孩子");
        SearchDemoBeanOne demo5 = new SearchDemoBeanOne();
        demo5.setName("赵四");
        demo5.setDes("东北舞王");
        mDatas.add(demo1);
        mDatas.add(demo2);
        mDatas.add(demo3);
        mDatas.add(demo4);
        mDatas.add(demo5);
        SearchDemoBeanTwo demoTwo1 = new SearchDemoBeanTwo();
        demoTwo1.setName("小红");
        demoTwo1.setEmail("xiaohong@xx.com");
        demoTwo1.setPhone("15011111111");
        demoTwo1.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo2 = new SearchDemoBeanTwo();
        demoTwo2.setName("小蓝");
        demoTwo2.setEmail("xiaolan@xx.com");
        demoTwo2.setPhone("15022222222");
        demoTwo2.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo3 = new SearchDemoBeanTwo();
        demoTwo3.setName("小绿");
        demoTwo3.setEmail("xiaolv@xx.com");
        demoTwo3.setPhone("15033333333");
        demoTwo3.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo4 = new SearchDemoBeanTwo();
        demoTwo4.setName("小紫");
        demoTwo4.setEmail("xiaozi@xx.com");
        demoTwo4.setPhone("15044444444");
        demoTwo4.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo5 = new SearchDemoBeanTwo();
        demoTwo5.setName("小黄");
        demoTwo5.setEmail("xiaohuang@xx.com");
        demoTwo5.setPhone("15055555555");
        demoTwo5.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo6 = new SearchDemoBeanTwo();
        demoTwo6.setName("小白");
        demoTwo6.setEmail("xiaobai@xx.com");
        demoTwo6.setPhone("15066666666");
        demoTwo6.setIconRes(R.mipmap.ic_launcher);
        SearchDemoBeanTwo demoTwo7 = new SearchDemoBeanTwo();
        demoTwo7.setName("小黑");
        demoTwo7.setEmail("xiaobai@xx.com");
        demoTwo7.setPhone("15077777777");
        demoTwo7.setIconRes(R.mipmap.ic_launcher);
        mDatasTwo.add(demoTwo1);
        mDatasTwo.add(demoTwo2);
        mDatasTwo.add(demoTwo3);
        mDatasTwo.add(demoTwo4);
        mDatasTwo.add(demoTwo5);
        mDatasTwo.add(demoTwo6);
        mDatasTwo.add(demoTwo7);

    }
}
