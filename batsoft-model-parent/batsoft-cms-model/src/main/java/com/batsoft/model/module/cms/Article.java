/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 11:16:51 
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
* <p>Article</p>
* @author: Bat Admin
* @Date :  2018-04-19 11:16:51 
*/
@Table(name="cms_article")
@Entity
@Data
@ToString
public class Article extends BaseModel {

        /**
        * 是否显示
        *0:隐藏
        */
            public static final Integer  DISPLAY0=0;
        /**
        * 是否显示
        *1:显示
        */
            public static final Integer  DISPLAY1=1;


    /**
    * id
    */

        @Id
    @Column(name="id")
    private String id;
    /**
    * 文章类别
    */

            @NotNull(message="文章类别不能为空")
            @Length(max = 64, message = "文章类别长度必须介于1和64之间")
            @Words(field = "文章类别", message = "文章类别包含敏感词")
    @Column(name="type_name")
    private String typeName;
    /**
    * 标题
    */

            @NotNull(message="标题不能为空")
            @Length(max = 50, message = "标题长度必须介于1和50之间")
            @Words(field = "标题", message = "标题包含敏感词")
    @Column(name="title")
    private String title;
    /**
    * 标题图片
    */

            @Length(max = 255, message = "标题图片长度必须介于1和255之间")
            @Words(field = "标题图片", message = "标题图片包含敏感词")
    @Column(name="file_path")
    private String filePath;
    /**
    * 文章类型key
    */

            @NotNull(message="文章类型key不能为空")
            @Length(max = 50, message = "文章类型key长度必须介于1和50之间")
            @Words(field = "文章类型key", message = "文章类型key包含敏感词")
    @Column(name="type_key")
    private String typeKey;
    /**
    * 作者
    */

            @Length(max = 50, message = "作者长度必须介于1和50之间")
            @Words(field = "作者", message = "作者包含敏感词")
    @Column(name="author")
    private String author;
    /**
    * 来源
    */

            @Length(max = 255, message = "来源长度必须介于1和255之间")
            @Words(field = "来源", message = "来源包含敏感词")
    @Column(name="article_from")
    private String articleFrom;
    /**
    * 来源URL
    */

            @Length(max = 255, message = "来源URL长度必须介于1和255之间")
            @Words(field = "来源URL", message = "来源URL包含敏感词")
    @Column(name="from_url")
    private String fromUrl;
    /**
    * 是否显示
    */

    @Column(name="display")
    private Integer display;
    /**
    * 点击数
    */

    @Column(name="hits")
    private Integer hits;
    /**
    * 简介
    */

            @Length(max = 255, message = "简介长度必须介于1和255之间")
            @Words(field = "简介", message = "简介包含敏感词")
    @Column(name="short_content")
    private String shortContent;
    /**
    * seo关键字
    */

            @Length(max = 255, message = "seo关键字长度必须介于1和255之间")
            @Words(field = "seo关键字", message = "seo关键字包含敏感词")
    @Column(name="seo_key")
    private String seoKey;
    /**
    * 内容
    */

            @Words(field = "内容", message = "内容包含敏感词")
    @Column(name="content")
    private String content;

}
