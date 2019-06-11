package com.batsoft.core.common.validator;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.exception.ValidException;
import lombok.Data;

import javax.servlet.ServletRequest;

/**
 * 邮箱验证码令牌类
 * @author Bat Admin
 * @version 2017-5-19
 */
@Data
public class EmailCodeToken implements ValidToken {
	private static final long serialVersionUID = 1L;
	private String code;
	private String email;
	private boolean emailLogin;
	private ServletRequest request;

	private RedisService redisService;
	public EmailCodeToken() {
		super();
	}

	public EmailCodeToken( String email,String code, boolean emailLogin, ServletRequest request) {
		this.code = code;
		this.email=email;
		this.emailLogin = emailLogin;
		this.request=request;
	}


	/**
	 * 校验邮箱验证码
	 * @throws ValidException
	 */
	@Override
	public void isValid() throws ValidException {
		// 增加判断验证码逻辑
		String code = this.code;
		redisService=(RedisService) SpringContextUtil.getBean("redisService");
		String redis_code = redisService.get(ApplicationConfigure.emailCode + ":" + this.email);
		if (null == redis_code || null == redis_code) {
			throw new ValidException("验证码失效！");
		} else if (null == code || !code.equalsIgnoreCase(redis_code)) {
			throw new ValidException("验证码错误！");
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