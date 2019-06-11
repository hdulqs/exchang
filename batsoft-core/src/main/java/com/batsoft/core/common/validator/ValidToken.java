package com.batsoft.core.common.validator;


import com.batsoft.core.exception.ValidException;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface ValidToken extends AuthenticationToken {

    /**
     * 校验是否成功
     * @return
     */
    void isValid() throws ValidException;

}
