/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:21:24 
*/

package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户货币账户订单记录
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_customer_account_order")
public class CustomerAccountOrder extends BaseModel {

	private static final long serialVersionUID = -5965641620938041363L;
	
	/**
	 * 方向 0:支出
	 */
	public static final Integer DIRECTION0 = 0;
	/**
	 * 方向 1:收入
	 */
	public static final Integer DIRECTION1 = 1;
	/**
	 * 操作类型 0:充值
	 */
	public static final String OPERATIONTYPE0 = "0";
	/**
	 * 操作类型 1:提现
	 */
	public static final String OPERATIONTYPE1 = "1";
	/**
	 * 操作类型 2:转入
	 */
	public static final String OPERATIONTYPE2 = "2";
	/**
	 * 操作类型 3:转出
	 */
	public static final String OPERATIONTYPE3 = "3";
	/**
	 * 操作类型 4:系统
	 */
	public static final String OPERATIONTYPE4 = "4";
	/**
	 * 订单状态 0:进行中
	 */
	public static final Integer OPEATIONSTATE0 = 0;
	/**
	 * 订单状态 1:已完成
	 */
	public static final Integer OPEATIONSTATE1 = 1;
	/**
	 * 订单状态 2:失败
	 */
	public static final Integer OPEATIONSTATE2 = 2;

	/**
	 * id
	 */

	@Id
	@Column(name = "id")
	private String id;
	/**
	 * 货币账户id
	 */

	@Length(max = 50, message = "货币账户id长度必须介于1和50之间")
	@Words(field = "货币账户id", message = "货币账户id包含敏感词")
	@Column(name = "account_id")
	private String accountId;
	/**
	 * 客户id
	 */

	@Length(max = 50, message = "客户id长度必须介于1和50之间")
	@Words(field = "客户id", message = "客户id包含敏感词")
	@Column(name = "customer_id")
	private String customerId;
	/**
	 * 账户账号
	 */

	@Length(max = 50, message = "账户账号长度必须介于1和50之间")
	@Words(field = "账户账号", message = "账户账号包含敏感词")
	@Column(name = "account_number")
	private String accountNumber;
	/**
	 * 币代码
	 */

	@Length(max = 50, message = "币代码长度必须介于1和50之间")
	@Words(field = "币代码", message = "币代码包含敏感词")
	@Column(name = "coin_code")
	private String coinCode;
	/**
	 * 方向
	 */

	@Column(name = "direction")
	private Integer direction;
	/**
	 * 操作类型
	 */

	@Length(max = 10, message = "操作类型长度必须介于1和10之间")
	@Words(field = "操作类型", message = "操作类型包含敏感词")
	@Column(name = "operation_type")
	private String operationType;
	/**
	 * 订单金额
	 */

	@Money(point = 15, message = "订单金额金额格式错误")
	@Column(name = "opeation_money")
	private BigDecimal opeationMoney;
	/**
	 * 交易时间
	 */

	@Column(name = "trade_time")
	private Date tradeTime;
	/**
	 * 订单状态
	 */

	@Column(name = "opeation_state")
	private Integer opeationState;
	/**
	 * 订单说明
	 */

	@Length(max = 50, message = "订单说明长度必须介于1和50之间")
	@Words(field = "订单说明", message = "订单说明包含敏感词")
	@Column(name = "remark")
	private String remark;

}
