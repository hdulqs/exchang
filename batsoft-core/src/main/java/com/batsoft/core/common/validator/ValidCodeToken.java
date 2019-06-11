package com.batsoft.core.common.validator;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.exception.ValidException;
import com.batsoft.utils.CookieUtil;
import lombok.Data;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 图形验证码令牌类
 * @author Bat Admin
 * @version 2017-5-19
 */
@Data
public class ValidCodeToken  implements ValidToken {
	private static final long serialVersionUID = 1L;
	private String captcha;
	private boolean mobileLogin;
	private ServletRequest request;

	private RedisService redisService;

	private NECaptchaVerifier captchaVerifier;
	private String captchaId = "87cc815627084c41bb8cb582e9378997";
	public final String secretId ="5c2f58ed7484b397050951e150e576b1";
	public final String secretKey="9cdb51d25c324e24894027dba6c72590";

	public ValidCodeToken() {
		super();
	}

	public ValidCodeToken( String captcha, boolean mobileLogin,ServletRequest request) {
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
		this.request = request;
		this.captchaVerifier = new NECaptchaVerifier(captchaId,new NESecretPair(secretId,secretKey));
	}


	/**
	 * 校验图形验证码
	 *
	 * @throws ValidException
	 */
	@Override
	public void isValid() throws ValidException {
		String	req_captcha = request.getParameter(NECaptchaVerifier.REQ_VALIDATE);
		if(req_captcha != null){
			captcha = req_captcha;
		}
		boolean result = captchaVerifier.verify(captcha,null);
		if(!result){
			throw new ValidException(Language.L("msg_picture_code_error"));
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