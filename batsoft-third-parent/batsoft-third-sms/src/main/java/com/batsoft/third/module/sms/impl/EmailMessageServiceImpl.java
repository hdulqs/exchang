package com.batsoft.third.module.sms.impl;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.third.module.sms.EmailMessageService;
import com.batsoft.utils.StringUtils;
import com.batsoft.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/8.
 */
@Slf4j
@Service("emailMessageService")
public class EmailMessageServiceImpl implements EmailMessageService {
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.subject}")
    private String defaultSubject;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public JsonResult sendSimpleMail(String to, String subject, String tpl, Map<String, Object> data) {
        JsonResult result = new JsonResult();
        try {
            //生成验证码
            data = initCode(to, data);
            if (StringUtils.isEmpty(subject)) {
                subject = defaultSubject;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(TemplateUtils.tplToStr(tpl, data));
            sender.send(message);

            Map<String, Object> retData = new HashedMap();
            String codeTimeOut= appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT);
            retData.put("codeTimeOut",codeTimeOut);

            result.setMsg("邮件已经发送");
            result.setSuccess(true);
            result.setCode(Constants.SUCCESS);
            result.setData(retData);
            logger.info("简单邮件已经发送。");
        } catch (Exception e) {
            result.setMsg("邮件发送失败");
            result.setSuccess(false);
            result.setCode(Constants.FAILED);
            logger.error("发送邮件时发生异常！", e);
        }
        return result;
    }

    /**
     * 生成验证码
     *
     * @param email
     * @param data
     * @return
     */
    private Map<String, Object> initCode(String email, Map<String, Object> data) {

        String code = "";
        //是否纯数字
        boolean isNumber = false;
        String is_number = appConfigService.findValueByKey(AppConfig.USECODENUMBER);
        if ("1".equals(is_number)) {
            isNumber = true;
        }

        //验证码长度
        String codeLength = appConfigService.findValueByKey(AppConfig.MOBILECODELENGTH);
        code = StringUtils.createRandom(isNumber, Integer.valueOf(codeLength));
        data.put("code", code);

        // 是否已经存在缓存
        if (redisService.hasKey(ApplicationConfigure.emailCode + ":" + email)) {
            redisService.delRedisByKey(ApplicationConfigure.emailCode + ":" + email);
        }
        // 验证码放入缓存
        redisService.set(ApplicationConfigure.emailCode + ":" + email, code, Integer.valueOf(appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT)));

        return data;
    }


}
