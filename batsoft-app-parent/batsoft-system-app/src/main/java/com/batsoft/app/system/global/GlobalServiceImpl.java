package com.batsoft.app.system.global;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.security.CsrfTokenManager;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/22.
 */
public class GlobalServiceImpl implements GlobalService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AppConfigService appConfigService;

    @Override
    public String findGlobInfo() {
        {
            AppUser user=new AppUser();
            user.setUserName(UserUtils.getUser().getUserName());
            user.setRealName(UserUtils.getUser().getRealName());
            Map<String ,Object> globInfo= new HashMap();
            Map<String ,Object> appConfig= new HashMap();

            appConfig.put(AppConfig.WEBSITTITLE,webSiteTitle());
            appConfig.put(AppConfig.WEBSITEHOST,webSiteHost());
            appConfig.put(AppConfig.WEBSITKEYWORDS,webSiteKeyWords());
            appConfig.put(AppConfig.FILEHOST,fileHost());

            globInfo.put("perm", UserUtils.getPermission());
            globInfo.put("user",user);
            globInfo.put("config",appConfig);
            String ret= JSON.toJSONString(globInfo);
            return ret;
        }
    }
    @Override
    public String staticDev() {
        if(ApplicationConfigure.profile.contains("prod")){
            return "dist";
        }else{
            return "dev";
        }
    }
    @Override
    public String staticUrl() {
        return ApplicationConfigure.staticAssets;
    }

    @Override
    public String webSiteTitle() {
        return appConfigService.findValueByKey(AppConfig.WEBSITTITLE);
    }

    @Override
    public String webSiteKeyWords() {
        return appConfigService.findValueByKey(AppConfig.WEBSITKEYWORDS);
    }

    @Override
    public String webSiteRemark() {
        return appConfigService.findValueByKey(AppConfig.WEBSITEREMARK);
    }

    @Override
    public String webSiteHost() {
//        return appConfigService.findValueByKey(AppConfig.WEBSITEHOST);
        return "http://localhost";
    }
    @Override
    public String fileHost() {
        return appConfigService.findValueByKey(AppConfig.FILEHOST);
    }

    @Override
    public String logoPath() {
        return appConfigService.findValueByKey(AppConfig.MANAGELOGO);
    }

    @Override
    public String company() {
        return appConfigService.findValueByKey(AppConfig.COMPANY);
    }

    @Override
    public String csrfToken() {
        return CsrfTokenManager.getTokenForSession(request.getSession());
    }
}
