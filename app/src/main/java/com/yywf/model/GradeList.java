package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class GradeList implements Serializable {
    private int id;
    private String gradename;
    private int purchasePrice;
    private int gradegive;
    private int gradedemand;
    private int profitratio;
    private int cashback;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getGradegive() {
        return gradegive;
    }

    public void setGradegive(int gradegive) {
        this.gradegive = gradegive;
    }

    public int getGradedemand() {
        return gradedemand;
    }

    public void setGradedemand(int gradedemand) {
        this.gradedemand = gradedemand;
    }

    public int getProfitratio() {
        return profitratio;
    }

    public void setProfitratio(int profitratio) {
        this.profitratio = profitratio;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }
}
