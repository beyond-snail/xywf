package com.yywf.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class TransRecordInfo implements Serializable {
    private List<TransRecordList> comsume_list;

    public List<TransRecordList> getComsume_list() {
        return comsume_list;
    }

    public void setComsume_list(List<TransRecordList> comsume_list) {
        this.comsume_list = comsume_list;
    }
}
