package com.batsoft.client.system;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/9/30.
 */
@Data
@Component("templateProperties")
@ConfigurationProperties(prefix = "template")
public class TemplateProperties {
    /**
     *  login tpl
     */
    private String login = "login";
    private String index="index";
    private String register="register";
    private String helpcenter="helpcenter";
    private String conditions="conditions";
    private String privacys="privacys";
    private String handlingfee="handlingfee";
    private String agreement="agreement";
    private String deprecated="deprecated";
    private String commercial="commercial";
    private String forgot="forgot";
    private String h5Login="h5/login";
    private String h5Register="h5/register";
    private String advertisement="advertisement";
    private String description="description";
    private String shopping_guide="shopping_guide";
    private String referral="referral";
}
