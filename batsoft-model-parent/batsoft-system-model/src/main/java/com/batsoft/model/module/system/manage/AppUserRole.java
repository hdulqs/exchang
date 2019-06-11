/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-13 16:56:34
 */
package com.batsoft.model.module.system.manage;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * <p>AppUserRole</p>
 * @author: Bat Admin
 * @Date :  2017-05-13 16:56:34
 */
@Table(name = "system_app_user_role")
@Entity
@Data
@ToString
public class AppUserRole extends BaseModel {
    /**
     * 用户id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 用户id
     */

    @NotNull(message = "用户id不能为空")
    @Length(max = 32, message = "用户id长度必须介于1和32之间")
    @Words(field = "用户id", message = "用户id包含敏感词")
    @Column(name = "user_id")
    private String userId;
    /**
     * 角色id
     */

    @NotNull(message = "角色id不能为空")
    @Length(max = 32, message = "角色id长度必须介于1和32之间")
    @Words(field = "角色id", message = "角色id包含敏感词")
    @Column(name = "role_id")
    private String roleId;

}
