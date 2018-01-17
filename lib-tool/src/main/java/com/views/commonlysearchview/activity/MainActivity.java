package com.views.commonlysearchview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tool.R;
import com.views.commonlysearchview.adapter.SGAdapter;
import com.views.commonlysearchview.adapter.SearchAdapter;
import com.views.commonlysearchview.bean.SanGuoBean;
import com.views.commonlysearchview.bean.SearchBean;
import com.views.commonlysearchview.widget.CommolySearchView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    /**
     * TAG
     */
    private static final String TAG = "MainActivity";
    /**
     * 通用搜索框
     */
    private CommolySearchView<SearchBean> mCommolySearchView;
    /**
     * 数据显示listview
     */
    private ListView mListView;
    /**
     * 数据源
     */
    private List<SearchBean> mDatas;
    /**
     * 适配器
     */
    private SearchAdapter adapter;
    //===========================三国部分===========================//
    /**
     * 三国通用搜索框
     */
    private CommolySearchView<SanGuoBean> mSGCommolySearchView;
    /**
     * 三国数据源
     */
    private List<SanGuoBean> mSGDatas;
    /**
     * 三国适配器
     */
    private SGAdapter sgAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initData();
//        initView();
//        initDataI();
//        initViewI();
    }

    /**
     * 初始化数据
//     */
//    private void initData() {
//        mDatas = new ArrayList<SearchBean>();
//        SearchBean bean1 = new SearchBean();
//        bean1.setSearchName("小明");
//        bean1.setSearchDescribe("一年级二班");
//        SearchBean bean2 = new SearchBean();
//        bean2.setSearchName("小白");
//        bean2.setSearchDescribe("二年级三班");
//        SearchBean bean3 = new SearchBean();
//        bean3.setSearchName("小张");
//        bean3.setSearchDescribe("三年级一班");
//        SearchBean bean4 = new SearchBean();
//        bean4.setSearchName("赵老师");
//        bean4.setSearchDescribe("一年级二班班主任");
//        SearchBean bean5 = new SearchBean();
//        bean5.setSearchName("王校长");
//        bean5.setSearchDescribe("学校校长");
//        SearchBean bean6 = new SearchBean();
//        bean6.setSearchName("陈主任");
//        bean6.setSearchDescribe("学校主任");
//        SearchBean bean7 = new SearchBean();
//        bean7.setSearchName("吴大爷");
//        bean7.setSearchDescribe("看门大爷");
//        mDatas.add(bean1);
//        mDatas.add(bean2);
//        mDatas.add(bean3);
//        mDatas.add(bean4);
//        mDatas.add(bean5);
//        mDatas.add(bean6);
//        mDatas.add(bean7);
//    }
//
//    /**
//     * 初始化控件
//     */
//    private void initView() {
//        mCommolySearchView = (CommolySearchView) findViewById(R.id.csv_show);
//        mListView = (ListView) findViewById(R.id.lv_show);
//        adapter = new SearchAdapter(this, mDatas);
//        mListView.setAdapter(adapter);
//        // 设置数据源
//        mCommolySearchView.setDatas(mDatas);
//        // 设置适配器
//        mCommolySearchView.setAdapter(adapter);
//        // 设置筛选数据
//        mCommolySearchView.setSearchDataListener(new CommolySearchView.SearchDatas<SearchBean>() {
//
//            @Override
//            public List<SearchBean> filterDatas(List<SearchBean> datas, List<SearchBean> filterdatas, String inputstr) {
//                Log.e(TAG, "filterDatas: " + inputstr);
//                for (int i = 0; i < datas.size(); i++) {
//                    // 筛选条件
//                    if ((datas.get(i).getSearchName()).contains(inputstr) || datas.get(i).getSearchDescribe().contains(inputstr)) {
//                        filterdatas.add(datas.get(i));
//                    }
//                }
//                return filterdatas;
//            }
//        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "当前点击的是" + mCommolySearchView.getFilterDatas().get(i).getSearchName(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 初始化数据
     */
