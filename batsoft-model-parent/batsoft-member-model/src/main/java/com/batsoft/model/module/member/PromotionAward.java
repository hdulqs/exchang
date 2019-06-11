package com.batsoft.model.module.member;

import com.batsoft.model.BaseModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@ToString
@Table(name = "cms_promoter_award")
public class PromotionAward extends BaseModel implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 被推广人id
     */
    @Column(name = "promoter_id")
    private String promoterId;

    /**
     *被推广名字
     */
    @Column(name = "promoter_name")
    private String promoterName;
    /**
     * 推广人id
     */
    @Column(name = "promoter_parent_id")
    private String promoterParentId;

    /**
     *推广状态
     */
    @Column(name = "status")
    private Integer status;

     /**
     *奖励
     */
    @Column(name = "award")
    private BigDecimal award;



}
