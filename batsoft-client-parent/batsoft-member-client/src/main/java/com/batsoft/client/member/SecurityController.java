/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.member;

import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.exception.ValidException;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.member.Bankcard;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.BankCardVo;
import com.batsoft.model.module.member.vo.UserAuthVo;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.member.service.BankcardService;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.third.module.Tpl;
import com.batsoft.third.module.sms.EmailMessageService;
import com.batsoft.utils.GoogleAuthenticatorUtil;
import com.batsoft.utils.StringUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.nutz.lang.Dumps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>securityController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description ="系统安全配置" )
@Controller("securityController")
@RequestMapping("/member/security")
@Slf4j
public class SecurityController extends GenericController {


    @Autowired
    private EmailMessageService emailMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private BankcardService bankcardService;

    @Autowired
    private RedisService redisService;


    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("userInfo", UserUtils.getUser());
        return "member/security/index";
    }

    /**
     * 安全策略
     *
     * @param model
     * @return
     */
    @RequestMapping("/safe")
    public String safe(Model model) {
        return "member/security/safe";
    }

    /**
     * 设置头像
     *
     * @param model
     * @return
     */
    @RequestMapping("/setAvatar")
    public String avatar(Model model) {
        model.addAttribute("userAvatar",UserUtils.getUser().getUserAvatar());
        return "member/security/user_avatar";
    }

    /**
     * 修改资金密码页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateTradePwd",method = RequestMethod.POST)
    @ApiOperation(value = "更新交易资金密码接口")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobileCode", dataType = "String",value = "手机验证码",required = true),
            @ApiImplicitParam(paramType = "query", name = "tradePassword", dataType = "String",value = "新交易密码",required = true)
    })
    public JsonResult updateTradePwd( @RequestParam(value = "mobileCode") String mobileCode,@RequestParam("tradePassword") String tradePassword,HttpServletRequest request) {
        UserVo user =  UserUtils.getUser();
        JsonResult jsonResult=new JsonResult(true);
        try {
            if(user.getUserMobile() != null && !user.getUserMobile().trim().equals("")){
                try {
                    MobileCodeToken mobileCodeToken = new MobileCodeToken(mobileCode, user.getUserMobile(), false, request);
                    mobileCodeToken.isValid();
                    jsonResult = userService.updateTradePassword(tradePassword);
                    //刷新缓存用户
                    UserUtils.freshCacheUser(user.getUserName());
                }catch (ValidException e){
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("msg_code_error"));
                    return jsonResult;
                }catch (Exception execution){
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg(Language.L_Failed(""));
                    jsonResult.setSuccess(false);
                }
            }else{
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setMsg(Language.L_Failed(""));
                jsonResult.setSuccess(false);
            }
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
        return  jsonResult;
    }

    /**
     * 设置资金密码页面
     *
     * @param mobileCode 验证码
     * @param tradePassword 交易密码
     * @return
     */
    @RequestMapping(value = "/addTradePwd",method = RequestMethod.POST)
    @ApiOperation(value = "设置交易资金密码接口")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobileCode", dataType = "String",value = "手机验证码",required = true),
            @ApiImplicitParam(paramType = "query", name = "tradePassword", dataType = "String",value = "交易密码",required = true)
    })
    public JsonResult addTradePwd(@RequestParam(value = "mobileCode") String mobileCode,
                               @RequestParam("tradePassword") String tradePassword,
                               HttpServletRequest request) {
        UserVo user =  UserUtils.getUser();
        JsonResult jsonResult=new JsonResult(true);
        try {
            if(user.getUserMobile() != null && !user.getUserMobile().trim().equals("")){
                try {
                    MobileCodeToken mobileCodeToken = new MobileCodeToken(mobileCode, user.getUserMobile(), false, request);
                    mobileCodeToken.isValid();
                    jsonResult = userService.addTradePassword(tradePassword);
                    //刷新缓存用户
                    UserUtils.freshCacheUser(user.getUserName());
                }catch (ValidException e){
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("msg_code_error"));
                    return jsonResult;
                }catch (Exception execution){
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg(Language.L_Failed(""));
                    jsonResult.setSuccess(false);
                }
            }else{
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setMsg(Language.L_Failed(""));
                jsonResult.setSuccess(false);
            }
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        }
        return  jsonResult;

    }


    /**
     * 修改登录密码页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/updatepwd")
    public String updatepwd(Model model, HttpServletRequest request) {
        return "member/security/updatepwd";
    }

    /**
     * 修改交易密码修改
     *
     * @param model
     * @return
     */
    @RequestMapping("/rpdatepwd")
    public String rpdatepwd(Model model, HttpServletRequest request) {
        User user = UserUtils.getUser();
        model.addAttribute("userName",user.getUserMobile());
        return "member/security/rpdatepwd";
    }

    /**
     * 添加交易密码修改
     *
     * @param model
     * @return
     */
    @RequestMapping("/settingdatapwd")
    public String setTradePasswd(Model model, HttpServletRequest request) {
        User user = UserUtils.getUser();
        model.addAttribute("userName",user.getUserMobile());
        return "member/security/settingdatapwd";
    }


    /**
     * 邮箱验证
     *
     * @param model
     * @return
     */
    @RequestMapping("/set_email")
    public String set_email(Model model) {
        return "member/security/setemail";
    }

    /**
     * 实名认证
     *
     * @param model
     * @return
     */
    @RequestMapping("/userauth")
    public String userauth(Model model) {
        return "member/security/userauth";
    }

    /**
     * Google双重认证页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/googleauth")
    public String googleAuth(Model model) {
        try {
            String secret = GoogleAuthenticatorUtil.generateSecretKey();
            model.addAttribute("secret", secret);
            model.addAttribute("host","otpauth://totp/"+UserUtils.getUser().getUserName());
            model.addAttribute("issuer",appConfigService.findValueByKey(AppConfig.WEBSITTITLE));
        }catch (Exception e){
            log.error("googleAuth>>>>>>>>>>>"+e.getMessage());
        }
        return "member/security/googleauth";
    }


    /**
     * 安全日志
     *
     * @param request
     * @return
     */
    @RequestMapping("/log")
    public String log(HttpServletRequest request) {
        return "member/security/log";
    }

    /**
     * 修改登录密码
     *
     * @param password
     *          新的登录密码
     * @param oldPassword
     *          旧登录密码
     *
     * @return
     */
    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult editPassword(@RequestParam(value = "password", required = true) String password,
                                   @RequestParam(value = "oldPassword", required = true) String oldPassword
                                ) {
        JsonResult jsonResult = new JsonResult();
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
            return jsonResult;
        }
        jsonResult= userService.updatePassword(userVo.getId(),password, oldPassword);
        return jsonResult;
    }



    /**
     * 绑定邮箱
     *
     * @param email
     * @param code
     * @param request
     * @return
     */
    @RequestMapping(value = "/addEmail", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addEmail(@RequestParam(value = "email", required = true) String email, @RequestParam(value = "code", required = true) String code, HttpServletRequest request) {
        return userService.addEmail(email, code, request);
    }
    /**
     * 银行卡绑定
     *
     * @param model
     * @return
     */
    @RequestMapping("/bindCard")
    public String bindCard(Model model) {
        User user = UserUtils.getUser(false);
        model.addAttribute("user",user);
        if(user.getRealState().equals(User.REALSTATE_SUCCESS)){
            return "member/security/bindCard";
        }else{
            return "member/security/userauth";
        }
    }
    /**
     * 查询银行卡信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/findBindCard")
    @ResponseBody
    public JsonResult findBindCard(Model model) {
        return bankcardService.findByUserId();
    }

    /**
     * 发送邮件邮箱
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "/sendEmail",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult sendEmail(@RequestParam(value = "email", required = true) String email) {
        Map<String, Object> data=new HashedMap();
        return emailMessageService.sendSimpleMail(email, null, Tpl.CODE,data);
    }


    /**
     * 实名认证
     * @param userAuthVo
     * @return
     */
    @RequestMapping(value = "/addUserAuth", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addUserAuth( UserAuthVo userAuthVo) {
        return userService.addUserAuthVo(userAuthVo);
    }
    /**
     * 增加银行卡
     * @param bankcard
     * @return
     */
    @RequestMapping(value = "/addBankCard", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addBankCard(BankCardVo bankcard, String mobileCode, HttpServletRequest request) {
        MobileCodeToken mobileCodeToken = new MobileCodeToken(mobileCode, UserUtils.getUser().getUserMobile(), false, request);
        mobileCodeToken.isValid();
        return bankcardService.addBankCard( bankcard);
    }


    /**
     * 设置谷歌认证
     * @param secret
     * @param googleCode
     * @param password
     * @return
     */

    @RequestMapping(value = "/addGoogleAuth",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addGoogleAuth(@RequestParam(value = "secret", required = true)String secret,
                                    @RequestParam(value = "googleCode", required = true)String googleCode,
                                    @RequestParam(value="password",required = true) String password) {
        return userService.addGoogleAuth(secret,googleCode,password);
    }

    /**
     * 关闭谷歌认证
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateGoogleAuthState",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateGoogleAuthState(@RequestParam(value = "code", required = true)String code,@RequestParam(value = "password", required = true)String password) {
        return userService.updateGoogleAuthState(code,password);
    }
    /**
     * 关闭谷歌认证page
     * @return
     */
    @RequestMapping(value = "/close_ga")
    public String closeGoogleAuth() {
        return "member/security/close_google_auth";
    }

    /**
     * 设置头像
     * @return
     */
    @RequestMapping(value = "/addAvatar",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAvatar(@RequestParam(value = "userAvatar", required = true)String userAvatar) {

        return userService.addAvatar(userAvatar);
    }
}