//    private void initDataI() {
//        mSGDatas = new ArrayList<SanGuoBean>();
//        SanGuoBean sgbean1 = new SanGuoBean();
//        sgbean1.setSgName("刘备");
//        sgbean1.setSgPetName("玄德");
//        sgbean1.setSgHeadBp(R.drawable.lb);
//        sgbean1.setSgDescribe("刘备（161年－223年6月10日），字玄德，东汉末年幽州涿郡涿县（今河北省涿州市）人");
//        SanGuoBean sgbean2 = new SanGuoBean();
//        sgbean2.setSgName("关羽");
//        sgbean2.setSgPetName("云长");
//        sgbean2.setSgHeadBp(R.drawable.gy);
//        sgbean2.setSgDescribe("关羽（？－220年），本字长生，后改字云长，河东郡解良（今山西运城）人");
//        SanGuoBean sgbean3 = new SanGuoBean();
//        sgbean3.setSgName("张飞");
//        sgbean3.setSgPetName("翼德");
//        sgbean3.setSgHeadBp(R.drawable.zf);
//        sgbean3.setSgDescribe("张飞（？－221年），字益德[1]  ，幽州涿郡（今河北省保定市涿州市）人氏");
//        SanGuoBean sgbean4 = new SanGuoBean();
//        sgbean4.setSgName("赵云");
//        sgbean4.setSgPetName("子龙");
//        sgbean4.setSgHeadBp(R.drawable.zy);
//        sgbean4.setSgDescribe("赵云（？－229年），字子龙，常山真定（今河北省正定）人");
//        SanGuoBean sgbean5 = new SanGuoBean();
//        sgbean5.setSgName("马超");
//        sgbean5.setSgPetName("孟起");
//        sgbean5.setSgHeadBp(R.drawable.mc);
//        sgbean5.setSgDescribe("马超（176年－222年），字孟起，司隶部扶风郡茂陵（今陕西兴平）人");
//        SanGuoBean sgbean6 = new SanGuoBean();
//        sgbean6.setSgName("黄忠");
//        sgbean6.setSgPetName("汉升");
//        sgbean6.setSgHeadBp(R.drawable.hz);
//        sgbean6.setSgDescribe("黄忠（？－220年），字汉升（一作“汉叔”[1]  ），南阳（今河南南阳）人");
//        SanGuoBean sgbean7 = new SanGuoBean();
//        sgbean7.setSgName("张辽");
//        sgbean7.setSgPetName("文远");
//        sgbean7.setSgHeadBp(R.drawable.zl);
//        sgbean7.setSgDescribe("张辽（169年－222年），字文远，雁门马邑（今山西朔州）人");
//        mSGDatas.add(sgbean1);
//        mSGDatas.add(sgbean2);
//        mSGDatas.add(sgbean3);
//        mSGDatas.add(sgbean4);
//        mSGDatas.add(sgbean5);
//        mSGDatas.add(sgbean6);
//        mSGDatas.add(sgbean7);
//
//    }

    /**
     * 初始化控件
     */
//    private void initViewI() {
//        mSGCommolySearchView = (CommolySearchView) findViewById(R.id.csv_show);
//        mListView = (ListView) findViewById(R.id.lv_show);
//        sgAdapter = new SGAdapter(this, mSGDatas);
//        mListView.setAdapter(sgAdapter);
//        // 设置数据源
//        mSGCommolySearchView.setDatas(mSGDatas);
//        // 设置适配器
//        mSGCommolySearchView.setAdapter(sgAdapter);
//        // 设置筛选数据
//        mSGCommolySearchView.setSearchDataListener(new CommolySearchView.SearchDatas<SanGuoBean>() {
//            @Override
//            public List<SanGuoBean> filterDatas(List<SanGuoBean> datas, List<SanGuoBean> filterdatas, String inputstr) {
//                for (int i = 0; i < datas.size(); i++) {
//                    // 筛选条件
//                    if ((datas.get(i).getSgDescribe()).contains(inputstr) || datas.get(i).getSgName().contains(inputstr) || datas.get(i).getSgPetName().contains(inputstr)) {
//                        filterdatas.add(datas.get(i));
//                    }
//                }
//                return filterdatas;
//            }
//        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, mSGCommolySearchView.getFilterDatas().get(i).getSgName() + "字" + mSGCommolySearchView.getFilterDatas().get(i).getSgPetName() + "\n" + mSGCommolySearchView.getFilterDatas().get(i).getSgDescribe(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

}
