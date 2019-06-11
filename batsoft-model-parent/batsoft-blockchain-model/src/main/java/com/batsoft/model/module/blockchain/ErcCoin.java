/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-24 21:08:34
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
 * 代币
 * 
 */
@Entity
@ToString
@Table(name = "blockchain_erc_coin")
public class ErcCoin extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8937327976683464989L;


	/**
     * 代币精度
     *18:ether
     */
    public static final String COINDECIMALS18 = "18";


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 代币Code
     */
    @NotNull(message = "代币Code不能为空")
    @Length(max = 20, message = "代币Code长度必须介于1和20之间")
    @Words(field = "代币Code", message = "代币Code包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;

    /**
     * 区块链类型 默认ETH
     */

    @NotNull(message = "区块链类型不能为空")
    @Length(max = 20, message = "区块链类型长度必须介于1和20之间")
    @Words(field = "区块链类型", message = "区块链类型包含敏感词")
    @Column(name = "block")
    private String block;

    /**
     * 合约地址
     */
    @NotNull(message = "合约地址不能为空")
    @Length(max = 255, message = "合约地址长度必须介于1和255之间")
    @Words(field = "合约地址", message = "合约地址包含敏感词")
    @Column(name = "coin_address")
    private String coinAddress;
    
    /**
     * 代币精度
     */
    @Length(max = 10, message = "代币精度长度必须介于1和10之间")
    @Words(field = "代币精度", message = "代币精度包含敏感词")
    @Column(name = "coin_decimals")
    private String coinDecimals;
    
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
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public String getCoinAddress() {
		return coinAddress;
	}
	public void setCoinAddress(String coinAddress) {
		this.coinAddress = coinAddress;
	}
	public String getCoinDecimals() {
		return coinDecimals;
	}
	public void setCoinDecimals(String coinDecimals) {
		this.coinDecimals = coinDecimals;
	}

}
