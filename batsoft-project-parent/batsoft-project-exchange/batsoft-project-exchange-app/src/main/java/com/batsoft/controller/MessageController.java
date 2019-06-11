package com.batsoft.controller;

import com.batsoft.core.annotation.SmsAuth;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.ValidCodeToken;
import com.batsoft.core.exception.ValidException;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.third.module.sms.MessageService;
import com.batsoft.utils.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(value = "MessageController",description = "短信Controller")
@RequestMapping(value = "/message")
@RestController
public class MessageController {

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private MessageService messageService;
    /**
     * 短信验证码发送
     *
     * @param mobile
     * @param validate
     * @return
     */
    @RequestMapping(value = "/sendCode",method = RequestMethod.POST)
    @ApiOperation(value = "短信验证码发送")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String",value = "手机号",required = true),
            @ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码",required = false),
            @ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形码",required = true)
    })
    @SmsAuth
    @ResponseBody
    public JsonResult sendCode(@RequestParam(value = "mobile") String mobile,
                               @RequestParam(value = "areaCode",required = false) String areaCode,
                               @RequestParam(value = "validate", required = false) String validate,
                               HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> data = new HashedMap();
        try {
            // 图形验证码校验
            ValidCodeToken validCodeToken = new ValidCodeToken(validate, false, request);
            validCodeToken.isValid();
            Map<String, Object> retData = new HashedMap();
            String codeTimeOut = appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT);
            retData.put("codeTimeOut", codeTimeOut);
            String host = IpUtil.getIpAddr(request);
            JsonResult ret = messageService.sendCode(areaCode, mobile,host, data);
            jsonResult.setSuccess(ret.getSuccess());
            jsonResult.setMsg(ret.getMsg());
            jsonResult.setCode(JsonResult.ResultCode.SUCCESS);
            jsonResult.setData(retData);
        } catch (ValidException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ResultCode.FAILE);
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ResultCode.FAILE);
        }
        return jsonResult;
    }

    /**
     * 短信验证码发送
     *
     * @param validate
     * @return
     */
    @RequestMapping(value = "/sendAuthMessageCode",method = RequestMethod.POST)
    @ApiOperation(value = "短信验证码发送")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码"),
            @ApiImplicitParam(paramType = "query", name = "validate", dataType = "String",value = "图形码",required = true)
    })
    @SmsAuth
    @ResponseBody
    public JsonResult sendCode(
            @RequestParam(value = "areaCode",required = false) String areaCode,
            @RequestParam(value = "validate") String validate,
            HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> data = new HashedMap();
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
        try {
            // 图形验证码校验
            ValidCodeToken validCodeToken = new ValidCodeToken(validate, false, request);
            validCodeToken.isValid();
            Map<String, Object> retData = new HashedMap();
            String codeTimeOut = appConfigService.findValueByKey(AppConfig.MOBILECODETIMEOUT);
            retData.put("codeTimeOut", codeTimeOut);
            String host = IpUtil.getIpAddr(request);
            JsonResult ret = messageService.sendCode(areaCode, userVo.getUserMobile(),host, data);
            jsonResult.setSuccess(ret.getSuccess());
            jsonResult.setMsg(ret.getMsg());
            jsonResult.setData(retData);
        } catch (ValidException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }
}
