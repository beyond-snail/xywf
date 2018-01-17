package com.views.commonlysearchview.bean;

import java.io.Serializable;

/**
 * Created by junweiliu on 17/3/29.
 */
public class SearchDemoBeanOne implements BaseSearch, Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 描述
     */
    private String des;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 搜索条件
     *
     * @return
     */
    @Override
    public String getSearchCondition() {
        StringBuilder searchStr = new StringBuilder();
        searchStr.append(name);
        searchStr.append(des);
        return searchStr.toString();
    }
}
