/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 11:16:51 
*/

package com.batsoft.service.module.cms.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.cms.Article;
import com.batsoft.model.module.cms.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 
* <p>ArticleDao</p>
* @author: Bat Admin
* @Date :  2018-04-19 11:16:51 
*/
public interface ArticleDao extends  BaseDao<Article, String> {
    /**
     * 通过类型key查询指定数量文章 size 为0 查全部
     * @param typeKey
     * @param size
     * @return
     */
    List<ArticleVo> findArticles(@Param("typeKey") String typeKey, @Param("size") Integer size);
}
