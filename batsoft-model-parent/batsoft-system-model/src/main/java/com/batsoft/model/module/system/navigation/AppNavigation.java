/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-08-08 18:29:21
 */
package com.batsoft.model.module.system.navigation;

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
 * 前端导航管理
 * <p>AppNavigation</p>
 * @author: Bat Admin
 * @Date :  2017-08-08 18:29:21
 */
@Table(name = "system_app_navigation")
@Entity
@Data
@ToString
public class AppNavigation extends BaseModel {

    /**
     * 导航是否显示
     *0:不显示
     */
    public static final Integer DISPLAY0 = 0;
    /**
     * 导航是否显示
     *1:显示
     */
    public static final Integer DISPLAY1 = 1;

    /**
     * 导航类型
     *0:自定义
     */
    public static final Integer TYPE0 = 0;
    /**
     * 导航类型
     *1:用户中心
     */
    public static final Integer TYPE1 = 1;
    /**
     * 导航类型
     *2:底部导航
     */
    public static final Integer TYPE2 = 2;
    /**
     * 是否以新窗口打开
     *0:否
     */
    public static final Integer EXTERNAL0 = 0;
    /**
     * 是否以新窗口打开
     *1:是
     */
    public static final Integer EXTERNAL1 = 1;


    /**
     * id
     */

    @Id
    @Column(name = "id")
    private String id;
    /**
     * 导航类型
     */

    @NotNull(message = "导航类型不能为空")
    @Column(name = "type")
    private Integer type;
    /**
     * 导航标题
     */

    @Length(max = 100, message = "导航标题长度必须介于1和100之间")
    @Words(field = "导航标题", message = "导航标题包含敏感词")
    @Column(name = "name")
    private String name;
    /**
     * 导航链接
     */

    @Length(max = 255, message = "导航链接长度必须介于1和255之间")
    @Words(field = "导航链接", message = "导航链接包含敏感词")
    @Column(name = "url")
    private String url;
    /**
     * icon
     */

    @Length(max = 50, message = "icon长度必须介于1和50之间")
    @Words(field = "icon", message = "icon包含敏感词")
    @Column(name = "icon")
    private String icon;
    /**
     * 是否以新窗口打开
     */

    @NotNull(message = "是否以新窗口打开不能为空")
    @Column(name = "external")
    private Integer external;
    /**
     * 排序
     */

    @NotNull(message = "排序不能为空")
    @Column(name = "sort")
    private Integer sort;
    /**
     * 父级ID
     */

    @NotNull(message = "父级ID不能为空")
    @Length(max = 255, message = "父级ID长度必须介于1和255之间")
    @Words(field = "父级ID", message = "父级ID包含敏感词")
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 菜单等级
     */

    @NotNull(message = "菜单等级不能为空")
    @Column(name = "level")
    private Integer level;
    /**
     * 菜单ID路径
     */

    @NotNull(message = "菜单ID路径不能为空")
    @Length(max = 255, message = "菜单ID路径长度必须介于1和255之间")
    @Words(field = "菜单ID路径", message = "菜单ID路径包含敏感词")
    @Column(name = "path")
    private String path;
    /**
     * 描述
     */

    @Length(max = 255, message = "描述长度必须介于1和255之间")
    @Words(field = "描述", message = "描述包含敏感词")
    @Column(name = "description")
    private String description;
    /**
     * 是否显示：0,否;1,是
     */

    @Column(name = "display")
    private Integer display;

}
