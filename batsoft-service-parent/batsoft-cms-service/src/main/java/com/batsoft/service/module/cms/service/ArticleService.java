/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 11:16:51 
*/

package com.batsoft.service.module.cms.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.cms.Article;
import com.batsoft.model.module.cms.vo.ArticleChannelVo;
import com.batsoft.model.module.cms.vo.ArticleVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* <p>ArticleService</p>
* @author: Bat Admin
* @Date :  2018-04-19 11:16:51 
*/
public interface ArticleService  extends BaseService<Article, String>{

    /**
     * 站点首页文章数据 只显示类别为是首页的数据
     */
    List<ArticleChannelVo> findIndexChannels();

    /**
     * 文章首页数据 显示全部类别
     */
    List<ArticleChannelVo> findAllChannels();

    /**
     * 获取分页数据
     * @param request
     * @param typeKey
     * @return
     */
    PageResult findArticles(HttpServletRequest request, String typeKey);

    /**
     * 通过类型key 和数量获取文章数据
     * @param typeKey
     * @param size
     * @return
     */
    List<ArticleVo> findArticles(String typeKey, Integer size);
}
