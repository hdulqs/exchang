package com.batsoft.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.batsoft.common.base.BaseController;
import com.batsoft.common.util.IpAdrressUtil;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.exception.ValidException;
import com.batsoft.model.module.exchange.vo.CustomerAccountVo;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.exchange.service.CustomerAccountService;
import com.batsoft.service.module.exchange.trade.util.RedisUserUtil;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.shiro.UsernamePasswordToken;
import com.batsoft.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

@Api(value = "UserController",description = "用户Controller")
@RequestMapping(value = "/user")
@RestController
@Slf4j
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private RestTemplate remoteRestTemplate ;

    @Autowired
    private RedisService redisService;

    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();

    @Autowired
    private CustomerAccountService customerAccountService;


    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "手机号",required = true),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "密码",required = true),
            @ApiImplicitParam(paramType = "query", name = "mobileCode", dataType = "String",value = "手机验证码",required = true),
            @ApiImplicitParam(paramType = "query", name = "areaCode", dataType = "String",value = "地区码",required = false),
            @ApiImplicitParam(paramType = "query", name = "tradePwd", dataType = "String",value = "交易密码",required = true),
            @ApiImplicitParam(paramType = "query", name = "promoterCode", dataType = "String",value = "推广人码",required = false)
    })
    public JsonResult register(@RequestParam(value = "userName") String userName,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "mobileCode") String mobileCode,
                               @RequestParam(value = "areaCode",required = false) String areaCode,
                               @RequestParam(value = "tradePwd") String tradePwd,
                               @RequestParam(value = "promoterCode",required = false) String promoterCode, HttpServletRequest request){
        JsonResult jsonResult = new JsonResult(false);
        Boolean hasUser = userService.hasUser(userName);
        if(hasUser){
            return new JsonResult(Boolean.FALSE, Language.L("msg_user_exists"), JsonResult.ResultCode.FAILE);
        }
        jsonResult = userService.saveUserByMobile(userName, password, mobileCode, areaCode,tradePwd,promoterCode ,request);
        if(jsonResult.getSuccess()){
            try {
                String mallUrl = appConfigService.findValueByKey(AppConfig.MALLURL);
                String mallPort = appConfigService.findValueByKey(AppConfig.MALLPORT);
                String shopPrefix = mallUrl + mallPort;
                URI uri = IpAdrressUtil.stringParse2URI(shopPrefix + "/mall/backend/access/addCustomerUserFormBttmall");
                HttpHeaders headers = new HttpHeaders();
                MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                headers.setContentType(type);
                JSONObject jsonObj = JSONObject.fromObject(jsonResult.getData());
                HttpEntity<String> req = new HttpEntity<String>(jsonObj.toString(),headers);
                ResponseEntity<String> remoteResponse =	remoteRestTemplate.exchange(uri, HttpMethod.POST,req,String.class);
                com.alibaba.fastjson.JSONObject resultJson = com.alibaba.fastjson.JSONObject.parseObject(remoteResponse.getBody());
                if (resultJson.isEmpty() || !Boolean.valueOf(resultJson.get("success").toString())) {
                    jsonResult.setMsg(Language.L_Failed("login.register_error_contact_customer_service"));
                    log.error("用户注册发送给商户端注册失败，用户id["+((User)jsonResult.getData()).getId()+"]");
                }
            } catch (Exception e){
                e.printStackTrace();
                jsonResult.setMsg(Language.L_Failed("login.register_error_contact_customer_service"));
                log.error("用户注册发送给商户端注册失败，用户id["+((User)jsonResult.getData()).getId()+"]",e);
            }
        }
        return jsonResult;
    }




    /**
     * 忘记密码 - 修改密码
     *
     * @param userName 用户名 =手机号
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "忘记登陆密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", dataType = "String",value = "用户名",required = true),
            @ApiImplicitParam(paramType = "query", name = "code", dataType = "String",value = "验证码",required = true),
            @ApiImplicitParam(paramType = "query", name = "type", dataType = "String",value = "验证码类型 [google/sms]",required = true),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "新密码",required = true)
    })
    @RequestMapping(value = "/forgotPassword",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult forgotPassword(@RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "code") String code,
                                     @RequestParam(value = "type") String type,
                                     @RequestParam(value = "password") String password,
                                     HttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //通过用户名查询用户是否存在
        User user = userService.findUser(userName);
        if (user != null) {
            boolean googleAuth = false;
            boolean mobileAuth = false;
            if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(code)) {
                if ("google".equals(type)) { //如果是google验证码
                    if (StringUtils.isNotEmpty(user.getGoogleCode())) {
                        googleAuth = userService.checkGoogleAuth(user.getGoogleCode(), code);
                    } else {
                        jsonResult.setSuccess(false);
                        jsonResult.setCode(JsonResult.ResultCode.FAILE);
                        jsonResult.setMsg(Language.L("google_unbound"));  //用户google验证码不存在
                        return jsonResult;
                    }
                } else {
                    try {
                        // 手机验证码校验
                        MobileCodeToken mobileCodeToken = new MobileCodeToken(code, userName, false, request);
                        mobileCodeToken.isValid();
                        mobileAuth = true;
                    }catch (Exception e){
                        jsonResult.setSuccess(false);
                        jsonResult.setMsg(e.getMessage());
                        return jsonResult;
                    }
                }
            }
            if (googleAuth || mobileAuth) {
                jsonResult = userService.addPassword(user, password);
            } else {
                jsonResult.setMsg(Language.L("msg_code_error"));
                jsonResult.setSuccess(false);
                jsonResult.setCode(JsonResult.ResultCode.FAILE);
            }
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.FAILE);
            jsonResult.setMsg(Language.L("msg_code_error"));  //用户不存在
        }
        Map<String, String> data = new HashedMap();
        data.put("loginUrl", "/login");
        jsonResult.setData(data);
        jsonResult.setSuccess(true);
        return jsonResult;
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
        JsonResult jsonResult=new JsonResult(true);
        UserVo user = UserUtils.getUser(false);
        if(user == null || user.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
            return jsonResult;
        }
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
     * 修改登录密码
     *
     * @param password
     *          新的登录密码
     * @param oldPassword
     *          旧登录密码
     *
     * @return
     */
    @ApiOperation(value = "修改登录密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String",value = "新密码",required = true),
            @ApiImplicitParam(paramType = "query", name = "oldPassword", dataType = "String",value = "旧密码",required = true)
    })
    @ResponseBody
    public JsonResult editPassword(@RequestParam(value = "password") String password,
                                   @RequestParam(value = "oldPassword") String oldPassword
    ) {
        JsonResult jsonResult = new JsonResult(false);
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
        JsonResult jsonResult=new JsonResult(true);
        UserVo user = UserUtils.getUser(false);
        if(user == null || user.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
            return jsonResult;
        }
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

    @ApiOperation(value = "用户中心接口")
    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult userCenter(){
        JsonResult jsonResult = new JsonResult(false);
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
         jsonResult.setSuccess(false);
         jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
         jsonResult.setMsg(Language.L_Failed("msg_no_login"));
         return jsonResult;
        }
        BigDecimal total = new BigDecimal(0);
        Jedis jedis = jedisClient.getJedis();
        List<CustomerAccountVo> customerAccountVos = RedisUserUtil.getAllCoinsAccount(UserUtils.getUser().getId());
        for (CustomerAccountVo accountVo:customerAccountVos){
            if(accountVo.getCoinCode()!=null && accountVo.getAvailable() !=null && accountVo.getFreeze()!=null){
                BigDecimal totalMoney =accountVo.getAvailable().add(accountVo.getFreeze());
                BigDecimal estimatedValue = totalMoney.multiply(RedisUserUtil.covertToUSDT(jedis,accountVo.getCoinCode(),"USDT")); //改币种的预估值 （保留两位小数）
                total = total.add(estimatedValue);
            }
        }
        jedisClient.close(jedis);
        HashMap<String,Object> map = new HashMap<>();
        map.put("user",userVo);
        map.put("totalMoney",total.setScale(2,BigDecimal.ROUND_HALF_UP));
        jsonResult.setSuccess(true);
        jsonResult.setData(map);
        return jsonResult;
    }

    /**
     * 我的资产 --个人中心--正常币种
     *
     * @return
     */
    @ApiOperation(value = "我的资产")
    @RequestMapping(value = "/financeData",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult financeData() {
        JsonResult jsonResult = new JsonResult();
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
        BigDecimal total = new BigDecimal(0);
        Jedis jedis = jedisClient.getJedis();
        List<CustomerAccountVo> accounts = customerAccountService.findCoinAccounts(UserUtils.getUser().getId());
        try {
            List<CustomerAccountVo> customerAccountVos = RedisUserUtil.getAllCoinsAccount(UserUtils.getUser().getId());
            for (CustomerAccountVo accountVo:customerAccountVos){
                CustomerAccountVo customerAccountVo =  accounts.stream().filter(new Predicate<CustomerAccountVo>() {
                    @Override
                    public boolean test(CustomerAccountVo customerAccountVo) {
                        if(customerAccountVo.getCoinCode() != null && customerAccountVo.getCoinCode().equals(accountVo.getCoinCode())){
                            return true;
                        }
                        return false;
                    }
                }).findFirst().get();
                accountVo.setAllowWithdraw(customerAccountVo.getAllowWithdraw());
                accountVo.setAllowRecharge(customerAccountVo.getAllowRecharge());
                accountVo.setCoinLogo(customerAccountVo.getCoinLogo());
            }
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(customerAccountVos));
            DecimalFormat decimalFormat = new DecimalFormat(Constants.MONEY_FORMAT);
            for (Object o : jsonArray) {
                com.alibaba.fastjson.JSONObject jo = (com.alibaba.fastjson.JSONObject) o;
                if(jo.get("coinCode")!=null && jo.get("available") !=null && jo.get("freeze")!=null){
                    BigDecimal totalMoney = new BigDecimal(jo.get("available").toString()).add(new BigDecimal(jo.get("freeze").toString()));
                    BigDecimal estimatedValue = totalMoney.multiply(RedisUserUtil.covertToUSDT(jedis,jo.get("coinCode").toString(),"USDT")); //改币种的预估值 （保留两位小数）
                    total = total.add(estimatedValue);
                    jo.put("estimatedValue", decimalFormat.format(estimatedValue));
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("totalMoney",total.setScale(2,BigDecimal.ROUND_HALF_UP));
            map.put("customerAccountVos", jsonArray);
            jsonResult.setData(map);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
        } finally {
            jedisClient.close(jedis);
        }
        return jsonResult;
    }

    @ApiOperation(value = "退出接口")
    @RequestMapping(value = "/logOut",method = RequestMethod.POST)
    private JsonResult logOut(){
        JsonResult jsonResult = new JsonResult(false);
        UserVo appUser = UserUtils.getUser();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //读取商城的redis token，如果有那么从redis删除
        Object tokenKey =  redisService.getObject("userToken:"+appUser.getUserName());
        if(tokenKey!=null){
            redisService.delRedisByKey("userToken:"+appUser.getUserName());
            redisService.delRedisByKey(tokenKey.toString());
        }
        jsonResult.setSuccess(true);
        jsonResult.setMsg(Language.L("logout"));
        jsonResult.setCode(JsonResult.ResultCode.SUCCESS);
        return  jsonResult;
    }


}
