/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2016年12月10日 下午5:43:08
 */
package com.batsoft.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author: Bat Admin
 * @Date : 2016年12月10日 下午5:43:08 100
 */
@Configuration
@Order(1)
public class ApplicationConfigure implements CommandLineRunner {

	@Value("${batsoft.name}")
    public String _name;
    
    @Value("${batsoft.validCode}")
    public String  _validCode;
    
    @Value("${batsoft.mobileCode}")
    public String _mobileCode;

    @Value("${batsoft.emailCode}")
    public String _emailCode;

    @Value("${batsoft.desc}")
    public String _desc;

    @Value("${shiro.cookie.sessionIdCookie.name}")
    public String _sessionIdCookie = "";

    @Value("${shiro.session.global-session-timeout}")
    public int _sessionTimeOut = 18000;

    @Value("${batsoft.file.basepath}")
    public String _file_basepath;

    @Value("${batsoft.static-assets}")
    public String _static;
    
    @Value("${spring.cloud.config.profile}")
    public String _profile;

    @Value("${batsoft.lucene.server}")
    public String _lucene_server;
    // 模版根目录
    @Value("${batsoft.tplPath}")
    private String _tplPath="";

    // 默认币种
    @Value("${batsoft.defaultCoin}")
    private String _defaultCoin="";

    /**
     * 手机验证码发送次数
     */
    public static final String DAY_MOBILE_SEND_NUM_KEY = "DAY_MOBILE_SEND_NUM_KEY";

    /**
     * 手机验证码单个ip发送次数
     */
    private String DayMobileHostSendNum;
    //@Value("${websocket.url}")
    //private String _ws_url="";

    public static String name;
    public static String profile;
    public static String validCode;
    public static String mobileCode;
    public static String emailCode;
    public static String desc;
    public static String sessionIdCookie;
    public static int sessionTimeOut;
    public static String staticAssets;
    public static String luceneServer;
    public static String tplPath;
    public static String defaultCoin;
    public static String wsUrl;
    /**
     * 文件路径
     */
    public static String FILE_BASEPATH;

    /* (non-Javadoc)
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {

        name = _name;
        validCode=_validCode;
        mobileCode=_mobileCode;
        emailCode=_emailCode;
        desc = _desc;
        sessionIdCookie = _sessionIdCookie;
        sessionTimeOut = _sessionTimeOut;
        FILE_BASEPATH = _file_basepath;

        staticAssets = _static;

        luceneServer = _lucene_server;
        profile=_profile;
        tplPath=_tplPath;
        defaultCoin=_defaultCoin;
        //wsUrl=_ws_url;


    }


}
