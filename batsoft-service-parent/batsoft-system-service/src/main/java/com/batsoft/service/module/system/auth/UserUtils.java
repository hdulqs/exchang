package com.batsoft.service.module.system.auth;

import com.batsoft.service.module.system.auth.AccountAuthorizationRealm.Principal;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.SpringContextUtil;
import com.batsoft.model.module.system.manage.AppRole;
import com.batsoft.model.module.system.manage.AppRoleMenu;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.model.module.system.manage.AppUserRole;
import com.batsoft.model.module.system.menu.AppMenu;
import com.batsoft.service.module.system.service.manage.AppRoleMenuService;
import com.batsoft.service.module.system.service.manage.AppRoleService;
import com.batsoft.service.module.system.service.manage.AppUserRoleService;
import com.batsoft.service.module.system.service.manage.AppUserService;
import com.batsoft.service.module.system.service.menu.AppMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户工具类
 */
public class UserUtils {

    private static AppUserService userService = SpringContextUtil.getBeanByClass(AppUserService.class);
    private static AppRoleService roleService = SpringContextUtil.getBeanByClass(AppRoleService.class);
    private static AppMenuService menuService = SpringContextUtil.getBeanByClass(AppMenuService.class);
    private static RedisService redisService = SpringContextUtil.getBeanByClass(RedisService.class);

    public static final String USER_CACHE_PREFIX = "manage:";
    public static final String USER_CACHE = USER_CACHE_PREFIX+"userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";

