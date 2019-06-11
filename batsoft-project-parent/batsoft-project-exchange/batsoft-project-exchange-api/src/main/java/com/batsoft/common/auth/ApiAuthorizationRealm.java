package com.batsoft.common.auth;


import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 校验器
 * 
 * @author simon
 */
public class ApiAuthorizationRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        return super.isPermittedAll(permissions, info);
    }
    
}
