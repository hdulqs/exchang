/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-05 23:23:40 
*/

package com.batsoft.model.module.system.type;
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
* 
* <p>AppDictionary</p>
* @author: Bat Admin
* @Date :  2018-05-05 23:23:40 
*/
@Table(name="system_app_dictionary")
@Entity
@Data
@ToString
public class AppDictionary extends BaseModel {



    /**
    * 主键ID
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 父级ID
    */

            @NotNull(message="父级ID不能为空")
            @Length(max = 255, message = "父级ID长度必须介于1和255之间")
            @Words(field = "父级ID", message = "父级ID包含敏感词")
    @Column(name="parent_id")
    private String parentId;
    /**
    * 名称
    */

            @NotNull(message="名称不能为空")
            @Length(max = 50, message = "名称长度必须介于1和50之间")
            @Words(field = "名称", message = "名称包含敏感词")
    @Column(name="name")
    private String name;
    /**
    * 字典Key
    */

            @NotNull(message="字典Key不能为空")
            @Length(max = 100, message = "字典Key长度必须介于1和100之间")
            @Words(field = "字典Key", message = "字典Key包含敏感词")
    @Column(name="dic_key")
    private String dicKey;
    /**
    * icon
    */

            @Length(max = 100, message = "icon长度必须介于1和100之间")
            @Words(field = "icon", message = "icon包含敏感词")
    @Column(name="icon")
    private String icon;
    /**
    * 排序
    */

            @NotNull(message="排序不能为空")
    @Column(name="sort")
    private Integer sort;
    /**
    * 字典等级
    */

            @NotNull(message="字典等级不能为空")
    @Column(name="level")
    private Integer level;
    /**
    * 字典ID路径
    */

            @NotNull(message="字典ID路径不能为空")
            @Length(max = 255, message = "字典ID路径长度必须介于1和255之间")
            @Words(field = "字典ID路径", message = "字典ID路径包含敏感词")
    @Column(name="level_path")
    private String levelPath;
    /**
    * 描述
    */

            @Length(max = 255, message = "描述长度必须介于1和255之间")
            @Words(field = "描述", message = "描述包含敏感词")
    @Column(name="description")
    private String description;
    /**
    * 是否显示：0,否;1,是
    */

    @Column(name="display")
    private Integer display;

}
