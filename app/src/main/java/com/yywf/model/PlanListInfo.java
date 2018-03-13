package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class PlanListInfo implements Serializable {

    private RepayInfoList repay;
    private ConsumeInfoList consume;

    public RepayInfoList getRepay() {
        return repay;
    }

    public void setRepay(RepayInfoList repay) {
        this.repay = repay;
    }

    public ConsumeInfoList getConsume() {
        return consume;
    }

    public void setConsume(ConsumeInfoList consume) {
        this.consume = consume;
    }
}
