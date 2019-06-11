package com.batsoft.blockchain.rpc;

import lombok.Data;

@Data
public class Config  {
    private  String coinCode="";

    private String  walletCode="";

    /**
     * protocol
     */
    private   String  protocol="protocol";

    /**
     * ip
     */
    private  String ip="127.0.0.1";

    /**
     * port
     */
    private String port="8000";

    /**
     * rpcuser
     */
    private  String rpcuser="rpcuser";

    /**
     * rpcpassword
     */
    private  String rpcpassword="rpcpassword";

    public Config(){}
    public Config(String walletCode,String coinCode,String protocol,String ip,String port,String rpcuser,String rpcpassword){

        this.protocol=protocol;
        this.ip=ip;
        this.port=port;
        this.rpcuser=rpcuser;
        this.rpcpassword=rpcpassword;
        this.coinCode=coinCode;
        this.walletCode=walletCode;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getWalletCode() {
        return walletCode;
    }

    public void setWalletCode(String walletCode) {
        this.walletCode = walletCode;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRpcuser() {
        return rpcuser;
    }

    public void setRpcuser(String rpcuser) {
        this.rpcuser = rpcuser;
    }

    public String getRpcpassword() {
        return rpcpassword;
    }

    public void setRpcpassword(String rpcpassword) {
        this.rpcpassword = rpcpassword;
    }

    @Override
    public String toString() {
        return "Config{" +
                "coinCode='" + coinCode + '\'' +
                ", walletCode='" + walletCode + '\'' +
                ", protocol='" + protocol + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", rpcuser='" + rpcuser + '\'' +
                ", rpcpassword='" + rpcpassword + '\'' +
                '}';
    }
}
