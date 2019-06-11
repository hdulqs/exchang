package com.batsoft.core.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * <p> TODO</p>     shiro Session 会话监听
 * @author:         Bat Admin
 * @Date :          2015年9月22日 上午10:46:58 
 */
public class MySessionListener implements SessionListener {

	@Override
	public void onExpiration(Session session) {
		System.out.println("会话过期：" + session.getId());
	}

	@Override
	public void onStart(Session session) {
		System.out.println("会话创建：" + session.getId());
	}

	@Override
	public void onStop(Session session) {
		System.out.println("会话停止：" + session.getId());
		
	}

}
