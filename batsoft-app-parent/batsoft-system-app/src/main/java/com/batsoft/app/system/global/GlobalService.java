package com.batsoft.app.system.global;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface GlobalService {
    String findGlobInfo();
    String staticUrl();
    String webSiteTitle();
    String webSiteKeyWords();
    String webSiteRemark();
    String webSiteHost();
    String fileHost();
    String logoPath();
    String company();
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
}
