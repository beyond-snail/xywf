package com.yywf.model;

import com.views.commonlysearchview.bean.BaseSearch;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class AgentInfo implements BaseSearch, Serializable {

//    private String icon;
//    private String memberName;
//    private String profitratio;
//    private String phone;
//    private int price;
    private int twoProfit;
    private long agentId;
    private int twoRebate;
    private String phone;
    private String typeName;
    private String name;
    private String picture;


    public int getTwoProfit() {
        return twoProfit;
    }

    public void setTwoProfit(int twoProfit) {
        this.twoProfit = twoProfit;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public int getTwoRebate() {
        return twoRebate;
    }

    public void setTwoRebate(int twoRebate) {
        this.twoRebate = twoRebate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String getSearchCondition() {
        StringBuilder searchStr = new StringBuilder();
        searchStr.append(name);
        return searchStr.toString();
    }
}
