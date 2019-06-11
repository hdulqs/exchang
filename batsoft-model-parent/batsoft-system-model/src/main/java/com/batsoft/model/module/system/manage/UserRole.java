/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:19:47
 */
package com.batsoft.model.module.system.manage;

import com.batsoft.model.BaseModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>UserRole</p>
 * @author: Bat Admin
 * @Date :  2017-05-12 20:19:47
 */
@Table(name = "system_app_user_role")
@Entity
@Data
@ToString
public class UserRole extends BaseModel {

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

}
