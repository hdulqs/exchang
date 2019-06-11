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
 * 	货币钱包
 * 
 */
@Entity
@ToString
@Table(name = "blockchain_coin_account")
public class CoinAccount extends BaseModel {

	private static final long serialVersionUID = 2994965082888144817L;
	
	/**
     * 状态
     *0:可用
     */
    public static final Integer STATUS0 = 0;
    /**
     * 状态
     *1:已用
     */
    public static final Integer STATUS1 = 1;
    
    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 钱包
     */
    @NotNull(message = "钱包不能为空")
    @Length(max = 32, message = "钱包长度必须介于1和32之间")
    @Words(field = "钱包", message = "钱包包含敏感词")
    @Column(name = "wallet_id")
    private String walletId;
    
    /**
     * 钱包code
     */
    @NotNull(message = "钱包code不能为空")
    @Length(max = 10, message = "钱包code长度必须介于1和10之间")
    @Words(field = "钱包code", message = "钱包code包含敏感词")
    @Column(name = "wallet_code")
    private String walletCode;
    
    /**
     * 币代码
     */
    @NotNull(message = "币代码不能为空")
    @Length(max = 10, message = "币代码长度必须介于1和10之间")
    @Words(field = "币代码", message = "币代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;
    
    /**
     * 币地址
     */
    @NotNull(message = "币地址不能为空")
    @Length(max = 255, message = "币地址长度必须介于1和255之间")
    @Words(field = "币地址", message = "币地址包含敏感词")
    @Column(name = "coin_address")
    private String coinAddress;
    
    /**
     * 币地址密码
     */
    @Length(max = 255, message = "币地址密码长度必须介于1和255之间")
    @Words(field = "币地址密码", message = "币地址密码包含敏感词")
    @Column(name = "address_password")
    private String addressPassword;
    
    /**
     * memo
     */
    @Length(max = 100, message = "memo长度必须介于1和100之间")
    @Words(field = "memo", message = "memo包含敏感词")
    @Column(name = "memo")
    private String memo;
    
    /**
     * 币数量
     */
    @NotNull(message = "币数量不能为空")
    @Money(point = 15, message = "币数量金额格式错误")
    @Column(name = "balance")
    private BigDecimal balance;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    @Column(name = "status")
    private Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getWalletCode() {
		return walletCode;
	}

	public void setWalletCode(String walletCode) {
		this.walletCode = walletCode;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
	}

	public String getCoinAddress() {
		return coinAddress;
	}

	public void setCoinAddress(String coinAddress) {
		this.coinAddress = coinAddress;
	}

	public String getAddressPassword() {
		return addressPassword;
	}

	public void setAddressPassword(String addressPassword) {
		this.addressPassword = addressPassword;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
