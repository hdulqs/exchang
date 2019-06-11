/**
* Copyright:    http://www.batsoft.cn
* @author:      Lucl
* @version:     V1.0
* @Date:        2017-10-28 18:03:41 
*/

package com.batsoft.model.module.loan;
import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import com.batsoft.utils.annotation.Money;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
        import java.math.BigDecimal;
        import java.util.Date;

/**
* 
* <p>Fund</p>
* @author: Lucl
* @Date :  2017-10-28 18:03:41 
*/
@Table(name="loan_fund")
@Entity
@Data
@ToString
public class Fund extends BaseModel {

            /**
            * 款项类型
            *principalLoans:本金放款
            */
            public static final String  FUNDTYPEprincipalLoans="principalLoans";
            /**
            * 款项类型
            *principal:本金
            */
            public static final String  FUNDTYPEprincipal="principal";
            /**
            * 款项类型
            *interest:利息
            */
            public static final String  FUNDTYPEinterest="interest";
            /**
            * 款项类型
            *service:服务费
            */
            public static final String  FUNDTYPEservice="service";
            /**
            * 款项类型
            *management:管理费
            */
            public static final String  FUNDTYPEmanagement="management";
            /**
            * 是否有效
            *0:有效
            */
                public static final Integer  VALID0=0;
            /**
            * 是否有效
            *1:无效
            */
                public static final Integer  VALID1=1;
            /**
            * 是否还清
            *0:否
            */
                public static final Integer  COMPLETED0=0;
            /**
            * 是否还清
            *1:是
            */
                public static final Integer  COMPLETED1=1;


    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 项目ID
    */

            @NotNull(message="项目ID不能为空")
            @Length(max = 64, message = "项目ID长度必须介于1和64之间")
            @Words(field = "项目ID", message = "项目ID包含敏感词")
    @Column(name="projectId")
    private String projectId;
    /**
    * 款项类型
    */

            @NotNull(message="款项类型不能为空")
            @Length(max = 50, message = "款项类型长度必须介于1和50之间")
            @Words(field = "款项类型", message = "款项类型包含敏感词")
    @Column(name="fundType")
    private String fundType;
    /**
    * 名称
    */

            @Length(max = 100, message = "名称长度必须介于1和100之间")
            @Words(field = "名称", message = "名称包含敏感词")
    @Column(name="fundName")
    private String fundName;
    /**
    * 当前期数
    */

    @Column(name="term")
    private Integer term;
    /**
    * 收入金额
    */

            @Money(point=2,message = "收入金额金额格式错误")
    @Column(name="incomeMoney")
    private BigDecimal incomeMoney;
    /**
    * 支出金额
    */

            @Money(point=2,message = "支出金额金额格式错误")
    @Column(name="payMoney")
    private BigDecimal payMoney;
    /**
    * 计息开始时间
    */

    @Column(name="startDate")
    private Date startDate;
    /**
    * 计息结束时间
    */

    @Column(name="intentDate")
    private Date intentDate;
    /**
    * 实际还款时间
    */

    @Column(name="factDate")
    private Date factDate;
    /**
    * 已还款金额
    */

            @Money(point=2,message = "已还款金额金额格式错误")
    @Column(name="afterMoney")
    private BigDecimal afterMoney;
    /**
    * 未还款金额
    */

            @Money(point=2,message = "未还款金额金额格式错误")
    @Column(name="notMoney")
    private BigDecimal notMoney;
    /**
    * 逾期金额
    */

            @Money(point=2,message = "逾期金额金额格式错误")
    @Column(name="overdueMoney")
    private BigDecimal overdueMoney;
    /**
    * 已偿还罚息
    */

            @Money(point=2,message = "已偿还罚息金额格式错误")
    @Column(name="afterOverdueMoney")
    private BigDecimal afterOverdueMoney;
    /**
    * 逾期天数
    */

    @Column(name="overdueDays")
    private Integer overdueDays;
    /**
    * 是否有效
    */

    @Column(name="valid")
    private Integer valid;
    /**
    * 是否还清
    */

    @Column(name="completed")
    private Integer completed;
    /**
    * 流水号
    */

            @Length(max = 50, message = "流水号长度必须介于1和50之间")
            @Words(field = "流水号", message = "流水号包含敏感词")
    @Column(name="requestNo")
    private String requestNo;
    /**
    * 公司ID
    */

            @Length(max = 64, message = "公司ID长度必须介于1和64之间")
            @Words(field = "公司ID", message = "公司ID包含敏感词")
    @Column(name="companyId")
    private String companyId;

}
