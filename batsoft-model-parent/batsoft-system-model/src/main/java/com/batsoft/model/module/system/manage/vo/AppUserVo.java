/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:18:59
 */
package com.batsoft.model.module.system.manage.vo;

import com.batsoft.model.module.system.manage.AppRole;
import com.batsoft.model.module.system.manage.AppUser;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>AppUser</p>
 * @author: Bat Admin
 * @Date :  2017-05-12 20:18:59
 */

@Data
@ToString
public class AppUserVo extends AppUser {


    /**
     * 前端提交来的用户角色id数组
     */
    private String[] rolesIds;

    /**
     * 用户所具有角色
     */
    private Set<Map<String, String>> roles;

    /**
     * 所有角色数据
     */
    private List<AppRole> allRoles;

    /**
     * 消息数
     */
    private  Integer notifyCount=10;



    public void setRoles(Set<Map<String, String>> roles) {
        this.roles = roles;
        Object[] result = roles.toArray();
        this.rolesIds=new String[result.length];
        for(int i=0;i<result.length;i++){
            Map item=(HashMap)result[i];
            this.rolesIds[i]=item.get("id").toString();
        }

    }
}
