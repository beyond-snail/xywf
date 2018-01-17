package com.views.commonlysearchview.bean;

/**
 * Created by junweiliu on 16/6/1.
 */
public class SanGuoBean {
    /**
     * 名称
     */
    private String sgName;
    /**
     * 描述
     */
    private String sgDescribe;
    /**
     * 头像
     */
    private int sgHeadBp;
    /**
     * 字
     */
    private String sgPetName;

    public SanGuoBean() {
    }

    public SanGuoBean(String sgName, String sgDescribe, int sgHeadBp, String sgPetName) {
        this.sgName = sgName;
        this.sgDescribe = sgDescribe;
        this.sgHeadBp = sgHeadBp;
        this.sgPetName = sgPetName;
    }

    public String getSgName() {
        return sgName;
    }

    public void setSgName(String sgName) {
        this.sgName = sgName;
    }

    public String getSgDescribe() {
        return sgDescribe;
    }

    public void setSgDescribe(String sgDescribe) {
        this.sgDescribe = sgDescribe;
    }

    public int getSgHeadBp() {
        return sgHeadBp;
    }

    public void setSgHeadBp(int sgHeadBp) {
        this.sgHeadBp = sgHeadBp;
    }

    public String getSgPetName() {
        return sgPetName;
    }

    public void setSgPetName(String sgPetName) {
        this.sgPetName = sgPetName;
    }
}
