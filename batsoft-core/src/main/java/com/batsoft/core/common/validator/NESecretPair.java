package com.batsoft.core.common.validator;

/**
 * 密钥对
 * Created by captcha_dev on 16-11-10.
 */
public class NESecretPair {
    public  String secretId ;
    public  String secretKey ;

    /**
     * 构造函数
     * @param secretId 密钥对id
     * @param secretKey 密钥对key
     */
    public NESecretPair(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }
}