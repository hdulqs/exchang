package com.batsoft.model.module.exchange.vo;

import lombok.Data;

/**
 * 手动充币
 * Created by Administrator on 2017/6/14.
 */
@Data
public class DepostPkData {
    String ids;
    String[] idsArr;
    String amount;

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
        this.idsArr = ids.split(",");
    }
}
