package com.batsoft.core.common.i18n;

import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.utils.StringUtils;
import org.springframework.stereotype.Component;

@Component("language")
public class Language {

    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    /**
     * <p> 语言切换</p>
     * L(message) message 必须为i18n 文件中配置的参数
     *
     * @author: Bat Admin
     * @param: @param code
     * @param: @return
     * @return: String
     * @Date :          2017年1月10日 下午4:41:59
     * @throws:
     */
    public static String L(String code) {
        LocaleMessageSourceService localeMessageSourceService = (LocaleMessageSourceService) SpringContextUtil.getBean("localeMessageSourceService");
        if (StringUtils.isEmpty(code)) {
            return "code is not find!";
        } else {
            return localeMessageSourceService.getMessage(code) == null ? "" : localeMessageSourceService.getMessage(code);
        }
    }

    /**
     * 返回 成功消息  *** 成功
     *
     * @param code
     * @return
     */
    public static String L_Success(String code) {
    return L_Success(code,true);
    }
    /**
     * 返回 成功消息  *** 成功
     *
     * @param code
     * @return
     */
    public static String L_Success(String code,boolean appendSuccessStr) {
        LocaleMessageSourceService localeMessageSourceService = (LocaleMessageSourceService) SpringContextUtil.getBean("localeMessageSourceService");
        if(!appendSuccessStr){
            return (localeMessageSourceService.getMessage("") == null ? "" : localeMessageSourceService.getMessage(code));
        }else{
            return (localeMessageSourceService.getMessage("") == null ? "" : localeMessageSourceService.getMessage(code));
        }
    }

    /**
     * 返回 成功消息  *** 失败
     *
     * @param code
     * @return
     */
    public static String L_Failed(String code) {
       return L_Failed(code,false);
    }
    /**
     * 返回 成功消息  *** 失败
     *
     * @param code 错误code
     * @param  appendFailStr 是否追加失败
     * @return
     */
    public static String L_Failed(String code,boolean appendFailStr) {
        LocaleMessageSourceService localeMessageSourceService = (LocaleMessageSourceService) SpringContextUtil.getBean("localeMessageSourceService");
        if(!appendFailStr) {
            return (localeMessageSourceService.getMessage("") == null ? "" : localeMessageSourceService.getMessage(code));
        }else {
            return (localeMessageSourceService.getMessage("") == null ? "" : localeMessageSourceService.getMessage(code)) + localeMessageSourceService.getMessage(FAILED);
        }
    }
}
