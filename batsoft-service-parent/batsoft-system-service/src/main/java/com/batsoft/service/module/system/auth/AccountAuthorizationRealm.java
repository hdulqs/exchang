package com.batsoft.service.module.system.auth;


import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.validator.ValidCodeToken;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.service.module.system.service.manage.AppUserService;
import com.batsoft.utils.StringUtils;
import com.batsoft.shiro.UsernamePasswordToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 管理员的认证,角色,权限控制
 */
public class AccountAuthorizationRealm extends AuthorizingRealm {

    @Resource
    AppUserService appUserService;
    @Autowired
    private RedisService redisService;

    /**
     * 查询获得用户信息
     * AuthenticationToken 用于收集用户提交的身份（如用户名）及凭据（如密码）
     * <p>
     * AuthenticationInfo有两个作用：
     * 1、如果Realm 是AuthenticatingRealm 子类，则提供给AuthenticatingRealm 内部使用的
     * CredentialsMatcher进行凭据验证；（如果没有继承它需要在自己的Realm中自己实现验证）；
     * 2、提供给SecurityManager来创建Subject（提供身份信息）；
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        if (username == null) {
            throw new AccountException(
                    "Null usernames are not allowed by this realm.");
        }
        //是否校验图形验证码  目前没解决rn 图形验证码问题 这里临时设置为false
       boolean ValidCode=false;

         if(ValidCode) {
            // 图形验证码校验
            ValidCodeToken validCodeToken = new ValidCodeToken(token.getCaptcha(), token.isMobileLogin(), token.getRequest());
            validCodeToken.isValid();
        }

        AppUser appUser = appUserService.findAppUser(username);

        if (appUser == null) {
            throw new UnknownAccountException("用户不存在");//用户不存在
        } else {
            return new SimpleAuthenticationInfo(new Principal(appUser,token.isMobileLogin()),
                    appUser.getPassword().toCharArray(), ByteSource.Util.bytes(appUser.getSalt()), getName());
        }

    }



    /**
     * 表示根据用户身份获取授权信息
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /**
         * 目前系统默认全部是batsoft_manage 角色，通过数据库角色对应权限来区分，以后可以考虑不通的角色也读取数据库设置
         */
        info.addRole("batsoft_manage");

        List<AppMenu> menuList = UserUtils.getMenuAndFunList();
        StringBuffer premissionCache=new StringBuffer();
        for (AppMenu appMenu : menuList) {
            if(appMenu!=null) {
                //循环菜单列表，将premission加入到权限信息中
                if (StringUtils.isNotEmpty(appMenu.getPermission())) {
                    for (String premission : appMenu.getPermission().split(",")) {
                        if (!"".equals(premission)) {
                            info.addStringPermission(premission);
                        }
                    }
                }
                if (!"".equals(appMenu.getPermission())) {
                    premissionCache.append(appMenu.getPermission());
                    premissionCache.append(",");
                }
            }
        }


        //放入缓存
        if(premissionCache.length()>0) {
            String premissionStr=premissionCache.deleteCharAt(premissionCache.length()-1).toString();
            redisService.setObject( UserUtils.CACHE_PERMISSION + "_" + UserUtils.USER_CACHE_ID_ + UserUtils.getUser().getId(),premissionStr,0);
        }

        return info;
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     * @param permission
     */
    private void authorizationValidate(Permission permission){
        // 模块授权预留接口
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id; // 编号
        private String userName; // 登录名
        private String realName; // 姓名
        private boolean mobileLogin; // 是否手机登录

//		private Map<String, Object> cacheMap;

        public Principal(AppUser user, boolean mobileLogin) {
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

}
