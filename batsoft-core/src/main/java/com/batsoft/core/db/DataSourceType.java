/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月13日 下午3:53:49
 */
package com.batsoft.core.db;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月13日 下午3:53:49 
 */
import lombok.Getter;  

/** 
 * Created by Bat Admin on 2016/12/29.
 */  
public enum DataSourceType {
    /**
     * read 读 slave 从库
     */
    read("read", "slave"),
    /**
     * write 写 slave 主库
     */
    write("write", "master");  
    @Getter  
    private String type;  
    @Getter  
    private String name;  
  
    DataSourceType(String type, String name) {  
        this.type = type;  
        this.name = name;  
    }  
}  
