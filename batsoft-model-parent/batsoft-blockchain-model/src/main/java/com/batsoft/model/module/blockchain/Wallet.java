/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-25 10:54:59
 */

package com.batsoft.model.module.blockchain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 钱包
 * 
 */
@Entity
@ToString
@Table(name = "blockchain_wallet")
public class Wallet extends BaseModel {
	
	private static final long serialVersionUID = -6258487163328881581L;
	
    /**
     * 钱包类型
     *BTC:BTC
     */
    public static final String WALLETTYPEBTC = "BTC";
    /**
     * 钱包类型
     *ETH:ETH
     */
    public static final String WALLETTYPEETH = "ETH";
    /**
     * 钱包类型
     *EOS:EOS
     */
    public static final String WALLETTYPEEOS = "EOS";
    /**
     * 状态
     *0:禁用
     */
    public static final Integer STATUS0 = 0;
    /**
     * 状态
     *1:正常
     */
    public static final Integer STATUS1 = 1;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 钱包类型
     */

    @Length(max = 255, message = "钱包类型长度必须介于1和255之间")
    @Words(field = "钱包类型", message = "钱包类型包含敏感词")
    @Column(name = "wallet_type")
    private String walletType;
    /**
     * 钱包名称
     */

    @NotNull(message = "钱包名称不能为空")
    @Length(max = 20, message = "钱包名称长度必须介于1和20之间")
    @Words(field = "钱包名称", message = "钱包名称包含敏感词")
    @Column(name = "wallet_name")
    private String walletName;
    /**
     * 钱包代码
     */

    @NotNull(message = "钱包代码不能为空")
    @Length(max = 20, message = "钱包代码长度必须介于1和20之间")
    @Words(field = "钱包代码", message = "钱包代码包含敏感词")
    @Column(name = "wallet_code")
    private String walletCode;
    /**
     * 协议
     */

    @Length(max = 20, message = "协议长度必须介于1和20之间")
    @Words(field = "协议", message = "协议包含敏感词")
    @Column(name = "rpc_protocol")
    private String rpcProtocol;
    /**
     * 钱包ip
     */

    @NotNull(message = "钱包ip不能为空")
    @Length(max = 15, message = "钱包ip长度必须介于1和15之间")
    @Words(field = "钱包ip", message = "钱包ip包含敏感词")
    @Column(name = "rpc_ip")
    private String rpcIp;
    /**
     * 钱包账号
     */

    @Length(max = 50, message = "钱包账号长度必须介于1和50之间")
    @Words(field = "钱包账号", message = "钱包账号包含敏感词")
    @Column(name = "rpc_account")
    private String rpcAccount;
    /**
     * 钱包密码
     */

    @Length(max = 50, message = "钱包密码长度必须介于1和50之间")
    @Words(field = "钱包密码", message = "钱包密码包含敏感词")
    @Column(name = "rpc_password")
    private String rpcPassword;
    /**
     * 钱包端口
     */

    @NotNull(message = "钱包端口不能为空")
    @Length(max = 10, message = "钱包端口长度必须介于1和10之间")
    @Words(field = "钱包端口", message = "钱包端口包含敏感词")
    @Column(name = "rpc_port")
    private String rpcPort;
    /**
     * 钱包描述
     */

    @Length(max = 255, message = "钱包描述长度必须介于1和255之间")
    @Words(field = "钱包描述", message = "钱包描述包含敏感词")
    @Column(name = "wallet_remark")
    private String walletRemark;
    /**
     * 状态
     */

    @NotNull(message = "状态不能为空")
    @Column(name = "status")
    private Integer status;
    /**
     * 区块高度
     */

    @Column(name = "block_high")
    private Long blockHigh;
    
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
	public String getWalletName() {
		return walletName;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}
	public String getWalletCode() {
		return walletCode;
	}
	public void setWalletCode(String walletCode) {
		this.walletCode = walletCode;
	}
	public String getRpcProtocol() {
		return rpcProtocol;
	}
	public void setRpcProtocol(String rpcProtocol) {
		this.rpcProtocol = rpcProtocol;
	}
	public String getRpcIp() {
		return rpcIp;
	}
	public void setRpcIp(String rpcIp) {
		this.rpcIp = rpcIp;
	}
	public String getRpcAccount() {
		return rpcAccount;
	}
	public void setRpcAccount(String rpcAccount) {
		this.rpcAccount = rpcAccount;
	}
	public String getRpcPassword() {
		return rpcPassword;
	}
	public void setRpcPassword(String rpcPassword) {
		this.rpcPassword = rpcPassword;
	}
	public String getRpcPort() {
		return rpcPort;
	}
	public void setRpcPort(String rpcPort) {
		this.rpcPort = rpcPort;
	}
	public String getWalletRemark() {
		return walletRemark;
	}
	public void setWalletRemark(String walletRemark) {
		this.walletRemark = walletRemark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getBlockHigh() {
		return blockHigh;
	}
	public void setBlockHigh(Long blockHigh) {
		this.blockHigh = blockHigh;
	}
	
}
