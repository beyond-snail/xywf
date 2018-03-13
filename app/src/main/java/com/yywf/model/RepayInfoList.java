package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class RepayInfoList implements Serializable {
    private String stop_sch;
    private String charge;
    private long consume_id;
    private String created_at;
    private long repay_id;
    private String repay_status;
    private String real_payment;
    private int exec_count;
    private String repay_at;
    private String updated_at;
    private String exec_status;
    private int exec_at;
    private String payment;


    public String getStop_sch() {
        return stop_sch;
    }

    public void setStop_sch(String stop_sch) {
        this.stop_sch = stop_sch;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public long getConsume_id() {
        return consume_id;
    }

    public void setConsume_id(long consume_id) {
        this.consume_id = consume_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getRepay_id() {
        return repay_id;
    }

    public void setRepay_id(long repay_id) {
        this.repay_id = repay_id;
    }

    public String getRepay_status() {
        return repay_status;
    }

    public void setRepay_status(String repay_status) {
        this.repay_status = repay_status;
    }

    public String getReal_payment() {
        return real_payment;
    }

    public void setReal_payment(String real_payment) {
        this.real_payment = real_payment;
    }

    public int getExec_count() {
        return exec_count;
    }

    public void setExec_count(int exec_count) {
        this.exec_count = exec_count;
    }

    public String getRepay_at() {
        return repay_at;
    }

    public void setRepay_at(String repay_at) {
        this.repay_at = repay_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getExec_status() {
        return exec_status;
    }

    public void setExec_status(String exec_status) {
        this.exec_status = exec_status;
    }

    public int getExec_at() {
        return exec_at;
    }

    public void setExec_at(int exec_at) {
        this.exec_at = exec_at;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
