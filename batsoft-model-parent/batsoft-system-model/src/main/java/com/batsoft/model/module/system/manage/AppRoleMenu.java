/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-13 16:56:07
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
 * <p>AppRoleMenu</p>
 * @author: Bat Admin
 * @Date :  2017-05-13 16:56:07
 */
@Table(name = "system_app_role_menu")
@Entity
@Data
@ToString
public class AppRoleMenu extends BaseModel {
    /**
     * 用户id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 角色id
     */

    @NotNull(message = "角色id不能为空")
    @Length(max = 32, message = "角色id长度必须介于1和32之间")
    @Words(field = "角色id", message = "角色id包含敏感词")
    @Column(name = "role_id")
    private String roleId;
    /**
     * 菜单id
     */

    @NotNull(message = "菜单id不能为空")
    @Length(max = 32, message = "菜单id长度必须介于1和32之间")
    @Words(field = "菜单id", message = "菜单id包含敏感词")
    @Column(name = "menu_id")
    private String menuId;

}
