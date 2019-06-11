/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:59 
*/

package com.batsoft.model.module.cms;
import com.batsoft.model.BaseModel;
import com.batsoft.utils.annotation.Words;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import com.batsoft.utils.annotation.Money;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;

/**
* 
* <p>Friend</p>
* @author: Bat Admin
* @Date :  2018-04-14 10:24:59 
*/
@Table(name="cms_friend")
@Entity
@Data
@ToString
public class Friend extends BaseModel {

            /**
            * 链接类型
            *0:logo类型
            */
                public static final Integer  TYPE0=0;
            /**
            * 链接类型
            *1:文字类型
            */
                public static final Integer  TYPE1=1;


    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 名称
    */

            @NotNull(message="名称不能为空")
            @Length(max = 100, message = "名称长度必须介于1和100之间")
            @Words(field = "名称", message = "名称包含敏感词")
    @Column(name="name")
    private String name;
    /**
    * 链接类型
    */

            @NotNull(message="链接类型不能为空")
    @Column(name="type")
    private Integer type;
    /**
    * 图片上传
    */

            @Length(max = 255, message = "图片上传长度必须介于1和255之间")
            @Words(field = "图片上传", message = "图片上传包含敏感词")
    @Column(name="image_url")
    private String imageUrl;
    /**
    * 排序
    */

    @Column(name="sort")
    private Integer sort;
    /**
    * 链接地址
    */

            @Length(max = 255, message = "链接地址长度必须介于1和255之间")
            @Words(field = "链接地址", message = "链接地址包含敏感词")
    @Column(name="link_url")
    private String linkUrl;

}
