package com.batsoft.job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TablesJob {

    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;

    @Value(value = "${spring.datasource.url}")
    private String url;

    @Value(value = "${spring.datasource.username}")
    private String userName;

    @Value(value = "${spring.datasource.password}")
    private String password;

    private String[] createTables= {"exchange_customer_account_freeze","exchange_customer_account_record","exchange_entrust_history","exchange_entrust_info"};

    @Scheduled(cron = "0 */1 * * * ?")
    void productLastMonthTables() throws SQLException, ClassNotFoundException{
        //判断表是否存在，不存在，那么生成表，表的名字是当前天加一天的下个月的每天生成一张表。
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        //生成每天的表
        calendar.add(Calendar.DAY_OF_MONTH,1);
        StringBuffer nextDay = new StringBuffer() ;
        int year = calendar.get(Calendar.YEAR);
        nextDay.append(year);
        int month = calendar.get(Calendar.MONTH)+1;
        if(month < 10){
            nextDay.append("_0").append(month);
        }else{
            nextDay.append("_").append(month);
        }
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(day < 10){
            nextDay.append("_0").append(day);
        }else{
            nextDay.append("_").append(day);
        }
        Class.forName(driver);
        Connection connection = null;
        connection = DriverManager.getConnection(url,userName,password);
        Statement statement = connection.createStatement();

        for (String tablename : createTables){
            String  createTableName = tablename + "_" + nextDay;
            ResultSet rs = connection.getMetaData().getTables(null, null, createTableName, null);
            if(rs.next()){
                continue;
            }else{
               String createSql = "create table "+createTableName+" like "+tablename;
               statement.executeUpdate(createSql);
            }
        }
        statement.close();
        connection.close();
    }

}
