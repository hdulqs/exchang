package com.batsoft.client.system.auth;


import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.batsoft.core.common.validator.ValidCodeToken;
import com.batsoft.model.module.member.Principal;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.shiro.UsernamePasswordToken;

/**
 * 管理员的认证,角色,权限控制
 */
public class AccountAuthorizationRealm extends AuthorizingRealm {

    @Resource
    UserService userService;
    @Autowired
    SessionDAO sessionDAO;

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
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for(Session session:sessions){
            if(username.equals(String.valueOf(session.getAttribute("userName")))){
                session.setTimeout(0);//设置session立即失效，即将其踢出系统
                break;
            }
        }
        // 图形验证码校验
        ValidCodeToken validCodeToken = new ValidCodeToken(token.getCaptcha(),token.isMobileLogin(),token.getRequest());
        validCodeToken.isValid();

        User user = userService.findUser(username);

        if (user == null) {
            throw new UnknownAccountException("用户不存在");//用户不存在
        }else if(user.getStatus().equals(User.STATUS1)){
            throw new UnknownAccountException("用户被禁用，请联系管理员！");//用户被禁用，请联系管理员！
        } else  {
            return new SimpleAuthenticationInfo(new Principal(user,token.isMobileLogin()),
                    user.getPassword().toCharArray(), ByteSource.Util.bytes(user.getSalt()), getName());
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
         * 目前系统默认全部是batsoft_front 角色，通过数据库角色对应权限来区分，以后可以考虑不通的角色也读取数据库设置
         */
        info.addRole("batsoft_front");

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



}
