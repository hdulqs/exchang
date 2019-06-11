/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.cms.article;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.Article;
import com.batsoft.model.module.cms.ArticleChannel;
import com.batsoft.model.module.cms.Single;
import com.batsoft.model.module.cms.vo.ArticleVo;
import com.batsoft.service.module.cms.service.ArticleChannelService;
import com.batsoft.service.module.cms.service.ArticleService;
import com.batsoft.service.module.cms.service.SingleService;
import com.batsoft.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>article</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "文章管理")
@Controller("articleController")
@RequestMapping("/article")
@Slf4j
public class ArticleController extends BaseController<Article,String> {

    @Resource(name="articleService")
    @Override
    public void setService(BaseService<Article, String> service) {
        super.service=service;
    }

    @Autowired
    private ArticleChannelService articleChannelService;


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index() {
        return "cms/article/index";
    }


    /**
     * 获取文章列表
     * @param typeKey
     * @param model
     * @return
     */
    @ApiOperation(value = "查询文章页面")
    @ApiImplicitParam(value = "关键字",name = "typeKey",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/list/{typeKey}",method = RequestMethod.GET)
    public String list(@PathVariable("typeKey") String typeKey, Model model) {

        QueryFilter filter =new QueryFilter(ArticleChannel.class);
        filter.addFilter("typeKey_EQ",typeKey);
        ArticleChannel articleChannel=articleChannelService.get(filter);

        model.addAttribute("typeKey",typeKey);
        model.addAttribute("typeName",articleChannel.getName());
        return "cms/article/list";
    }

    /**
     * 获取文章分类
     * @return
     */
    @ApiOperation(value = "获取文章分类")
    @RequestMapping(value = "/listChannels",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult listChannels(){
        QueryFilter filter= new QueryFilter(ArticleChannel.class);
        filter.addFilter("del_EQ",ArticleChannel.DEL_FLAG_NORMAL);
        filter.addFilter("status_EQ",ArticleChannel.STATUS1);
        List<ArticleChannel> list = articleChannelService.find(filter);
        return  showSuccessJson("成功",list);
    }

    /**
     * 获取文章内容
     * @param id
     * @param model
     * @return
     */
    @ApiOperation(value = "获取文章内容")
    @ApiImplicitParam(value = "文章id",name = "id",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public String content(@PathVariable("id") String id,Model model) {
        QueryFilter channelFilter=new QueryFilter(ArticleChannel.class);

        Article article =  findById(id);
        channelFilter.addFilter("typeKey_EQ",article.getTypeKey());
        ArticleChannel channel=articleChannelService.get(channelFilter);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        model.addAttribute(articleVo);
        model.addAttribute("typeName",channel.getName());
        model.addAttribute("typeKey",article.getTypeKey());
        return "cms/article/content";
    }

    /**
     * 查询分页数据
     * @param typeKey
     * @param request
     * @return
     */
    @ApiOperation(value = "获取指定文章数量")
    @ApiImplicitParam(value = "文章类型",name = "typeKey",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/page/{typeKey}",method = RequestMethod.GET)
    @ResponseBody
    public PageResult page(@PathVariable("typeKey") String typeKey,HttpServletRequest request){
        PageResult pageResult = ((ArticleService)service).findArticles(request,typeKey);
        return pageResult;
    }

    /**
     * 查询文章指定类型和条数
     * @param typeKey 类型key
     * @param size 数量
     * @return
     */
    @ApiOperation(value = "获取指定文章")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文章类型", name = "typeKey", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(value = "条数", name = "size", paramType = "path", dataType = "Integer", required = true)
    })
    @RequestMapping(value = "/data/{typeKey}/{size}",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult data(@PathVariable("typeKey") String typeKey,@PathVariable("size") Integer size){
        JsonResult jsonResult = new JsonResult();
        try {
           List<ArticleVo> list = ((ArticleService) service).findArticles(typeKey, size);
           jsonResult.setSuccess(true);
           jsonResult.setCode(Constants.SUCCESS);
           jsonResult.setData(list);

       }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(e.getMessage());
       }
        return jsonResult;
    }

    /**
     * 全站首页文章展示
     * @return
     */
    @ApiOperation(value = "全站首页文章展示")
    @RequestMapping(value = "/index/articles",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult indexArticles(){
        JsonResult jsonResult = new JsonResult();
        try {
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setData(((ArticleService)super.service).findIndexChannels());
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 文章频道全部类型数据
     * @return
     */
    @ApiOperation(value = "文章频道全部类型数据")
    @RequestMapping(value = "/channelArticles",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult mainData(){
        JsonResult jsonResult = new JsonResult();
        try {
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setData(((ArticleService)super.service).findAllChannels());
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }


}
