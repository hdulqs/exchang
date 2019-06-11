package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动类
 */
@Table(name = "exchange_action")
@Data
@Entity
@ApiModel(value = "ExchangeAction",description = "活动类")
public class ExchangeAction extends BaseModel {

	private static final long serialVersionUID = -8640648475289418226L;
	
	@Id
    @Column(name = "id")
    private String id;
    /**
     * 活动时间
     */
    @Column(name = "action_name")
    private String actionName;
    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "startTime",notes = "活动开始时间")
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 链接名字
     */
    private String linkName;

    /**
     * 介绍
     */
    private String introduce;
    /**
     * 活动结束时间
     */
    @Column(name = "end_time")
    private Date endTime;
    
    
    private Date leadTime;


    private Date actionDate;

    private BigDecimal buyLimitPrice;

    private BigDecimal sellLimitPrice;

    private Integer buyLimitAmout;

    private Integer sellLimitAmout;

    /**
     * 活动是否有效
     */
    @Column(name = "valide")
    private int valide;
    
    /**
     * 交易币
     */
    @Column(name = "coin_code")
    private String coinCode;
    
    /**
     * 定价币
     */
    @Column(name = "price_code")
    private String priceCode;

    @ApiModelProperty(value = "publicDate",notes = "公告日期")
    private Date publicDate;

    //公告链接
    @ApiModelProperty(value = "link",notes = "公告链接")
    private String link;

    //币的图片
    @ApiModelProperty(value = "coinPicture",notes = "币的图片")
    private String coinPicture;

    //距离开始时间秒数

    @ApiModelProperty(value = "startSeconds",notes = "距离开始时间秒数",dataType = "Long")
    private long startSeconds;
}