    public static final String CACHE_ROLE_LIST = USER_CACHE_PREFIX+"roleList";
    public static final String CACHE_MENU_LIST = USER_CACHE_PREFIX+"menuList";
    public static final String CACHE_PERMISSION = USER_CACHE_PREFIX+"permission";


    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static AppUser get(String id) {
        AppUser user = (AppUser) getCache(USER_CACHE + USER_CACHE_ID_ + id);
        if (user == null) {
            user = userService.findAppUserById(id);
            if (user == null) {
                return null;
            }
            putCache(USER_CACHE + USER_CACHE_ID_ + user.getId(), user);
            putCache(USER_CACHE + USER_CACHE_LOGIN_NAME_ + user.getUserName(), user);
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     *
     * @param userName
     * @return 取不到返回null
     */
    public static AppUser getByUserName(String userName) {
        AppUser user = (AppUser) getCache(USER_CACHE + USER_CACHE_LOGIN_NAME_ + userName);
        if (user == null) {
            user = userService.findAppUser(userName);
            if (user == null) {
                return null;
            }
            putCache(USER_CACHE + USER_CACHE_ID_ + user.getId(), user);
            putCache(USER_CACHE + USER_CACHE_LOGIN_NAME_ + user.getUserName(), user);
        }
        return user;
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_ROLE_LIST + "_" + USER_CACHE_ID_ + getUser().getId());
        removeCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId());
        removeCache(USER_CACHE + USER_CACHE_ID_ + getUser().getId());
        removeCache(USER_CACHE + USER_CACHE_LOGIN_NAME_ + getUser().getUserName());
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(AppUser user) {
        removeCache(CACHE_ROLE_LIST + "_" + USER_CACHE_ID_ + user.getId());
        removeCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + user.getId());
        removeCache(USER_CACHE + USER_CACHE_ID_ + user.getId());
        removeCache(USER_CACHE + USER_CACHE_LOGIN_NAME_ + user.getUserName());
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new Admin()
     */
    public static AppUser getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            AppUser user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new AppUser();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new AppUser();
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<AppRole> getRoleList() {
        List<AppRole> roleList = (List<AppRole>) getCache(CACHE_ROLE_LIST + "_" + USER_CACHE_ID_ + getUser().getId());
        if (roleList == null) {
            AppUser user = getUser();
            if (user.getSuperUser() != null && user.getSuperUser().equals(1)) {
                roleList = roleService.findAll();
            } else {
                AppUserRoleService appUserRoleService = SpringContextUtil.getBeanByClass(AppUserRoleService.class);
                List<AppUserRole> userRoleList = appUserRoleService.findRoleByUserId(user.getId());
                roleList = new ArrayList<AppRole>();
                for (AppUserRole userRole : userRoleList) {
                    if (userRole == null) {
                        break;
                    }
                    QueryFilter filter = new QueryFilter(AppRole.class);
                    filter.addFilter("id=", userRole.getRoleId());
                    filter.addFilter("del=", 0);
                    AppRole role = roleService.get(filter);
                    roleList.add(role);
                }

            }
            if (roleList.size() > 0) {
                putCache(CACHE_ROLE_LIST + "_" + USER_CACHE_ID_ + getUser().getId(), roleList);
            }
        }
        return roleList;
    }

    /**
     * 查询menu
     * @param onlyMenu 是否只查询menu true 是 false 否--把方法也查询出来
     * @return
     */
    private static List<AppMenu> getUserMenuList(boolean onlyMenu) {

        List<AppMenu> menuList = new ArrayList<AppMenu>();
        List<AppRole> roles = getRoleList();
        List<AppRoleMenu> roleMenus = null;
        AppRoleMenuService roleMenuService = SpringContextUtil.getBeanByClass(AppRoleMenuService.class);

        AppMenu appMenu = null;
        for (AppRole role : roles) {
            if (role == null) {
                break;
            }
            roleMenus = roleMenuService.findRoleMenus(role.getId());
            for (AppRoleMenu roleMenu : roleMenus) {
                QueryFilter filter = new QueryFilter(AppMenu.class);
                if (roleMenu == null) {
                    break;
                }
                filter.addFilter("id=", roleMenu.getMenuId());
                filter.addFilter("del=", 0);

                if(onlyMenu) {
                    filter.addFilter("menu=",  AppMenu.MENU1);
                }
                appMenu = menuService.get(filter);
                menuList.add(appMenu);
            }
        }
        return menuList;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<AppMenu> getMenuList() {
        List<AppMenu> menuList = (List<AppMenu>) getCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId());
        if (menuList == null) {
            AppUser user = getUser();
            if (user.getSuperUser() != null && user.getSuperUser().equals(1)) {
                menuList = menuService.findAll();
            } else {
                menuList = getUserMenuList(true);
            }
            putCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId(), menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户所有授权菜单和方法
     *
     * @return
     */
    public static List<AppMenu> getMenuAndFunList() {
        List<AppMenu> menuList = (List<AppMenu>) getCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId());
        if (menuList == null) {
            AppUser user = getUser();
            if (user.getSuperUser() != null && user.getSuperUser().equals(1)) {
                menuList = menuService.findAll();
            } else {
                menuList = getUserMenuList(false);
            }
            putCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId(), menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户Permission 直接向缓存中取值，该缓存在AccountAuthorizationRealm 中存入
     *
     * @return
     */
    public static String getPermission() {
        String permissions = (String) getCache(CACHE_PERMISSION + "_" + USER_CACHE_ID_ + getUser().getId());
        return permissions;
    }

    /**
     * 获取当前用户授权可显示菜单
     *
     * @return
     */
    public static List<AppMenu> getShowMenuList() {
        List<AppMenu> menuList = (List<AppMenu>) getCache(CACHE_MENU_LIST);
        if (menuList == null) {
            AppUser user = getUser();
            if (user.getSuperUser() != null && user.getSuperUser().equals(1)) {
                QueryFilter filter = new QueryFilter(AppMenu.class);
                filter.addFilter("menu=", AppMenu.MENU1);
                filter.addFilter("del=", AppMenu.DEL_FLAG_NORMAL);
                //menu 是1的时候表示菜单  0的时候是功能
                menuList = menuService.find(filter);
            } else {
                menuList = getUserMenuList(true);
            }
        }
        putCache(CACHE_MENU_LIST + "_" + USER_CACHE_ID_ + getUser().getId(), menuList);
        return menuList;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException e) {

        } catch (InvalidSessionException e) {

        }
        return null;
    }


    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = redisService.getObject(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {

        redisService.setObject(key, value, 0);
    }

    public static void removeCache(String key) {
        redisService.delRedisByKey(key);
    }


}
