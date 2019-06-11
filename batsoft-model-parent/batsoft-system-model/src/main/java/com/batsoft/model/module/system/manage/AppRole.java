/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:19:47
 */
package com.batsoft.model.module.system.manage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;

import lombok.ToString;

/**
 * 应用管理角色
 * 
 */
@Entity
@ToString
@Table(name = "system_app_role")
public class AppRole extends BaseModel {

	private static final long serialVersionUID = 1888568646336223857L;

	/**
     * 角色ID
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    @Length(max = 50, message = "角色名称长度必须介于1和50之间")
    @Words(field = "角色名称", message = "角色名称包含敏感词")
    @Column(name = "name")
    private String name;
    
    /**
     * 角色唯一标识
     */
    @NotNull(message = "角色唯一标识不能为空")
    @Length(max = 50, message = "角色唯一标识长度必须介于1和50之间")
    @Words(field = "角色唯一标识", message = "角色唯一标识包含敏感词")
    @Column(name = "role_key")
    private String roleKey;
    
    /**
     * 是否禁用
     */
    @Column(name = "available")
    private Integer available;
    
    /**
     * 创建人
     */
    @NotNull(message = "创建人不能为空")
    @Length(max = 100, message = "创建人长度必须介于1和100之间")
    @Words(field = "创建人", message = "创建人包含敏感词")
    @Column(name = "create_user")
    private String createUser;
    
    /**
     * 修改人
     */
    @NotNull(message = "修改人不能为空")
    @Length(max = 100, message = "修改人长度必须介于1和100之间")
    @Words(field = "修改人", message = "修改人包含敏感词")
    @Column(name = "update_user")
    private String updateUser;
    
    /**
     * 创建人id
     */
    @NotNull(message = "创建人id不能为空")
    @Length(max = 255, message = "创建人id长度必须介于1和255之间")
    @Words(field = "创建人id", message = "创建人id包含敏感词")
    @Column(name = "create_user_id")
    private String createUserId;
    
    /**
     * 修改人id
     */
    @NotNull(message = "修改人id不能为空")
    @Length(max = 255, message = "修改人id长度必须介于1和255之间")
    @Words(field = "修改人id", message = "修改人id包含敏感词")
    @Column(name = "update_user_id")
    private String updateUserId;
    
    /**
     * 备注
     */
    @Length(max = 255, message = "备注长度必须介于1和255之间")
    @Words(field = "备注", message = "备注包含敏感词")
    @Column(name = "remark")
    private String remark;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	public Integer getAvailable() {
		return available;
	}
	public void setAvailable(Integer available) {
		this.available = available;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
