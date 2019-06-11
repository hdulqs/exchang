package com.batsoft.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Administrator on 2017/5/21.
 */
public class ValidException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public ValidException() {

        super();

    }

    public ValidException(String message, Throwable cause) {

        super(message, cause);

    }

    public ValidException(String message) {

        super(message);

    }

    public ValidException(Throwable cause) {

        super(cause);

    }
}