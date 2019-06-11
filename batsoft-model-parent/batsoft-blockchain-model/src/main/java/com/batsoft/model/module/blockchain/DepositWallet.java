/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-06-03 10:51:01
 */

package com.batsoft.model.module.blockchain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Money;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 提币钱包
 * 
 */
@Entity
@ToString
@Table(name = "blockchain_deposit_wallet")
public class DepositWallet extends BaseModel {

	private static final long serialVersionUID = 2838722210994154084L;
	
	/**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 钱包类型
     */
    @Length(max = 50, message = "钱包类型长度必须介于1和50之间")
    @Words(field = "钱包类型", message = "钱包类型包含敏感词")
    @Column(name = "wallet_type")
    private String walletType;
    
    /**
     * 币代码
     */
    @Length(max = 50, message = "币代码长度必须介于1和50之间")
    @Words(field = "币代码", message = "币代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;
    
    /**
     * 提币地址
     */
    @NotNull(message = "提币地址不能为空")
    @Length(max = 255, message = "提币地址长度必须介于1和255之间")
    @Words(field = "提币地址", message = "提币地址包含敏感词")
    @Column(name = "wallet_address")
    private String walletAddress;
    
    /**
     * 币数量
     */
    @Money(point = 15, message = "币数量金额格式错误")
    @Column(name = "wallet_amount")
    private BigDecimal walletAmount;
    
    /**
     * 旷工费
     */
    @NotNull(message = "旷工费不能为空")
    @Money(point = 15, message = "旷工费金额格式错误")
    @Column(name = "fee")
    private BigDecimal fee;

    /**
     * ETH gas_price旷工费
     */
    @NotNull(message = "gas不能为空")
    @Money(point = 15, message = "gas格式错误")
    @Column(name = "gas_price")
    private BigDecimal gasPrice;

    /**
     * ETH gas_limits
     */
    @NotNull(message = "gasLimit不能为空")
    @Money(point = 15, message = "gasLimit格式错误")
    @Column(name = "gas_limit")
    private BigDecimal gasLimit;

    /**
     * 解锁密码
     */
    @Length(max = 100, message = "解锁密码长度必须介于1和100之间")
    @Words(field = "解锁密码", message = "解锁密码包含敏感词")
    @Column(name = "lock_password")
    private String lockPassword;
    
    /**
     * memo标签
     */
    @Length(max = 255, message = "memo标签长度必须介于1和255之间")
    @Words(field = "memo标签", message = "memo标签包含敏感词")
    @Column(name = "memo")
    private String memo;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWalletType() {
		return walletType;
	}
	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}
	public String getCoinCode() {
		return coinCode;
	}
	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}
	public String getWalletAddress() {
		return walletAddress;
	}
	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}
	public BigDecimal getWalletAmount() {
		return walletAmount;
	}
	public void setWalletAmount(BigDecimal walletAmount) {
		this.walletAmount = walletAmount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public BigDecimal getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(BigDecimal gasPrice) {
		this.gasPrice = gasPrice;
	}
	public BigDecimal getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(BigDecimal gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getLockPassword() {
		return lockPassword;
	}
	public void setLockPassword(String lockPassword) {
		this.lockPassword = lockPassword;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
