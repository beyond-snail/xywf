package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class ConsumeInfoList implements Serializable {
    private String stop_sch;
    private String consume_at;
    private String charge;
    private long consume_id;
    private String created_at;
    private String real_payment;
    private int exec_count;
    private String updated_at;
    private String exec_status;
    private int exec_at;
    private String payment;
    private String consume_status;
    private long plan_id;

    public String getStop_sch() {
        return stop_sch;
    }

    public void setStop_sch(String stop_sch) {
        this.stop_sch = stop_sch;
    }

    public String getConsume_at() {
        return consume_at;
    }

    public void setConsume_at(String consume_at) {
        this.consume_at = consume_at;
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

    public String getConsume_status() {
        return consume_status;
    }

    public void setConsume_status(String consume_status) {
        this.consume_status = consume_status;
    }

    public long getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(long plan_id) {
        this.plan_id = plan_id;
    }
}
