/**
 * Copyright:   领航者
 * @author:      Bat Admin
 * @version:     V1.0 
 * @Date:        2018-03-28 23:35:05 
 */
package com.batsoft.service.module.exchange.trade.model;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class TradeEntrustInfo implements Serializable {

	private static final long serialVersionUID = 2678057571917465223L;
	
	//卖方货币账户id
	private String sellAccountId;
	
	//卖方客户id
	private String sellCustomerId;  
	
	//卖方委托单号
	private String sellOrderId;

	//买方货币账户id
	private String buyAccountId;  
	
	//买方客户id
	private String buyCustomerId;  
	
	//买方委托单号
	private String buyOrderId;

	//主动买，还是主动卖;   buy   sell
	private String type;

	//交易币代码
	private String tradeCoinCode;  

	//定价币代码
	private String pricingCoinCode;  

	//成交价格
	private BigDecimal entrustPrice;  

	//成交数量
	private BigDecimal entrustAmout;  

	//买费率
	private BigDecimal buyRate;

	//卖费率
	private BigDecimal sellRate;

	//买手续费
	private BigDecimal buyFee;

	//卖手续费
	private BigDecimal sellFee;

    //成交时间
	private Date entrustTime;
	
	public String getSellOrderId() {
		return sellOrderId;
	}

	public void setSellOrderId(String sellOrderId) {
		this.sellOrderId = sellOrderId;
	}

	public String getBuyOrderId() {
		return buyOrderId;
	}

	public void setBuyOrderId(String buyOrderId) {
		this.buyOrderId = buyOrderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>卖方货币账户id</p>
	 * @author:  Bat Admin
	 * @return:  String 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public String getSellAccountId() {
		return sellAccountId;
	}
	
	/**
	 * <p>卖方货币账户id</p>
	 * @author:  Bat Admin
	 * @param:   @param sellAccountId
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setSellAccountId(String sellAccountId) {
		this.sellAccountId = sellAccountId;
	}
	
	
	/**
	 * <p>买方货币账户id</p>
	 * @author:  Bat Admin
	 * @return:  String 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public String getBuyAccountId() {
		return buyAccountId;
	}
	
	/**
	 * <p>买方货币账户id</p>
	 * @author:  Bat Admin
	 * @param:   @param buyAccountId
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setBuyAccountId(String buyAccountId) {
		this.buyAccountId = buyAccountId;
	}
	
	
	/**
	 * <p>卖方客户id</p>
	 * @author:  Bat Admin
	 * @return:  String 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public String getSellCustomerId() {
		return sellCustomerId;
	}
	
	/**
	 * <p>卖方客户id</p>
	 * @author:  Bat Admin
	 * @param:   @param sellCustomerId
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setSellCustomerId(String sellCustomerId) {
		this.sellCustomerId = sellCustomerId;
	}
	
	
	/**
	 * <p>买方客户id</p>
	 * @author:  Bat Admin
	 * @return:  String 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public String getBuyCustomerId() {
		return buyCustomerId;
	}
	
	/**
	 * <p>买方客户id</p>
	 * @author:  Bat Admin
	 * @param:   @param buyCustomerId
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setBuyCustomerId(String buyCustomerId) {
		this.buyCustomerId = buyCustomerId;
	}

	public String getTradeCoinCode() {
		return tradeCoinCode;
	}

	public void setTradeCoinCode(String tradeCoinCode) {
		this.tradeCoinCode = tradeCoinCode;
	}

	public String getPricingCoinCode() {
		return pricingCoinCode;
	}

	public void setPricingCoinCode(String pricingCoinCode) {
		this.pricingCoinCode = pricingCoinCode;
	}

	/**
	 * <p>成交价格</p>
	 * @author:  Bat Admin
	 * @return:  BigDecimal 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public BigDecimal getEntrustPrice() {
		return entrustPrice;
	}
	
	/**
	 * <p>成交价格</p>
	 * @author:  Bat Admin
	 * @param:   @param entrustPrice
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setEntrustPrice(BigDecimal entrustPrice) {
		this.entrustPrice = entrustPrice;
	}
	
	
	/**
	 * <p>成交数量</p>
	 * @author:  Bat Admin
	 * @return:  BigDecimal 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public BigDecimal getEntrustAmout() {
		return entrustAmout;
	}
	
	/**
	 * <p>成交数量</p>
	 * @author:  Bat Admin
	 * @param:   @param entrustAmout
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setEntrustAmout(BigDecimal entrustAmout) {
		this.entrustAmout = entrustAmout;
	}
	
	
	/**
	 * <p>成交时间</p>
	 * @author:  Bat Admin
	 * @return:  Date 
	 * @Date :   2018-03-28 23:35:05    
	 */
	public Date getEntrustTime() {
		return entrustTime;
	}
	
	/**
	 * <p>成交时间</p>
	 * @author:  Bat Admin
	 * @param:   @param entrustTime
	 * @return:  void 
	 * @Date :   2018-03-28 23:35:05   
	 */
	public void setEntrustTime(Date entrustTime) {
		this.entrustTime = entrustTime;
	}


	public BigDecimal getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(BigDecimal buyRate) {
		this.buyRate = buyRate;
	}

	public BigDecimal getSellRate() {
		return sellRate;
	}

	public void setSellRate(BigDecimal sellRate) {
		this.sellRate = sellRate;
	}


	public BigDecimal getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(BigDecimal buyFee) {
		this.buyFee = buyFee;
	}

	public BigDecimal getSellFee() {
		return sellFee;
	}

	public void setSellFee(BigDecimal sellFee) {
		this.sellFee = sellFee;
	}

	@Override
	public String toString() {
		return "TradeEntrustInfo [sellAccountId=" + sellAccountId + ", sellCustomerId=" + sellCustomerId
				+ ", sellOrderId=" + sellOrderId + ", buyAccountId=" + buyAccountId + ", buyCustomerId=" + buyCustomerId
				+ ", buyOrderId=" + buyOrderId + ", type=" + type + ", tradeCoinCode=" + tradeCoinCode
				+ ", pricingCoinCode=" + pricingCoinCode + ", entrustPrice=" + entrustPrice + ", entrustAmout="
				+ entrustAmout + ", buyRate=" + buyRate + ", sellRate=" + sellRate + ", buyFee=" + buyFee + ", sellFee="
				+ sellFee + ", entrustTime=" + entrustTime + "]";
	}
	
}
