package com.batsoft.model.module.system.menu;

import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "system_app_menu")
@ToString
public class AppMenu extends BaseModel {

    public static final  Integer DISPLAY0=0;
    public static final  Integer DISPLAY1=1;

    public  static final Integer MENU0=0;
    public  static final Integer MENU1=1;
    private static final long serialVersionUID = -3909699858491335108L;
    /**
     * 索引id
     */
    @Id
    private String id;

    /**
     * 菜单名
     */
    @Length(max = 50, message = "菜单名称长度必须介于1和50之间")
    @Words(field = "菜单名称", message = "菜单名称包含敏感词")
    private String name;
    /**
     * 菜单key
     */
    @Length(max = 50, message = "菜单key长度必须介于1和100之间")
    @Words(field = "菜单key", message = "菜单key包含敏感词")
    private String menuKey;

    /**
     * 菜单Path
     */
    @Length(max = 255, message = "菜单名称长度必须介于1和255之间")
    @Words(field = "菜单路径", message = "菜单路径包含敏感词")
    private String path;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 等级
     */
    private Integer level;

    /**
     * 菜单路径
     */
    private String levelPath;
    /**
     * 菜单描述
     */
    @Words(field = "菜单描述", message = "菜单描述包含敏感词")
    private String description;
    /**
     * 权限标识
     */
    @Words(field = "权限标识", message = "权限标识包含敏感词")
    private String permission;
    /**
     * 是否显示：0,否;1,是
     */
    private Integer display;
    /**
     * 是否主导航：0,否;1,是
     * 非主导航将在下拉选择中
     */
    private Integer mainNav;
    /**
     * 是否菜单：0,否;1,是
     */
    private Integer menu;
    /**
     * icon
     */
    @Words(field = "图标", message = "图标包含敏感词")
    private String icon;
    /**
     * 是否是外部链接 0否 1是
     */
    private Integer external;


}
