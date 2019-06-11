package com.batsoft.core.session;


import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * session 持久化
 * <p>
 * TODO
 * </p>
 * 
 * @author: Bat Admin
 * @Date : 2016年4月11日 下午6:58:07
 */
@Slf4j
public class MySessionDAO extends CachingSessionDAO {

	@Autowired
	private RedisService redisService;

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		try {
			redisService.setObject(ApplicationConfigure.sessionIdCookie + sessionId.toString(), session,  ApplicationConfigure.sessionTimeOut);
		} catch (Exception e) {
			log.info("-------------------------------redis 异常  创建session--------------------------------------------");
		}
		return session.getId();
	}

	@Override
	protected void doUpdate(Session session) {
		if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
			return; // 如果会话过期/停止 没必要再更新了
		}

		if (StringUtils.isEmpty(session.getId())) {
			return;
		}

		try {
			redisService.setObject(ApplicationConfigure.sessionIdCookie + session.getId().toString(), session, ApplicationConfigure.sessionTimeOut);
		} catch (Exception e) {
			log.info("-------------------------------redis 异常  更新session--------------------------------------------");
		}

	}

	@Override
	protected void doDelete(Session session) {
		try {
			redisService.delRedisByKey(ApplicationConfigure.sessionIdCookie + session.getId().toString());
		} catch (Exception e) {
			log.info("-------------------------------redis 异常  删除session--------------------------------------------");
		}
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = null;
		log.info("==========讀取=====");
        try {
        	session = (Session) redisService.getObject(ApplicationConfigure.sessionIdCookie+sessionId.toString());
        	
    	    if(StringUtils.isEmpty(session)){
    	    	return null;
            }else{
            	redisService.setObject(ApplicationConfigure.sessionIdCookie+sessionId.toString(), session, ApplicationConfigure.sessionTimeOut);
            }
        	
		} catch (Exception e) {
			log.info("-------------------------------redis 异常 读session--------------------------------------------");
		}
        return session;
    
	}


}
