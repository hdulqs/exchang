package com.batsoft.third.module.sms.impl;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.third.module.sms.MessageService;
import com.batsoft.third.module.sms.SendService;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/8.
 */
@Slf4j
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SendService juHeSendServiceImpl;

    @Override
    public JsonResult sendCode(String areaCode, String mobile,String host, Map<String, Object> data) {
        JsonResult result=new JsonResult();
        String pattern = "(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(host);
        if(matcher.matches()){
            if(!checkHostSend(mobile,host)){
                result.setSuccess(false);
                result.setMsg(Language.L_Failed("msg_send_code_over_max",false));
                return result;
            }
        }
        if(!checkSend(mobile)){
            result.setSuccess(false);
            result.setMsg(Language.L_Failed("msg_send_code_over_max",false));
            return result;
        }
        //生成验证码
        data = initCode(mobile, data);
        // 是否启用默认值
        if (!"1".equals(appConfigService.findValueByKey(AppConfig.USEDEFAULTMOBILECODE))) {
            // 发送验证码
            result = juHeSendServiceImpl.send(areaCode, mobile, data, appConfigService.findValueByKey(AppConfig.SMSTPLCODE));
        }else{
            result.setMsg("success");
            result.setCode(JsonResult.ResultCode.SUCCESS);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    boolean checkSend(String mobile){
        if(StringUtils.isNull(mobile)){
            return false;
        }
        String key = ApplicationConfigure.DAY_MOBILE_SEND_NUM_KEY+":"+mobile;
       if(redisService.hasKey(key)){
            String DaySendNum = redisService.get(key);
            String dayApplicationNum = appConfigService.findValueByKey(AppConfig.DAYSENDMOBILECODEMAXNUM);
            int sendMaxNum = 5;
            if(!StringUtils.isNull(dayApplicationNum)){
                sendMaxNum = Integer.valueOf(dayApplicationNum);
            }
            if(Integer.valueOf(DaySendNum)>sendMaxNum){
                return false;
            }else{
                redisService.set(key,Integer.toString(Integer.valueOf(DaySendNum)+1), DateUtils.getNowToEndOfDaySends().intValue());
            }
        }else{
           redisService.set(key,"1",DateUtils.getNowToEndOfDaySends().intValue());
       }
        return true;
    }
    /**
     * IP发送短信验证码
     * @param host
     * @return
     */
    boolean checkHostSend(String mobile,String host){
        if(StringUtils.isNull(host)){
            return false;
        }
        String key = ApplicationConfigure.DAY_MOBILE_SEND_NUM_KEY+":"+mobile+":"+host;
        if(redisService.hasKey(key)){
            String DaySendNum = redisService.get(key);
            String dayApplicationNum = appConfigService.findValueByKey(AppConfig.DAYHOSTSENDMOBILECODEMAXNUM);
            int sendMaxNum = 10;
            if(!StringUtils.isNull(dayApplicationNum)){
                sendMaxNum = Integer.valueOf(dayApplicationNum);
            }
            if(Integer.valueOf(DaySendNum)>sendMaxNum){
                return false;
            }else{
                redisService.set(key,Integer.toString(Integer.valueOf(DaySendNum)+1), DateUtils.getNowToEndOfDaySends().intValue());
            }
        }else{
            redisService.set(key,"1",DateUtils.getNowToEndOfDaySends().intValue());
        }
        return true;
    }

    /**
     * 生成验证码
     *
     * @param mobile
     * @param data
     * @return
     */
    private Map<String, Object> initCode(String mobile, Map<String, Object> data) {

        String code = "";
        // 是否启用默认值
        if ("1".equals(appConfigService.findValueByKey(AppConfig.USEDEFAULTMOBILECODE))) {
            code = appConfigService.findValueByKey(AppConfig.DEFAULTMOBILECODE);
        } else {
            //是否纯数字
            boolean isNumber = false;
            String is_number = appConfigService.findValueByKey(AppConfig.USECODENUMBER);
            if ("1".equals(is_number)) {
                isNumber = true;
            }
            //验证码长度
            String codeLength = appConfigService.findValueByKey(AppConfig.MOBILECODELENGTH);
            code = StringUtils.createRandom(isNumber, Integer.valueOf(codeLength));
        }
        data.put("#code#", code);

        // 是否已经存在缓存
        if (redisService.hasKey(ApplicationConfigure.mobileCode + ":" + mobile)) {
            redisService.delRedisByKey(ApplicationConfigure.mobileCode + ":" + mobile);
        }
        // 验证码放入缓存
        redisService.set(ApplicationConfigure.mobileCode + ":" + mobile, code, Integer.valueOf(appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT)));

        return data;
    }
}
