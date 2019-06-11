package com.batsoft.service.module.exchange.trade.service.impl;

import com.batsoft.service.module.exchange.trade.service.TradeSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service("tradeSqlService")
public class TradeSqlServiceImpl implements TradeSqlService {
    @Autowired
    private  JdbcTemplate jdbcTemplate;
    @Override
    public void messageToSql(String sql) {
        try {
            sql = sql.substring(0, sql.length() - 1);
            String[] arrSql = sql.split(";");
            jdbcTemplate.batchUpdate(arrSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
