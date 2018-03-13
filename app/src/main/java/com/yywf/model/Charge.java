package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class Charge implements Serializable {
    private String total;
    private String repay;
    private String consume;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRepay() {
        return repay;
    }

    public void setRepay(String repay) {
        this.repay = repay;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }
}
