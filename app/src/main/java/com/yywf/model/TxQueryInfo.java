package com.yywf.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class TxQueryInfo implements Serializable {
    private int tranAmt;
    private int notranAmt;
    private String description;
    private String feeDescription;

    public int getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(int tranAmt) {
        this.tranAmt = tranAmt;
    }

    public int getNotranAmt() {
        return notranAmt;
    }

    public void setNotranAmt(int notranAmt) {
        this.notranAmt = notranAmt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }
}
