package com.batsoft.service.module.member.service;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;

import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.model.module.member.Principal;
import com.batsoft.model.module.member.User;
import com.batsoft.model.module.member.vo.UserVo;


/**
 * 用户工具类
 */
public class UserUtils {

	private static UserService userService = SpringContextUtil.getBeanByClass(UserService.class);
	private static RedisService redisService=SpringContextUtil.getBeanByClass(RedisService.class);
	public static final String MEMBER_USER_CACHE = "memberUserCache:";
	public static final String MEMBER_USER_CACHE_LOGIN_NAME_ = "ln:";
	/**
	 * redis存储的键值 在购买b币的时候有读取 需要同步
	 */
	public static final String TRADE_PASSWD_KEY = "exchange:tradepassword:";
	/**
	 * 根据登录名获取用户
	 * @param userName
	 * @return 取不到返回null
	 */
	public static UserVo getByUserName(String userName) {
		UserVo user = (UserVo) getCache(MEMBER_USER_CACHE + MEMBER_USER_CACHE_LOGIN_NAME_ + userName);
		if (user == null){
			user = userService.findUserInfo(userName);
			if (user == null){
				return null;
			}
			putCache(MEMBER_USER_CACHE + MEMBER_USER_CACHE_LOGIN_NAME_ + user.getUserName(), user);
		}
		return user;
	}

	/**
	 * 刷新用户缓存
	 * @param userName
	 * @return
	 */
	public static void freshCacheUser(String userName) {
		UserVo	user = userService.findUserInfo(userName);
		if (user != null){
			putCache(MEMBER_USER_CACHE + MEMBER_USER_CACHE_LOGIN_NAME_ + user.getUserName(), user);
		}
	}


	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){

		removeCache(MEMBER_USER_CACHE + MEMBER_USER_CACHE_LOGIN_NAME_ + getUser().getUserName());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user) {
		removeCache(MEMBER_USER_CACHE + MEMBER_USER_CACHE_LOGIN_NAME_ + user.getUserName());
	}


	/**
	 * 获取当前用户
	 * @return 取不到返回 new Admin()
	 */
	public static UserVo getUser() {
		Principal principal = getPrincipal();
		if (principal!=null){
			UserVo user = getByUserName(principal.getUserName());
			if (user != null){
				return user;
			}
			return new UserVo();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new UserVo();
	}

	/**
	 * 获取当前用户
	 * @return 取不到返回 new Admin()
	 */
	public static UserVo getUser(boolean cache) {
		if(cache){
			return  getUser();
		}else {
			Principal principal = getPrincipal();
			if (principal != null) {
				UserVo user  = userService.findUserInfo(principal.getUserName());;
				if (user != null){
					return user;
				}
				return new UserVo();
			}
			return new UserVo();
		}
	}
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal() {
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (Exception e){
			
		}
		return null;
	}
	

	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj =redisService.getObject(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
		redisService.setObject(key, value,3*60*60*24);
	}

	public static void removeCache(String key) {
		redisService.delRedisByKey(key);
	}

	
}
