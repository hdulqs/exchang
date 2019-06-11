/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-23 23:07:19
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
 * 货币订单
 * 
 * @author simon
 */
@Entity
@ToString
@Table(name = "blockchain_coin_order")
public class CoinOrder extends BaseModel {

	private static final long serialVersionUID = 4239471843966521663L;

	/**
     * 交易类型
     *0:系统外
     */
    public static final Integer TXTYPE0 = 0;
    
    /**
     * 交易类型
     *1:系统内
     */
    public static final Integer TXTYPE1 = 1;
    
    /**
     * 是否确认
     *0:否
     */
    public static final Integer CONSUME0 = 0;
    
    /**
     * 是否确认
     *1:是
     */
    public static final Integer CONSUME1 = 1;


    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 币代码
     */
    @Length(max = 20, message = "币代码长度必须介于1和20之间")
    @Words(field = "币代码", message = "币代码包含敏感词")
    @Column(name = "coin_code")
    private String coinCode;


    /**
     * 钱包类型 BTC ？ETH ？QTUM ...
     */
    @Column(name = "wallet_type")
    private String walletType;


    /**
     * from
     */
    @NotNull(message = "from不能为空")
    @Length(max = 255, message = "from长度必须介于1和255之间")
    @Words(field = "from", message = "from包含敏感词")
    @Column(name = "from_address")
    private String fromAddress;
    
    /**
     * to_address
     */
    @NotNull(message = "to_address不能为空")
    @Length(max = 255, message = "to_address长度必须介于1和255之间")
    @Words(field = "to_address", message = "to_address包含敏感词")
    @Column(name = "to_address")
    private String toAddress;


    /**
     * 交易金额
     */
    @NotNull(message = "交易金额不能为空")
    @Money(point = 15, message = "交易金额金额格式错误")
    @Column(name = "amount")
    private BigDecimal amount;
    
    /**
     * 手续费
     */
    @NotNull(message = "手续费不能为空")
    @Money(point = 15, message = "手续费金额格式错误")
    @Column(name = "fee")
    private BigDecimal fee;
    
    /**
     * 区块链流水号
     */
    @Length(max = 200, message = "区块链流水号长度必须介于1和200之间")
    @Words(field = "区块链流水号", message = "区块链流水号包含敏感词")
    @Column(name = "tx_hash")
    private String txHash;
    /**
     * 交易类型
     */

    @NotNull(message = "交易类型不能为空")
    @Column(name = "tx_type")
    private Integer txType;
    
    /**
     * block_number
     */
    @Length(max = 50, message = "block_number长度必须介于1和50之间")
    @Words(field = "block_number", message = "block_number包含敏感词")
    @Column(name = "block_number")
    private String blockNumber;
    
    /**
     * transaction_index
     */
    @Length(max = 50, message = "transaction_index长度必须介于1和50之间")
    @Words(field = "transaction_index", message = "transaction_index包含敏感词")
    @Column(name = "transaction_index")
    private String transactionIndex;

    /**
     *tx_input
     */
    @Column(name = "tx_input")
    private String txInput;
    
    /**
     * block_hash
     */
    @Length(max = 200, message = "block_hash长度必须介于1和200之间")
    @Words(field = "block_hash", message = "block_hash包含敏感词")
    @Column(name = "block_hash")
    private String blockHash;
    
    /**
     * gas
     */
    @Money(point = 15, message = "gas金额格式错误")
    @Column(name = "gas")
    private BigDecimal gas;
    
    /**
     * gas_price
     */
    @Money(point = 15, message = "gas_price金额格式错误")
    @Column(name = "gas_price")
    private BigDecimal gasPrice;
    
    /**
     * 是否确认
     */
    @Column(name = "consume")
    private Integer consume;

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

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getTxType() {
        return txType;
    }

    public void setTxType(Integer txType) {
        this.txType = txType;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getTxInput() {
        return txInput;
    }

    public void setTxInput(String txInput) {
        this.txInput = txInput;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigDecimal getGas() {
        return gas;
    }

    public void setGas(BigDecimal gas) {
        this.gas = gas;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Integer getConsume() {
        return consume;
    }

    public void setConsume(Integer consume) {
        this.consume = consume;
    }
}
