package com.batsoft.client.system.global;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface GlobalService {
    String findGlobInfo();
    String staticUrl();
    String getUserName();
    String getUserId();
    String webSiteTitle();
    String webSiteKeyWords();
    String webSiteRemark();
    String webSiteHost();
    String shopAddress();
    String wsUrl();
    String fileHost();
    String iconPath();
    String logoLightPath();
    String logoDarkPath();
    String company();
    String loginBanner();
    String userBanner();
    String mobileArea();
    String c2cOpen();
    String bankOpen();
    String userTotalMoney();

    /**
     * 是否登陆
     * @return
     */
    boolean isLogin();

    /**
     * 是否google认证
     * @return
     */
    boolean googleAuth();

    /**
     * 是否有交易密码
     * @return
     */
    boolean tradePasswdValid();

    /**
     * 是否实名认证
     * @return
     */
    boolean realState();


    /**
     * 静态文件模式 dev 源文件 dist 压缩合并文件
     * 根据spring.cloud.config.profile=front-prod 来确定属于那种模式
     *   dev --dev
     *   prod --dist
     * @return
     */
    String staticDev();

    /**
     * csrfToken
     * @return
     */
    String csrfToken();

    /**
     * 获取语言
     * @return
     */
    String lan();
}
