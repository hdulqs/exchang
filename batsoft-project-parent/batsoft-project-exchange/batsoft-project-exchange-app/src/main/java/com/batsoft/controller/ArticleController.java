/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.controller;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.Article;
import com.batsoft.model.module.cms.ArticleChannel;
import com.batsoft.model.module.cms.vo.ArticleChannelVo;
import com.batsoft.model.module.cms.vo.ArticleVo;
import com.batsoft.service.module.cms.service.ArticleChannelService;
import com.batsoft.service.module.cms.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/api/article")
@Slf4j
@ResponseBody
public class ArticleController extends BaseController<Article,String> {

    @Resource(name="articleService")
    @Override
    public void setService(BaseService<Article, String> service) {
        super.service=service;
    }

    @Autowired
    private ArticleChannelService articleChannelService;





    /**
     * 获取文章列表
     * @param typeKey
     * @param model
     * @return
     */
    /*@ApiOperation(value = "查询文章页面")
    @ApiImplicitParam(value = "关键字",name = "typeKey",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/list/{typeKey}",method = RequestMethod.GET)
    public String list(@PathVariable("typeKey") String typeKey, Model model) {

        QueryFilter filter =new QueryFilter(ArticleChannel.class);
        filter.addFilter("typeKey_EQ",typeKey);
        ArticleChannel articleChannel=articleChannelService.get(filter);

        model.addAttribute("typeKey",typeKey);
        model.addAttribute("typeName",articleChannel.getName());
        return "cms/article/list";
    }*/

    /**
     * 获取文章分类
     * @return
     */
    @ApiOperation(value = "获取文章分类")
    @RequestMapping(value = "/listChannels",method = RequestMethod.GET)
    public JsonResult listChannels(){
        QueryFilter filter= new QueryFilter(ArticleChannel.class);
        filter.addFilter("del_EQ", ArticleChannel.DEL_FLAG_NORMAL);
        filter.addFilter("status_EQ", ArticleChannel.STATUS1);
        List<ArticleChannel> list = articleChannelService.find(filter);
        return  showSuccessJson("成功",list);
    }

    /**
     * 获取文章内容
     * @param id
     * @return
     */
    @ApiOperation(value = "根据主键id获取文章内容")
    @ApiImplicitParam(value = "文章id",name = "id",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult<ArticleVo> content(@PathVariable("id") String id) {
        QueryFilter channelFilter=new QueryFilter(ArticleChannel.class);
        Article article =  findById(id);
        channelFilter.addFilter("typeKey_EQ",article.getTypeKey());
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        JsonResult<ArticleVo> resultData=new JsonResult<>();
        resultData.setSuccess(Boolean.TRUE);
        resultData.setData(articleVo);
        return resultData;
    }

    /**
     * 查询分页数据
     * @param typeKey
     * @param request
     * @return
     */
    @ApiOperation(value = "根据传的参数类型获取公告分页列表")
    @ApiImplicitParam(value = "文章类型",name = "typeKey",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/page/{typeKey}",method = RequestMethod.GET)
    public JsonResult<PageResult> page(@PathVariable("typeKey") String typeKey, HttpServletRequest request){
        PageResult pageResult = ((ArticleService)service).findArticles(request,typeKey);
        JsonResult<PageResult> jsonResult = new JsonResult();
        jsonResult.setCode(Constants.SUCCESS);
        jsonResult.setSuccess(Boolean.TRUE);
        jsonResult.setData(pageResult);
        return jsonResult;
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
    public JsonResult data(@PathVariable("typeKey") String typeKey,@PathVariable("size") Integer size){
        JsonResult resultData=new JsonResult<>();
        List<ArticleVo> list = ((ArticleService) service).findArticles(typeKey, size);
        resultData.setSuccess(Boolean.TRUE);
        resultData.setData(list);
        resultData.setCode(Constants.SUCCESS);
        return resultData;
    }



    /**
     * 全站首页文章展示
     * @return
     */
    @ApiOperation(value = "全站首页文章展示")
    @RequestMapping(value = "/index/articles",method = RequestMethod.GET)
    public JsonResult<List<ArticleChannelVo>> indexArticles(){
        JsonResult<List<ArticleChannelVo>> resultData=new JsonResult<>();
        resultData.setSuccess(Boolean.TRUE);
        resultData.setCode(Constants.SUCCESS);
        resultData.setData(((ArticleService)super.service).findIndexChannels());
        return resultData;
    }




}
