package com.batsoft.third.module.weixin.service.impl;

import com.batsoft.third.module.weixin.service.IWXPayDomain;
import com.batsoft.third.module.weixin.util.WeixinConfig;
import com.github.wxpay.sdk.WXPayConfig;
import org.nutz.dao.entity.annotation.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {

    @Value("${pay.weixin.appid}")
    public String appid;
    @Value("${pay.weixin.mch_id}")
    public String mch_id;

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;
    @Autowired
    public WeixinConfig weixinConfig;

    private WXPayConfigImpl() throws Exception{
        String certPath = "D://CERT/common/apiclient_cert.p12";
        File file = new File(certPath);
        if(file.exists()){
            InputStream certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
            certStream.close();
        }
    }

    public static WXPayConfigImpl getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getAppID() {
//        return appid;
        return weixinConfig.getAppid();
    }

    @Override
    public String getMchID() {
//        return mch_id;
        return weixinConfig.getMch_id();
    }

    @Override
    public String getKey() {
        return "2ab9071b06b9f739b950ddb41db2690d";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        if(this.certData!=null){
            certBis = new ByteArrayInputStream(this.certData);
            return certBis;
        }else{
            return null;
        }
    }


    @Override
    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

//    @Override
//    public int getReportWorkerNum() {
//        return 1;
//    }
//
//    @Override
//    public int getReportBatchSize() {
//        return 2;
//    }
}
