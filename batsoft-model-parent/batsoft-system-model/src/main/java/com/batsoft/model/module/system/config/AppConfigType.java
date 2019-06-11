/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-26 15:11:40 
*/
package com.batsoft.model.module.system.config;

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
* <p>AppConfigType</p>
* @author: Bat Admin
* @Date :  2017-06-26 15:11:40 
*/
@Table(name="system_app_config_type")
@Entity
@Data
@ToString
public class AppConfigType extends BaseModel {

    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 类型名称
    */

            @NotNull(message="类型名称不能为空")
            @Length(max = 50, message = "类型名称长度必须介于1和50之间")
            @Words(field = "类型名称", message = "类型名称包含敏感词")
    @Column(name="type_name")
    private String typeName;
    /**
    * 类型key
    */

            @NotNull(message="类型key不能为空")
            @Length(max = 50, message = "类型key长度必须介于1和50之间")
            @Words(field = "类型key", message = "类型key包含敏感词")
    @Column(name="type_key")
    private String typeKey;
    /**
    * 备注
    */

            @Length(max = 255, message = "备注长度必须介于1和255之间")
            @Words(field = "备注", message = "备注包含敏感词")
    @Column(name="remark")
    private String remark;
    /**
     * 排序
     */
    private Integer sort;
}
