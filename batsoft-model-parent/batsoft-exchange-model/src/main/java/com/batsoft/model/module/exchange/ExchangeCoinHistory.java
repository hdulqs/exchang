package com.batsoft.model.module.exchange;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.batsoft.model.BaseModel;

/**
 * 货币交易量统计
 * 
 */
@Entity
@Table(name = "exchange_coin_history")
public class ExchangeCoinHistory extends BaseModel {

	private static final long serialVersionUID = -8369797556758270896L;
	
	@Id
    @Column(name = "id")
    private String id;

    /**
     *coin_code = EXCHANGE 的时候 total是全球交易总额 fee是全球交易总额手续费
     */
    @Column(name = "coin_code")
    private String coinCode;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "time")
    private Date time;

    @Column(name = "fee")
    private BigDecimal fee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

}
