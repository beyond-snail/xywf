package com.views.commonlysearchview.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by junweiliu on 17/3/29.
 * 封装的搜索bean用于传递和使用不同类型的数据源
 */
public class CommonSearchBean<T> implements Serializable {
    /**
     * 数据源
     */
    private List<T> list;
    /**
     * 搜索类型  1代表搜索测试1,2代表搜索测试2
     */
    private int searchType;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
}
