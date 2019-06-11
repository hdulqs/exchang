package com.batsoft.third.module.sms;

import com.batsoft.core.common.JsonResult;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/8.
 */
public interface EmailMessageService {
    /**
     * sendSimpleMail
     * @param email
     * @param subject
     * @param tpl
     * @param data
     * @return
     */
    JsonResult sendSimpleMail(String email,String subject,String tpl, Map<String, Object> data) ;
}
