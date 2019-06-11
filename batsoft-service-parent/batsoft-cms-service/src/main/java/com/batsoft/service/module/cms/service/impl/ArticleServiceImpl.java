/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 11:16:51 
*/
package com.batsoft.service.module.cms.service.impl;

import com.batsoft.core.common.PageFactory;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.cms.Article;
import com.batsoft.model.module.cms.ArticleChannel;
import com.batsoft.model.module.cms.vo.ArticleChannelVo;
import com.batsoft.model.module.cms.vo.ArticleVo;
import com.batsoft.service.module.cms.dao.ArticleDao;
import com.batsoft.service.module.cms.service.ArticleChannelService;
import com.batsoft.service.module.cms.service.ArticleService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
* <p> ArticleServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-19 11:16:51 
*/
@Service("articleService")
public class ArticleServiceImpl extends BaseServiceImpl<Article, String> implements ArticleService{

@Autowired
private ArticleDao articleDao;
    @Autowired
    private ArticleChannelService articleChannelService;

    /**
     * 站点首页文章数据 只显示类别为是首页的数据
     */
    @Override
    public List<ArticleChannelVo> findIndexChannels() {
        ArrayList<ArticleChannelVo> channelVoArrayList=new ArrayList<>();
        ArticleChannelVo articleChannelVo=null;
        try {
            QueryFilter filter =new QueryFilter(ArticleChannel.class);
            filter.addFilter("del_EQ",ArticleChannel.DEL_FLAG_NORMAL);
            filter.addFilter("parentId_EQ","0");
            filter.addFilter("indexShow_EQ",ArticleChannel.INDEXSHOW1);
            filter.addFilter("status_EQ",ArticleChannel.STATUS1);
            filter.orderBy("sort desc");
            List<ArticleChannel> list=articleChannelService.find(filter);
            for (ArticleChannel channel:list) {
                articleChannelVo=new ArticleChannelVo();
                articleChannelVo.setName(channel.getName());
                articleChannelVo.setTypeKey(channel.getTypeKey());
                articleChannelVo.setIcon(channel.getIcon());
                List<ArticleVo> articles = this.findArticles(channel.getTypeKey(),ArticleVo.INDEX_PAGE_SIZE);
                articleChannelVo.setArticleVos(articles);
                channelVoArrayList.add(articleChannelVo);
            }
        }catch (Exception e){

        }
        return  channelVoArrayList;
    }

    /**
     * 文章首页数据 显示全部类别
     */
    @Override
    public List<ArticleChannelVo> findAllChannels() {
        ArrayList<ArticleChannelVo> channelVoArrayList=new ArrayList<>();
        ArticleChannelVo articleChannelVo=null;
        try {
            QueryFilter filter =new QueryFilter(ArticleChannel.class);
            filter.addFilter("del_EQ",ArticleChannel.DEL_FLAG_NORMAL);
            filter.addFilter("parentId_EQ","0");
            filter.addFilter("status_EQ",ArticleChannel.STATUS1);
            filter.orderBy("sort desc");
            List<ArticleChannel> list=articleChannelService.find(filter);
            for (ArticleChannel channel:list) {
                articleChannelVo=new ArticleChannelVo();
                articleChannelVo.setName(channel.getName());
                articleChannelVo.setTypeKey(channel.getTypeKey());
                articleChannelVo.setIcon(channel.getIcon());
                List<ArticleVo> articles = this.findArticles(channel.getTypeKey(),ArticleVo.INDEX_PAGE_SIZE);
                articleChannelVo.setArticleVos(articles);
                channelVoArrayList.add(articleChannelVo);
            }
        }catch (Exception e){

        }
        return  channelVoArrayList;
    }

    /**
     * 文章分页查询
     * @param request
     * @return
     */
    @Override
    public PageResult findArticles(HttpServletRequest request, String typeKey) {
        //封装必要参数
        QueryFilter filter = new QueryFilter(Article.class, request);
        //分页插件
        Page<Article> page = PageFactory.getPage(filter);
        //查询方法
        articleDao.findArticles(typeKey,ArticleVo.ALLPAGESIZE);

        return new PageResult(page, filter);
    }

    /**
     * 通过类型key 和数量获取文章数据
     *
     * @param  typeKey
     * @param size
     * @return
     */
    @Override
    public List<ArticleVo> findArticles(String typeKey, Integer size) {
        return articleDao.findArticles(typeKey, size);
    }
}
