package com.batsoft.core.model;

import java.util.Date;

/**
 * 用于批量操作对象传递 主键数组
 * Created by Administrator on 2017/6/14.
 */
public class PksData {
    String ids;
    String[] idsArr;

    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
        setIdsArr(ids);
    }

    public String[] getIdsArr() {
        return idsArr;
    }

    public void setIdsArr(String ids) {
        if(ids != null && ids.contains(",")){
            this.idsArr = ids.split(",");
        } else {
            this.idsArr = new String[]{ids};
        }
    }
}
