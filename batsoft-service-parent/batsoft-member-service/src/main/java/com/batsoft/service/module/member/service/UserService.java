/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-07-06 17:45:44 
*/
package com.batsoft.service.module.member.service;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserAuthVo;
import com.batsoft.model.module.member.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
* <p>UserService</p>
* @author: Bat Admin
* @Date :  2017-07-06 17:45:44 
*/
public interface UserService extends BaseService<User, String>{
    /**
     * 用户信息
     * @param userName
     * @return
     */
    User findUser(String userName);

    /**
     *
     * @param promotionCode
     * @return
     */
    User findUserByPromotionCode(String promotionCode);
    /**
     * 用户安全认证信息
     * @param userName
     * @return
     */
    UserVo findUserInfo(String userName);

    List<User> findUserHavePromotionAward(Date time , int page, int pageSize);
    Long findUserHavePromotionAwardSum(Date time);
    List<User> findUnKYCUser(HashMap map);

    Long findUnKYCUserTotal(HashMap map);
    /**
     * 更新用户信息
     * @param user
     */
   void updateUser(User user);

    /**
     * 通过手机号注册
     * @param userName
     * @param password
     * @param mobileCode
     * @param areaCode
     * @param request
     * @return
     */
   JsonResult saveUserByMobile(String userName, String password, String mobileCode, String areaCode,String tradePwd,String prometorCode, HttpServletRequest request);

   JsonResult saveUserByEmail(String userName, String password, HttpServletRequest request);

    /**
     * 设置资金密码
     * @param password
     * @return
     */
   JsonResult addTradePassword(String password);

    /**
     * 设置登录密码
     * @param user
     * @param password
     * @return
     */
    JsonResult addPassword(User user, String password);

    /**
     * 修改资金密码
     * @param password
     * @return
     */
    JsonResult updateTradePassword(String password);
    /**
     * 修改登录密码
     * @param userId
     * @param password
     * @param oldPassword
     * @return
     */
    JsonResult updatePassword(String userId,String password, String oldPassword);

    /**
     * 设置邮箱
     * @param email
     * @param code
     * @return
     */
    JsonResult addEmail(String email, String code, HttpServletRequest request);

    /**
     * 修改登录人信息
     */
    void updatUserLogin(HttpServletRequest request);

    /**
     * 实名认证 直接传值
     * @param realName 证件姓名
     * @param areaCode 地区号 用于发短信
     * @param userCardType 证件类型
     * @param userCardNumber 证件号
     * @param userCardFront 证件正面照
     * @param userCardBack 证件反面照
     * @param userCardAll 手持证件照
     * @param country 国家
     * @param sex 性别
     * @return
     */
    JsonResult addUserAuth(String realName, String areaCode, Integer userCardType, String userCardNumber, String userCardFront, String userCardBack, String userCardAll,String country,Integer sex);

    /**
     * 实名认证 通过UserAuthVo 传值
     * @param userAuthVo
     * @return
     */
    JsonResult addUserAuthVo(UserAuthVo userAuthVo);


    Long findUserByCard(String id,String  card);

    /**
     * 增加google 认证
     * @param secret
     * @param googleCode
     * @param password
     * @return
     */
    JsonResult addGoogleAuth(String secret, String googleCode,String password);
    /**
     * 关闭 认证
     * @param code
     * @param  password
     * @return
     */
    JsonResult updateGoogleAuthState(String code,String password);

    boolean checkGoogleAuth(String checkCode, String googleCode);

    /**
     * 设置头像
     * @param userAvatar
     * @return
     */
    JsonResult addAvatar(String userAvatar);

    /**
     * 通过id 查询对象 主要查询一些非关键信息
     * @param id
     * @return
     */
    UserVo findUserById(String id);

    /**
     * 校验密码是否正确
     * @param password
    * @return
     */
    boolean checkPassword (String password);

    /**
     * 查询用户数量
     * @return
     */
    JsonResult findUserNum();
    /**
     * 未审核用户数量
     * @return
     */
    JsonResult findUnauditedUserNum();

    /**
     * 身份审核通过
     * @param pks
     * @return
     */
    JsonResult updateRealState(String[] pks,String agree,String msg);

    /**
     * hasUser
     * @param name
     * @return boolean
     */
    Boolean hasUser(String name);

}
