package com.batsoft.core.security;

/**
 * Created by Administrator on 2017/9/18.
 */

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.utils.IdGen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**

 * A manager for the CSRF token for a given session. The {@link #getTokenForSession(HttpSession)} should used to

 * obtain the token value for the current session (and this should be the only way to obtain the token value).

 * ***/



public final class CsrfTokenManager {



    /**

     * The token parameter name

     */

    static final String CSRF_PARAM_NAME = "_csrf";

    /**
     * The token time out default 300s
     */
    static final Integer CSRF_TIME_OUT = 300;

    /**

     * The location on the session which stores the token

     */

    public static final  String CSRF_TOKEN_FOR_SESSION_ATTR_NAME =  "CsrfTokenManager.tokenval";



    public static String getTokenForSession(HttpSession session) {

        String token = null;

         RedisService redisService=(RedisService) SpringContextUtil.getBean("redisService");

        // I cannot allow more than one token on a session - in the case of two

        // requests trying to

        // init the token concurrently

        synchronized (session) {

            /*token =  redisService.get(Constants.CACHE_SECURITY_KEY+ ApplicationConfigure.sessionIdCookie+":"+CSRF_TOKEN_FOR_SESSION_ATTR_NAME+"-"+session.getId().toString());

            if (null == token) {

                token = IdGen.uuid().toString();

                redisService.set(Constants.CACHE_SECURITY_KEY+ ApplicationConfigure.sessionIdCookie+":"+CSRF_TOKEN_FOR_SESSION_ATTR_NAME+"-"+session.getId().toString(), token,CSRF_TIME_OUT);

            }*/

            token = IdGen.uuid().toString();

            redisService.set(Constants.CACHE_SECURITY_KEY+ ApplicationConfigure.sessionIdCookie+":"+CSRF_TOKEN_FOR_SESSION_ATTR_NAME+"-"+session.getId().toString(), token,CSRF_TIME_OUT);


        }

        return token;

    }


    /**
     * clear token
     * @param session
     */
    public static void  clearTokenForSession(HttpSession session) {

        RedisService redisService=(RedisService) SpringContextUtil.getBean("redisService");


        synchronized (session) {

            redisService.delRedisByKey(Constants.CACHE_SECURITY_KEY+ ApplicationConfigure.sessionIdCookie+":"+CSRF_TOKEN_FOR_SESSION_ATTR_NAME+"-"+session.getId().toString());


        }


    }

    /**

     * Extracts the token value from the session

     *

     * @param request

     * @return

     */

    public static String getTokenFromRequest(HttpServletRequest request) {

        return request.getParameter(CSRF_PARAM_NAME);

    }



    private CsrfTokenManager() {

    };



}
