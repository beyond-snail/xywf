package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class BankCardInfo implements Serializable{
    private String id;
    private int bankIcon;
    private String bank_name;
    private int amt;
    private String zdDay;
    private String HkDay;
    private String card_num;
    private String member_name;
    private Long bankbillId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(int bankIcon) {
        this.bankIcon = bankIcon;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getZdDay() {
        return zdDay;
    }

    public void setZdDay(String zdDay) {
        this.zdDay = zdDay;
    }

    public String getHkDay() {
        return HkDay;
    }

    public void setHkDay(String hkDay) {
        HkDay = hkDay;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public Long getBankbillId() {
        return bankbillId;
    }

    public void setBankbillId(Long bankbillId) {
        this.bankbillId = bankbillId;
    }

    @Override
    public String toString() {
        return "BankCardInfo{" +
                "bankIcon=" + bankIcon +
                ", bank_name='" + bank_name + '\'' +
                ", amt=" + amt +
                ", zdDay=" + zdDay +
                ", HkDay=" + HkDay +
                ", card_num='" + card_num + '\'' +
                ", member_name='" + member_name + '\'' +
                '}';
    }
}
