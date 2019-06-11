/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-12-08 09:56:42
 */

package com.batsoft.model.module.exchange.vo;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.batsoft.model.module.exchange.CustomerAccount;

import lombok.Data;
import lombok.ToString;

/**
 *
 * <p>CoinFinanceVo</p>
 * @author: Bat Admin
 * @Date :  2017-12-08 09:56:42
 */
@ToString
public class CustomerAccountVo extends CustomerAccount {
	
	private static final long serialVersionUID = 1517986517483321737L;
	private  static  final DecimalFormat format = new DecimalFormat("0.000000");
	/**
	 * coin logo
	 */
	private String coinLogo;

	/**
	 * 是否允许充值
	 */
	private Integer allowRecharge;

	/**
	 * 是否允许提现
	 */
	private Integer allowWithdraw;

	/**
	 * 节点确认数
	 */
	private Integer confirm;


	private BigDecimal totalMoney;

	private String totalMoneyStr;

	private String availableStr;

	private String freezeStr;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCoinLogo() {
		return coinLogo;
	}

	public void setCoinLogo(String coinLogo) {
		this.coinLogo = coinLogo;
	}

	public Integer getAllowRecharge() {
		return allowRecharge;
	}

	public void setAllowRecharge(Integer allowRecharge) {
		this.allowRecharge = allowRecharge;
	}

	public Integer getAllowWithdraw() {
		return allowWithdraw;
	}

	public void setAllowWithdraw(Integer allowWithdraw) {
		this.allowWithdraw = allowWithdraw;
	}

	public Integer getConfirm() {
		return confirm;
	}

	public void setConfirm(Integer confirm) {
		this.confirm = confirm;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}



	@Override
	public BigDecimal getAvailable() {
		return new BigDecimal(format.format(super.getAvailable()));
	}

	public String getAvailableStr() {
		return format.format(super.getAvailable());
	}

	public String getFreezeStr() {
		return format.format(super.getFreeze());
	}
	
	@Override
	public BigDecimal getFreeze() {
		return new BigDecimal(format.format(super.getFreeze()));
	}

	public BigDecimal getTotalMoney() {
		return new BigDecimal(format.format((this.getAvailable() == null ? BigDecimal.ZERO : this.getAvailable()).add(this.getFreeze() == null ? BigDecimal.ZERO : this.getFreeze())));
	}

	public String getTotalMoneyStr() {
		return format.format((this.getAvailable() == null ? BigDecimal.ZERO : this.getAvailable()).add(this.getFreeze() == null ? BigDecimal.ZERO : this.getFreeze()));
	}
}
