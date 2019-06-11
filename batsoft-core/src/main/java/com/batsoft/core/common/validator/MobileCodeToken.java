package com.batsoft.core.common.validator;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.exception.ValidException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;

/**
 * 令牌类
 * @author Bat Admin
 * @version 2017-5-19
 */
@Data
public class MobileCodeToken implements ValidToken {
	private static final long serialVersionUID = 1L;
	private String code;
	private String mobile;
	private boolean mobileLogin;
	private ServletRequest request;

	private RedisService redisService;
	public MobileCodeToken() {
		super();
	}

	public MobileCodeToken(String code,String mobile, boolean mobileLogin, ServletRequest request) {
		this.code = code;
		this.mobile=mobile;
		this.mobileLogin = mobileLogin;
		this.request=request;
	}


	/**
	 * 校验短信验证码
	 * @throws ValidException
	 */
	@Override
	public void isValid() throws ValidException {
		isValid(false);
	}

	public void isValid(boolean deleteCache) throws ValidException {
		redisService = (RedisService) SpringContextUtil.getBean("redisService");
		String redis_code = redisService.get(ApplicationConfigure.mobileCode + ":" + mobile);
		if( redis_code != null && code != null && code.equals(redis_code) ){
				redisService.delRedisByKey(ApplicationConfigure.mobileCode + ":" + mobile);
		} else {
			if( deleteCache ){
				redisService.delRedisByKey(ApplicationConfigure.mobileCode + ":" + mobile);
			}
			throw new ValidException(Language.L_Failed("register.telephone_verification",true));
		}
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

}