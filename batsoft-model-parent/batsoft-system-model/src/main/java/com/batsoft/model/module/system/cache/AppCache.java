/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-06-30 11:28:27 
*/
package com.batsoft.model.module.system.cache;

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
* <p>AppCache</p>
* @author: Bat Admin
* @Date :  2017-06-30 11:28:27 
*/
@Table(name="system_app_cache")
@Entity
@Data
@ToString
public class AppCache extends BaseModel {

    /**
    * 缓存类型
    *0:永久缓存
    */
    public static final Integer  CACHETYPE0 = 0;
    /**
    * 缓存类型
    *1:临时缓存
    */
    public static final Integer  CACHETYPE1 = 1;
    /**
    * 缓存值类型
    *0:字符串
    */
    public static final Integer  CACHEVALUETYPE0 = 0;
    /**
    * 缓存值类型
    *1:对象
    */
    public static final Integer  CACHEVALUETYPE1 = 1;

    /**
    * id
    */
    @Id
    @Column(name="id")
    private String id;
    
    /**
    * 缓存key
    */
    @NotNull(message="缓存key不能为空")
    @Length(max = 100, message = "缓存key长度必须介于1和100之间")
    @Words(field = "缓存key", message = "缓存key包含敏感词")
    @Column(name="cache_key")
    private String cacheKey;
    
    /**
    * 缓存值
    */
    @Words(field = "缓存值", message = "缓存值包含敏感词")
    @Column(name="cache_value")
    private String cacheValue;
    
    /**
    * 缓存类型
    */
    @NotNull(message="缓存类型不能为空")
    @Column(name="cache_type")
    private Integer cacheType;
    
    /**
    * 缓存时间(s)0为永久
    */
    @Column(name="cache_time")
    private Integer cacheTime;
    
    /**
    * 缓存值类型
    */
    @Column(name="cache_value_type")
    private Integer cacheValueType;

}
