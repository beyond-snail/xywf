package com.views.commonlysearchview.bean;

import java.io.Serializable;

/**
 * Created by junweiliu on 17/3/29.
 */
public class SearchDemoBeanTwo implements BaseSearch, Serializable {
    /**
     * 头像资源
     */
    private int iconRes;
    /**
     * 头像地址
     */
    private int iconUrl;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(int iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        searchStr.append(phone);
        searchStr.append(email);
        return searchStr.toString();
    }
}
