package com.batsoft.model.module.system.manage.vo;

/**
 * Created by Administrator on 2017/6/14.
 */
public class RolePremData {
    String roleId;
    String perms;
    String[] permsArr;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPerms() {

        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
        setPermsArr(perms);
    }

    public String[] getPermsArr() {
        return permsArr;
    }

    public void setPermsArr(String perms) {
        this.permsArr = perms.split(",");
    }
}
