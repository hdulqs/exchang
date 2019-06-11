package com.batsoft.client.system.global;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.security.CsrfTokenManager;
import com.batsoft.model.module.exchange.Coin;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.Principal;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.service.module.system.service.navigation.AppNavigationService;

/**
 * Created by Administrator on 2017/5/22.
 */
public class GlobalServiceImpl implements GlobalService {

    @Autowired
    private  HttpServletRequest request;
    @Autowired
    private AppConfigService appConfigService;
    @Value("${websocket.point}")
    public String point;
    @Value("${websocket.broker}")
    public String broker;
    @Autowired
    private CustomerAccountService customerAccountService;
    @Override
    public String findGlobInfo() {
        {
            Map<String ,Object> globInfo= new HashMap();
            Map<String, Object> userInfo = new HashMap();
            Map<String, Object> websocket = new HashMap();
            Map<String ,Object> appConfig= new HashMap<String ,Object>();

            appConfig.put(AppConfig.WEBSITTITLE,webSiteTitle());
            appConfig.put(AppConfig.WEBSITEHOST,webSiteHost());
            appConfig.put(AppConfig.WEBSITKEYWORDS,webSiteKeyWords());
            appConfig.put(AppConfig.FILEHOST,fileHost());
            // appConfig.put("webNav",appNavigationService.findNav());

            // webSocket
            websocket.put("point",point);
            websocket.put("broker",broker);

            globInfo.put("webSocket",websocket);

            //用户信息
            userInfo.put("userName", UserUtils.getUser().getUserName());
            userInfo.put("userNick", UserUtils.getUser().getUserNick());
            userInfo.put("userMobile", UserUtils.getUser().getMobileSecurity());
            userInfo.put("userEmail", UserUtils.getUser().getEmailSecurity());
            userInfo.put("userRealName", UserUtils.getUser(false).getRealNameSecurity());
            userInfo.put("realState", UserUtils.getUser(false).getRealState());
            userInfo.put("realStateRemark", UserUtils.getUser(false).getRealStateRemark());
            userInfo.put("hasGoogleValid", UserUtils.getUser(false).isHasGoogleValid());
            userInfo.put("hasEmailValid", UserUtils.getUser().isHasEmailValid());
            userInfo.put("hasMobileValid", UserUtils.getUser().isHasMobileValid());
            userInfo.put("hasTradePasswordValid", UserUtils.getUser().isHasTradePassword());
            userInfo.put("oldLoginTime", UserUtils.getUser(false).getOldLoginTime());
            userInfo.put("oldLoginIp", UserUtils.getUser(false).getOldLoginIp());
            userInfo.put("userAvatar", UserUtils.getUser().getUserAvatar());

            globInfo.put("config",appConfig);
            // 是否登录
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null){
                globInfo.put("user", userInfo);
            }

            String ret= JSON.toJSONString(globInfo);
            return ret;
        }
    }


    @Override
    public String staticUrl() {
        return ApplicationConfigure.staticAssets;
    }

    @Override
    public String getUserName() {
        return UserUtils.getUser().getUserName();
    }

    @Override
    public String getUserId() {
        return UserUtils.getUser().getId();
    }

    @Override
    public String webSiteTitle() {
        return appConfigService.findValueByKey(AppConfig.WEBSITTITLE);
    }

    @Override
    public String shopAddress() { return appConfigService.findValueByKey(AppConfig.WEB_SHOP_ADDRESS); }

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
        return "http://localhost";
        //return appConfigService.findValueByKey(AppConfig.WEBSITEHOST);
    }

    @Override
    public String wsUrl() {
        return ApplicationConfigure.wsUrl;
    }

    @Override
    public String fileHost() {
        return appConfigService.findValueByKey(AppConfig.FILEHOST);
    }

    @Override
    public String iconPath() {
        return appConfigService.findValueByKey(AppConfig.ICONPATH);
    }

    @Override
    public String logoDarkPath() {
        return appConfigService.findValueByKey(AppConfig.FRONTLOGODARK);
    }

    @Override
    public String logoLightPath() {
        return appConfigService.findValueByKey(AppConfig.FRONTLOGOLIGHT);
    }

    @Override
    public String company() {
        return appConfigService.findValueByKey(AppConfig.COMPANY);
    }

    @Override
    public String loginBanner() {
        return appConfigService.findValueByKey(AppConfig.FRONTLOGINBANNER);
    }

    @Override
    public String userBanner() {
        return appConfigService.findValueByKey(AppConfig.FRONTUSERBANNER);
    }
    @Override
    public String mobileArea() {
        return appConfigService.findValueByKey(AppConfig.MOBILEAREA);
    }

    @Override
    public String c2cOpen() {
        return appConfigService.findValueByKey(AppConfig.C2COPEN);
    }

    @Override
    public String bankOpen() {
        return appConfigService.findValueByKey(AppConfig.BANKOPEN);
    }

    @Override
    public String userTotalMoney() {
        List<CustomerAccountVo> customerAccountVos = customerAccountService.findList(Coin.STATUS1);
        BigDecimal total = new BigDecimal(0);
        if(!customerAccountVos.isEmpty()) {
            total = RedisUserUtil.getTotalCNY(customerAccountVos).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return total.toString();
    }

    /**
     * 是否登陆
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        if(UserUtils.getUser().getUserName()==null){
            return  false;
        }else {
            return true;
        }
    }

    /**
     * 是否有交易验证密码
     *
     * @return
     */
    @Override
    public boolean tradePasswdValid() {
        return UserUtils.getUser().isHasTradePassword();
    }


    /**
     * 是否google认证
     *
     * @return
     */
    @Override
    public boolean googleAuth() {
        return UserUtils.getUser().isHasGoogleValid();
    }

    /**
     * 是否实名认证
     *
     * @return
     */
    @Override
    public boolean realState() {
        return UserUtils.getUser(false).isHasRealNameValid();
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
    public String csrfToken() {
        return CsrfTokenManager.getTokenForSession(request.getSession());
    }

    @Override
    public String lan() {
        CookieLocaleResolver localeResolver=(CookieLocaleResolver)SpringContextUtil.getBean("localeResolver");
        String lan = "";
        LocaleContext context=localeResolver.resolveLocaleContext(request);
        lan= context.getLocale().toString();
        return lan;
    }
}
