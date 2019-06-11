/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-05-12 20:18:59
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
 * <p>AppUser</p>
 * @author: Bat Admin
 * @Date :  2017-05-12 20:18:59
 */
@Table(name = "system_app_user")
@Entity
@Data
@ToString
public class AppUser extends BaseModel {

    /**
     * 用户id
     */
    @Id
    @Column(name = "id")
    private String id;
    
    /**
     * 工号
     */
    @NotNull(message = "工号不能为空")
    @Length(max = 100, message = "工号长度必须介于1和100之间")
    @Words(field = "工号", message = "工号包含敏感词")
    @Column(name = "office_id")
    private String officeId;
    
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @Length(max = 50, message = "用户名长度必须介于1和50之间")
    @Words(field = "用户名", message = "用户名包含敏感词")
    @Column(name = "user_name")
    private String userName;

    @NotNull(message = "密码不能为空")
    @Length(max = 32, message = "用户名长度必须介于1和32之间")
    @Column(name = "password")
    private String password;


    @Column(name = "salt")
    private String salt;
    
    /**
     * 真实姓名
     */
    @NotNull(message = "真实姓名不能为空")
    @Length(max = 50, message = "真实姓名长度必须介于1和50之间")
    @Words(field = "真实姓名", message = "真实姓名包含敏感词")
    @Column(name = "real_name")
    private String realName;
    
    /**
     * 手机号
     */
    @Length(max = 15, message = "手机号长度必须介于1和15之间")
    @Words(field = "手机号", message = "手机号包含敏感词")
    @Column(name = "mobile")
    private String mobile;
    
    /**
     * 固定电话
     */
    @Length(max = 20, message = "固定电话长度必须介于1和20之间")
    @Words(field = "固定电话", message = "固定电话包含敏感词")
    @Column(name = "phone")
    private String phone;
    
    /**
     * 邮箱
     */
    @Length(max = 20, message = "邮箱长度必须介于1和20之间")
    @Words(field = "邮箱", message = "邮箱包含敏感词")
    @Column(name = "email")
    private String email;


    /**
     * 是否超级管理员0 否  1是
     */
    @Column(name = "super_user")
    private Integer superUser;
    /**
     * 是否禁用
     */
    @Column(name = "available")
    private Integer available;
    /**
     * 备注
     */

    @Length(max = 255, message = "备注长度必须介于1和255之间")
    @Words(field = "备注", message = "备注包含敏感词")
    @Column(name = "remark")
    private String remark;


}
