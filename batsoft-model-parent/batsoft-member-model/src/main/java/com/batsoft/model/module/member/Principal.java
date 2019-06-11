package com.batsoft.model.module.member;

/**
 * Created by Administrator on 2017/9/17.
 */

import java.io.Serializable;

/**
 * 授权用户信息
 */
public  class Principal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 编号
    private String userName; // 登录名
    private String realName; // 姓名
    private boolean mobileLogin; // 是否手机登录

//		private Map<String, Object> cacheMap;

    public Principal(User user, boolean mobileLogin) {
        this.id = user.getId() + "";
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.mobileLogin = mobileLogin;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }


}
