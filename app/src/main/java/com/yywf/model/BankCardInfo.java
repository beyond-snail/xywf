package com.yywf.model;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class BankCardInfo {

    private int bankIcon;
    private String bankName;
    private int amt;
    private int zdDay;
    private int HkDay;
    private String wH;
    private String name;

    public int getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(int bankIcon) {
        this.bankIcon = bankIcon;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int getZdDay() {
        return zdDay;
    }

    public void setZdDay(int zdDay) {
        this.zdDay = zdDay;
    }

    public int getHkDay() {
        return HkDay;
    }

    public void setHkDay(int hkDay) {
        HkDay = hkDay;
    }

    public String getwH() {
        return wH;
    }

    public void setwH(String wH) {
        this.wH = wH;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
