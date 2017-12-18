package com.yywf.model;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class VoucherInfo {

    private int amt;
    private String useText;
    private String useTime;
    private int useStatus;

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getUseText() {
        return useText;
    }

    public void setUseText(String useText) {
        this.useText = useText;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }
}
