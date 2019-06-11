package com.batsoft.service.module.exchange.provider;

import java.util.HashMap;

public class EntrustInfoProvider {
    public String findPageBySql(HashMap<String,Object> map){
        return "SELECT * FROM exchange_entrust_info"+map.get("date")+" where custimer_id="+map.get("customer_id")+"   ORDER BY create_time DESC limit "+map.get("from")+","+map.get("pageSize");
    }

    public String findPageBySqlTotal(HashMap<String,Object> map){
        return "SELECT count(*) FROM exchange_entrust_info"+map.get("date")+" where custimer_id="+map.get("customer_id")+"   ORDER BY create_time DESC ";
    }
}
