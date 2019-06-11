/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月1日 下午4:32:01
 */
package com.batsoft.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2017年1月1日 下午4:32:01 
 */
@Data
@ToString
@ApiModel(value = "BaseModel",description = "返回数据结果类")
public class BaseModel implements Serializable {
    /**
     * 查詢全部数据
     */
    public static final Integer ALLPAGESIZE=0;

    /** 创建时间-数据库字段 */
    @Column(name = "create_time")
    private Date createTime;
    /** 修改时间-数据库字段 */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 0:未删除;1.已删除
     **/

    private int del;

    @Transient
    private Integer page = 1;

    @Transient
    private Integer rows = 10;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

	

	/* 删除标记（0：正常；1：删除；2：审核；） */
    /** 正常 */
    public static final Integer DEL_FLAG_NORMAL = 0;
    /** 删除 */
    public static final Integer DEL_FLAG_DELETE = 1;
    /** 审核 */
    public static final Integer DEL_FLAG_AUDIT = 2;


}

