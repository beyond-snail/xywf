package com.yywf.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class PlanInfo implements Serializable {
    private Charge  charge;
    private List<PlanListInfo> plan;
    private long plan_id;

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public List<PlanListInfo> getPlan() {
        return plan;
    }

    public void setPlan(List<PlanListInfo> plan) {
        this.plan = plan;
    }

    public long getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(long plan_id) {
        this.plan_id = plan_id;
    }
}
