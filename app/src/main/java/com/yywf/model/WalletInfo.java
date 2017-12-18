package com.yywf.model;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class WalletInfo {
    private int totalAmt1;
    private int totalAmt2;

    private List<WalletListInfo> walletListInfo;




    public int getTotalAmt1() {
        return totalAmt1;
    }

    public void setTotalAmt1(int totalAmt1) {
        this.totalAmt1 = totalAmt1;
    }

    public int getTotalAmt2() {
        return totalAmt2;
    }

    public void setTotalAmt2(int totalAmt2) {
        this.totalAmt2 = totalAmt2;
    }

    public List<WalletListInfo> getWalletListInfo() {
        return walletListInfo;
    }

    public void setWalletListInfo(List<WalletListInfo> walletListInfo) {
        this.walletListInfo = walletListInfo;
    }
}
