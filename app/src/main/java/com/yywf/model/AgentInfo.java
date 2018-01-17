package com.yywf.model;

import com.views.commonlysearchview.bean.BaseSearch;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class AgentInfo implements BaseSearch, Serializable {

    private String icon;
    private String memberName;
    private String profitratio;
    private String phone;
    private int price;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getProfitratio() {
        return profitratio;
    }

    public void setProfitratio(String profitratio) {
        this.profitratio = profitratio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String getSearchCondition() {
        StringBuilder searchStr = new StringBuilder();
        searchStr.append(memberName);
        return searchStr.toString();
    }
}
