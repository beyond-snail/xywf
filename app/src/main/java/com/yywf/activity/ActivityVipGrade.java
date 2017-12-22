package com.yywf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tool.utils.view.MyGridView;
import com.yywf.R;
import com.yywf.adapter.AdapterVipGrade;
import com.yywf.model.VipGrade;
import com.yywf.util.MyActivityManager;

import java.util.ArrayList;
import java.util.List;


public class ActivityVipGrade extends BaseActivity implements OnClickListener {


    private VipGrade vo;
    private List<VipGrade> list = new ArrayList<VipGrade>();

    private AdapterVipGrade adapter;



    private MyGridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_purchase_grade);
        MyActivityManager.getInstance().addActivity(this);
        initTitle("购买等级");
        if (findViewById(R.id.backBtn) != null) {
            findViewById(R.id.backBtn).setVisibility(View.VISIBLE);
        }
        if (findViewById(R.id.img_right) != null){
            findViewById(R.id.img_right).setVisibility(View.VISIBLE);
            findViewById(R.id.img_right).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent(mContext, ActivityReadWord.class));
                }
            });
        }
        initView();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initView() {


        for (int i = 0; i < 10; i++){
            VipGrade vipGrade = new VipGrade();
            vipGrade.setGrade(i+1);
            vipGrade.setDefault(false);
            vipGrade.setHot(false);
            list.add(vipGrade);
        }



        gridview = (MyGridView) findViewById(R.id.id_gridview);
        adapter = new AdapterVipGrade(mContext, list);
        gridview.setAdapter(adapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vo = list.get(position);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setDefault(false);
                }
                vo.setDefault(true);
                adapter.notifyDataSetChanged();


            }
        });

        textView(R.id.id_grade_msg).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_grade_msg:
                startActivity( new Intent(mContext, ActivityReadWord.class));
                break;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();


    }












}
