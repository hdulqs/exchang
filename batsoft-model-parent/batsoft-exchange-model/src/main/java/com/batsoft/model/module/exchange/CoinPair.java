/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:20:19
 */

package com.batsoft.model.module.exchange;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 交易对配置
 * 
 */
@Entity
@Data
@ToString
@Table(name = "exchange_coin_pair")
public class CoinPair extends BaseModel {

	/**
	 * 状态 0:禁止交易
	 */
	public static final Integer STATUS0 = 0;
	/**
	 * 状态 1:开启交易
	 */
	public static final Integer STATUS1 = 1;
	/**
	 * 是否推荐 0:不推荐
	 */
	public static final Integer RECOMENT0 = 0;
	/**
	 * 是否推荐 1:推荐
	 */
	public static final Integer RECOMENT1 = 1;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private String id;
	
	/**
	 * 交易币代码
	 */
	@NotNull(message = "交易币代码不能为空")
	@Length(max = 50, message = "交易币代码长度必须介于1和50之间")
	@Words(field = "交易币代码", message = "交易币代码包含敏感词")
	@Column(name = "trade_coin_code")
	private String tradeCoinCode;
	
	/**
	 * 定价币代码
	 */
	@NotNull(message = "定价币代码不能为空")
	@Length(max = 50, message = "定价币代码长度必须介于1和50之间")
	@Words(field = "定价币代码", message = "定价币代码包含敏感词")
	@Column(name = "pricing_coin_code")
	private String pricingCoinCode;
	
	/**
	 * 交易币logo
	 */
	// @NotNull(message = "交易币logo不能为空")
	// @Length(max = 100, message = "交易币logo长度必须介于1和100之间")
	@Words(field = "交易币logo", message = "交易币logo包含敏感词")
	@Column(name = "trade_coin_logo")
	private String tradeCoinLogo;
	
	/**
	 * 状态
	 */
	@NotNull(message = "状态不能为空")
	@Column(name = "status")
	private Integer status;
	
	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 是否推荐
	 */
	@Column(name = "recommend")
	private Integer recommend;
	
	/**
	 * 挂单数量最小位数
	 */
	@Column(name = "amt_decimal")
	private Integer amtDecimal;
	
	/**
	 * 定价币单价最小位数
	 */
	@Column(name = "price_decimal")
	private Integer priceDecimal;
	
	/**
	 * 交易总额最小位数
	 */
	@Column(name = "amount_decimal")
	private Integer amountDecimal;

	/**
	 * 发行价
	 */
	@Column(name = "open_price")
	private BigDecimal openPrice;

}
