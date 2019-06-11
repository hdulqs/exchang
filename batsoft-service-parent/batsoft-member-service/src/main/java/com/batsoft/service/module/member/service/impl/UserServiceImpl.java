/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.service.module.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.enums.AppConfigKeyEnum;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.common.validator.EmailCodeToken;
import com.batsoft.core.common.validator.MobileCodeToken;
import com.batsoft.core.exception.ValidException;
import com.batsoft.core.model.SaveResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.member.PromotionAward;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserAuthVo;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.mq.RabbitMqSender;
import com.batsoft.service.module.member.dao.PromotionAwardDao;
import com.batsoft.service.module.member.dao.UserDao;
import com.batsoft.service.module.member.service.BankcardService;
import com.batsoft.service.module.member.service.PromotionAwardService;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.shiro.PasswordHelper;
import com.batsoft.utils.CardUtil;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.GoogleAuthenticatorUtil;
import com.batsoft.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p> UserServiceImpl </p>
 *
 * @author:
 * @Date :  2017-07-06 17:45:44
 */
@Service("userService")
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {


    @Autowired
    private UserDao userDao;
    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PromotionAwardService promoterAwardService;

    @Autowired
    private AppConfigService appConfigService;

    @Override
    public User findUser(String userName) {
        User user = userDao.findUser(userName);
        return user;
    }

    @Override
    public UserVo findUserInfo(String userName) {
        return userDao.findUserInfo(userName);
    }

    @Override
    public List<User> findUserHavePromotionAward(Date time ,int page,int pageSize) {
        HashMap<String,Object> map = new HashMap();
        map.put("createTime",time);
        int from = (page-1)*pageSize;
        map.put("from",from);
        map.put("pageSize",pageSize);
        return userDao.findUserHavePromotionAward(map);
    }
    @Override
    public Long findUserHavePromotionAwardSum(Date time) {
        HashMap<String,Object> map = new HashMap();
        map.put("createTime",time);
        return userDao.findUserHavePromotionAwardSum(map);
    }

    @Override
    public List<User> findUnKYCUser(HashMap map) {
        return userDao.findUnKYCUser(map);
    }

    @Override
    public Long findUnKYCUserTotal(HashMap map) {
        return userDao.findUnKYCUserTotal(map);
    }

    @Override
    public void updateUser(User user) {
        this.update(user);
        //更新缓存
        UserUtils.clearCache(user);
        UserUtils.getUser();
    }

    /**
     * 是否存在用户
     *
     * @param userName
     * @return
     */
    @Override
    public Boolean hasUser(String userName) {
        boolean has = false;
        User user = this.findUser(userName);
        if (user != null) {
            has = true;
        }
        return has;

    }

    @Override
    public User findUserByPromotionCode(String promotionCode) {
        return userDao.findUserByPromotionCode(promotionCode);
    }

    /**
     * 注册
     * @param userName
     * @param password
     * @param mobileCode
     * @param areaCode
     * @param request
     * @param promoterCode
     * @return
     */
    @Override
    public JsonResult saveUserByMobile(String userName, String password, String mobileCode, String areaCode,String tradePwd,String promoterCode, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String mobile = userName;
            try {
                MobileCodeToken mobileCodeToken = new MobileCodeToken(mobileCode, mobile, false, request);
                mobileCodeToken.isValid();
                // 手机验证码校验
                User user = new User();
                user.setUserName(userName);
                user.setUserMobile(mobile);
                user.setAreaCode(areaCode);
                if(promoterCode != null && !"".equals(promoterCode)) {
                    user.setPromoterParentCode(promoterCode);
                }
                //生成自己的推广码
                char[] heads = new char[2];
                for (int i=0;i<heads.length;i++){
                    int a = 'a'+(int)(Math.random()*26);
                    heads[i] = (char) a;
                }
                String headcode = new String(heads).toUpperCase();
                if(mobile != null && mobile.length() > 4) {
                    user.setPromoterCode(headcode.concat(mobile.substring(mobile.length() - 4, mobile.length())));
                }else{
                    user.setPromoterCode(mobile);
                }
                //加密密码
                PasswordHelper passwordHelper = new PasswordHelper();
                PasswordHelper.PasswordInfo passwordInfo = passwordHelper.encryptPassword(password);
                user.setPassword(passwordInfo.getPassword());
                user.setSalt(passwordInfo.getSalt());
                //设置交易密码
                String tradePassWord = passwordHelper.encryString(tradePwd,passwordInfo.getSalt());
                user.setTradePassword(tradePassWord);

                //数据校验
                if (!beanValidatorForJson(user)) {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("msg_data_error"));
                    return jsonResult;
                }

                //保存用户
                SaveResult result = save(user);
                User resultUser = new User();
                resultUser.setUserName(userName);
                resultUser.setId(result.getId());
                resultUser.setUserMobile(mobile);
                resultUser.setPromoterCode(user.getPromoterCode());
                if(user.getPromoterParentCode()!=null) {
                    resultUser.setPromoterParentCode(user.getPromoterParentCode());
                }
                //如果有推广码，那么写推广表
                if(promoterCode != null){
                    User promoterUser = findUserByPromotionCode(promoterCode);
                    if(promoterUser != null){
                        PromotionAward promotionAward = new PromotionAward();
                        String value = appConfigService.findValueByKey(AppConfigKeyEnum.promotionAward.getCode());
                        BigDecimal promotionAwardBig = new BigDecimal(10);
                        if(value!=null){
                            promotionAwardBig = new BigDecimal(value);
                        }
                        promotionAward.setAward(promotionAwardBig);
                        String promotionName = user.getUserName();
                        if(promotionName.length()>7){
                            promotionName = promotionName.substring(0,3).concat("****").concat(promotionName.substring(promotionName.length()-4,promotionName.length()));
                        } else {
                            if(promotionName.length()>3) {
                                promotionName = promotionName.substring(0, 2).concat("****").concat(promotionName.substring(promotionName.length()-1,promotionName.length()));
                            }
                        }
                        promotionAward.setPromoterName(promotionName);
                        promotionAward.setCreateTime(new Date());
                        promotionAward.setPromoterId(result.getId());
                        promotionAward.setPromoterParentId(promoterUser.getId());
                        promotionAward.setStatus(0);
                        promoterAwardService.save(promotionAward);
                        if(promoterUser.getPromoterParentCode() != null){
                            User promoterParentUser = findUserByPromotionCode(promoterUser.getPromoterParentCode());
                            if(promoterParentUser !=null ){
                                String newValue = appConfigService.findValueByKey(AppConfigKeyEnum.promotionParentAward.getCode());
                                PromotionAward  newPromotionAward = new PromotionAward();
                                BigDecimal  newpromotionAwardBig = new BigDecimal(5);
                                if( newValue != null){
                                    newpromotionAwardBig = new BigDecimal(newValue);
                                }
                                newPromotionAward.setAward(newpromotionAwardBig);
                                newPromotionAward.setPromoterName(promotionName);
                                newPromotionAward.setCreateTime(new Date());
                                newPromotionAward.setPromoterId(result.getId());
                                newPromotionAward.setPromoterParentId(promoterParentUser.getId());
                                newPromotionAward.setStatus(0);
                                promoterAwardService.save(newPromotionAward);
                            }
                        }
                    }
                }
                jsonResult.setData(resultUser);
                jsonResult.setSuccess(true);
                jsonResult.setMsg(Language.L_Success("msg_register"));
                //发送注册成功消息
                rabbitMqSender.toRegisterInit(JSON.toJSONString(resultUser));
                return jsonResult;
            } catch (ValidException e) {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("msg_code_error"));
                return jsonResult;
            } catch (Exception e) {
                e.printStackTrace();
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L_Failed("msg_register"));
            }
            return jsonResult;
    }


    @Override
    public JsonResult saveUserByEmail(String userName, String password, HttpServletRequest request) {
        return null;
    }

    @Override
    public JsonResult addTradePassword(String password) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            //加密密码
            PasswordHelper passwordHelper = new PasswordHelper();
            user.setTradePassword(passwordHelper.encryString(password, user.getSalt()));
            //清除缓存
            redisService.delRedisByKey(UserUtils.TRADE_PASSWD_KEY+user.getId());
            this.updateUser(user);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
        } catch (Exception e) {
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }
    @Override
    public JsonResult addPassword(User user, String password) {
        JsonResult jsonResult = new JsonResult();
        try {
            //这里写错了
//            User user = UserUtils.getUser();
            //加密密码
            PasswordHelper passwordHelper = new PasswordHelper();
            user.setPassword(passwordHelper.encryString(password, user.getSalt()));
            this.updateUser(user);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
        } catch (Exception e) {
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult updateTradePassword(String password) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            //加密交易密码
            PasswordHelper passwordHelper = new PasswordHelper();
            user.setTradePassword(passwordHelper.encryString(password, user.getSalt()));
            //清除redis缓存
            redisService.delRedisByKey(UserUtils.TRADE_PASSWD_KEY+user.getId());
            this.updateUser(user);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
        } catch (Exception e) {
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult updatePassword(String userId,String password, String oldPassword) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = userDao.findUserById(userId);
            //加密旧密码
            PasswordHelper passwordHelperOld = new PasswordHelper();
            String pd = passwordHelperOld.encryString(oldPassword, user.getSalt());
            if (!user.getPassword().equals(pd)) {
                jsonResult.setCode(Constants.FAILED);
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("msg_old_password_error"));
                return jsonResult;
            }
            //加密新密码
            PasswordHelper passwordHelper = new PasswordHelper();
            user.setPassword(passwordHelper.encryString(password, user.getSalt()));
            this.updateUser(user);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
        } catch (Exception e) {
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult addEmail(String email, String code, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        try {
            // 验证码校验
            EmailCodeToken emailCodeToken = new EmailCodeToken(email, code, false, request);
            emailCodeToken.isValid();
            User user = UserUtils.getUser();
            user.setUserEmail(email);
            //邮箱绑定用户
            this.updateUser(user);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
            return jsonResult;
        } catch (ValidException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
            return jsonResult;
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 修改登录人的信息
     *
     * @return
     */
    @Override
    @Async
    public void updatUserLogin(HttpServletRequest request) {

        try {
            User user = UserUtils.getUser(false);
            if (user != null) {
                user.setOldLoginTime(user.getLoginTime() == null ? DateUtils.getNowTimesTamp() : user.getLoginTime());//上次登陆时间
                user.setLoginTime(DateUtils.getNowTimesTamp());//最后登陆时间
                user.setLoginNum(user.getLoginNum() + 1);
                String oldLoginIp = user.getLoginIp();
                user.setOldLoginIp(user.getLoginIp() == null ? IpUtil.getIpAddr(request) : user.getLoginIp());
                user.setLoginIp(IpUtil.getIpAddr(request));
                String tradePwdRedisKey = UserUtils.TRADE_PASSWD_KEY+user.getId();
                if ( user.getLoginIp() == null ){
                    redisService.delRedisByKey(tradePwdRedisKey);
                } else  if( oldLoginIp != null && user.getLoginIp() != null ){
                     if( !oldLoginIp.equals(user.getLoginIp()) ){
                         redisService.delRedisByKey(tradePwdRedisKey);
                     }
                }
                this.updateUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updatUserLogin err>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
        }
    }

    @Override
    public JsonResult addUserAuthVo(UserAuthVo userAuthVo) {
        JsonResult jsonResult = new JsonResult();
        String realName;
        String areaCode;
        Integer userCardType;
        String userCardNumber;
        String userCardFront;
        String userCardBack;
        String userCardAll;
        String country;
        Integer sex;
        User user = UserUtils.getUser();
        try {
            if (User.USERCARDTYPE0.equals(userAuthVo.getIdentity_type())) {//身份证类型验证
                realName = userAuthVo.getFirst_name() + userAuthVo.getLast_name();
                areaCode = "86";
                userCardType = userAuthVo.getIdentity_type();
                userCardNumber = userAuthVo.getIdentity();
                Long excardNoMber = findUserByCard(user.getId(),userCardNumber);
                if(excardNoMber!=0){
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("cardno_have_verify"));
                    return jsonResult;
                }
                userCardFront = userAuthVo.getFront_pic1();
                userCardBack = userAuthVo.getBack_pic1();
                userCardAll = userAuthVo.getHand_pic1();
                country = "中国";
                // 通过身份证号计算 先写死
                if (userCardNumber.length() > 15) {
                    sex = Integer.valueOf(((Map) CardUtil.getCarInfo(userCardNumber)).get("sex").toString());
                } else {
                    sex = Integer.valueOf(((Map) CardUtil.getCarInfo15W(userCardNumber)).get("sex").toString());
                }
            } else {
                realName = userAuthVo.getLast_name_en() + userAuthVo.getFirst_name_en();
                areaCode = "";
                userCardType = userAuthVo.getIdentity_type();
                userCardNumber = userAuthVo.getIdentity_en();
                Long excardNoMber = findUserByCard(user.getId(),userCardNumber);
                if(excardNoMber!=0){
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("cardno_have_verify"));
                    return jsonResult;
                }
                userCardFront = userAuthVo.getFront_pic_en1();
                userCardBack = userAuthVo.getBack_pic_en1();
                userCardAll = userAuthVo.getHand_pic_en1();
                country = userAuthVo.getCountry();
                sex = userAuthVo.getSex();
            }
            jsonResult = addUserAuth(realName, areaCode, userCardType, userCardNumber, userCardFront, userCardBack, userCardAll, country, sex);
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(Language.L_Failed(""));
        }
        return jsonResult;
    }


    @Override
    public Long findUserByCard(String id,String card) {
        HashMap map = new HashMap();
        map.put("id",id);
        map.put("card",card);
        return   userDao.findUserByCard(map);
    }

    @Override
    public JsonResult addUserAuth(String realName, String areaCode, Integer userCardType, String userCardNumber, String userCardFront, String userCardBack, String userCardAll, String country, Integer sex) {
        User user = UserUtils.getUser(false);
        JsonResult jsonResult = new JsonResult();
        try {
            if (null != user) {
                if (user.getRealState().equals(User.REALSTATE_NORMAL)|| user.getRealState().equals(User.REALSTATE_FAILE)) {
                    user.setRealName(realName);
                    user.setAreaCode(areaCode);
                    user.setUserCardType(userCardType);
                    user.setUserCardNumber(userCardNumber);
                    user.setUserCardFront(userCardFront);
                    user.setUserCardBack(userCardBack);
                    user.setUserCardAll(userCardAll);
                    user.setRealState(User.REALSTATE_WAITING);
                    user.setSex(sex);
                    user.setCountry(country);
                    this.updateUser(user);
                    jsonResult.setSuccess(true);
                    jsonResult.setCode(Constants.SUCCESS);
                    jsonResult.setMsg(Language.L("msg_user_auth_commit_success"));
                } else if(user.getRealState().equals(User.REALSTATE_WAITING)) {
                    jsonResult.setSuccess(false);
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg(Language.L("msg_user_auth_commit"));
                } else if(user.getRealState().equals(User.REALSTATE_SUCCESS)){
                    jsonResult.setSuccess(false);
                    jsonResult.setCode(Constants.FAILED);
                    jsonResult.setMsg(Language.L("msg_user_auth_exists"));
                }
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(Language.L_Failed(""));
            log.error("实名认证异常>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
        }
        return jsonResult;
    }
    @Override
    public JsonResult updateRealState(String[] pks,String agree,String msg){
        JsonResult jsonResult = new JsonResult();
        if(pks.length == 1){
            try{
                User user  = this.get(pks[0]);
                if(user == null){
                    return super.createJsonResult(false,Language.L("msg_user_no_exists"));
                }
                if(Boolean.toString(Boolean.FALSE).equalsIgnoreCase(agree)){
                    user.setRealState(User.REALSTATE_FAILE);
                    user.setRealStateRemark(msg);
                    int result = this.update(user);
                    if(result == 1){
                        return super.createJsonResult(true,Language.L("msg_user_auth_success"));
                    } else {
                        return super.createJsonResult(true,Language.L("msg_user_auth_fail"));
                    }
                } else {
                    user.setRealState(User.REALSTATE_SUCCESS);
                    int result = this.update(user);
                    if(result == 1){
                        List<PromotionAward> promotionAwards = promoterAwardService.getParentPromotionIdList(user.getId());
                        if(!promotionAwards.isEmpty()){
                            for (PromotionAward promotionAward:promotionAwards){
                                UserVo parentUser = findUserById(promotionAward.getPromoterParentId());
                                if(parentUser!=null){
                                    if(parentUser.getPromotionAward() == null) {
                                      parentUser.setPromotionAward(new BigDecimal(0));
                                    }
                                    BigDecimal award = parentUser.getPromotionAward().add(promotionAward.getAward());
                                    parentUser.setPromotionAward(award);
                                   int res =  update(parentUser);
                                   if(res == 1){
                                       promotionAward.setStatus(1);
                                       promoterAwardService.update(promotionAward);
                                   }
                                }
                            }
                        }
                        return super.createJsonResult(true,Language.L("msg_user_auth_success"));
                    } else {
                        return super.createJsonResult(true,Language.L("msg_user_auth_fail"));
                    }
                }
            }catch (Exception e){
                jsonResult.setMsg(Language.L("msg_system_error"));
                jsonResult.setSuccess(false);
                e.printStackTrace();
            }
        }else {
            ArrayList<String> arrayList = new ArrayList<>();
            for (String id:pks) {
                arrayList.add(id);
            }
            int column =  userDao.updateUserByUserIdSetRealStateSuccess(arrayList);
            if(column == arrayList.size()){
                jsonResult.setSuccess(true);
                jsonResult.setMsg(Language.L("msg_user_auth_success"));
            } else {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("msg_user_auth_fail"));
            }
        }
        return jsonResult;
    }


    @Override
    public JsonResult addGoogleAuth(String secret, String googleCode, String password) {
        JsonResult jsonResult = new JsonResult();

        if (checkPassword(password)) {
            if (checkGoogleAuth(secret, googleCode)) {
                User user = UserUtils.getUser();
                if (null != user) {
                    user.setGoogleCode(secret);
                    user.setOpenGoogleAuth(User.OPENGOOGLEAUTH1);
                    this.updateUser(user);
                    jsonResult.setSuccess(true);
                    jsonResult.setMsg(Language.L_Success(""));

                } else {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("msg_data_error"));
                }
            } else {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L_Failed(""));
            }
        } else {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L("msg_password_error"));
        }
        return jsonResult;
    }


    @Override
    public JsonResult updateGoogleAuthState(String code, String password) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            if (checkPassword(password)) {
                if (checkGoogleAuth(user.getGoogleCode(), code)) {
                    user.setGoogleCode("");
                    user.setOpenGoogleAuth(User.OPENGOOGLEAUTH0);
                    this.updateUser(user);
                    jsonResult.setSuccess(true);
                    jsonResult.setMsg(Language.L_Success("google_close_success"));
                } else {
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(Language.L("msg_valid_error"));
                }
            } else {
                jsonResult.setSuccess(false);
                jsonResult.setMsg(Language.L("msg_password_error"));
            }
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L_Failed(""));
        }

        return jsonResult;
    }


    @Override
    public boolean checkGoogleAuth(String secret, String googleCode) {
        long code = Long.parseLong(googleCode);
        long t = System.currentTimeMillis();
        GoogleAuthenticatorUtil googleAuth = new GoogleAuthenticatorUtil();
        googleAuth.setWindowSize(15); // should give 5 * 30 seconds of grace...
        boolean r = googleAuth.check_code(secret, code, t);
        return r;
    }

    @Override
    public JsonResult addAvatar(String userAvatar) {
        JsonResult jsonResult = new JsonResult();
        try {
            User user = UserUtils.getUser();
            user.setUserAvatar(userAvatar);
            this.updateUser(user);
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L_Failed(""));
        }
        return jsonResult;
    }

    @Override
    public UserVo findUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public boolean checkPassword(String password) {
        try {
            User user = this.findUser(UserUtils.getUser().getUserName());
            PasswordHelper passwordHelper = new PasswordHelper();
            String pass = passwordHelper.encryString(password, user.getSalt());
            if (user.getPassword().equals(pass)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("密码校验异常>>>>>>>>>>>>>>>>>>>>>>" + e.getMessage());
            return false;
        }
    }

    @Override
    public JsonResult findUserNum(){
        JsonResult jsonResult = new JsonResult();
        try{
            jsonResult = super.createJsonResult(true,null);
            jsonResult.setData(userDao.findUserNum());
        }catch (Exception e){
            jsonResult = super.createJsonResult(false,null);
            jsonResult.setData(0);
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    public JsonResult findUnauditedUserNum(){
        JsonResult jsonResult = new JsonResult();
        try{
            jsonResult = super.createJsonResult(true,null);
            jsonResult.setData(userDao.findUnauditedUserNum());
        }catch (Exception e){
            jsonResult = super.createJsonResult(false,null);
            jsonResult.setData(0);
            e.printStackTrace();
        }
        return jsonResult;
    }


}
