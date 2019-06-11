/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 11:16:51
 */
package com.batsoft.app.cms;


import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.Article;
import com.batsoft.service.module.cms.service.consumer.ArticleConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>ArticleController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 11:16:51
 */
@RestController("articleController")
@RequestMapping("/cms/article")

@Slf4j
public class ArticleController extends BaseController<Article, String> {

    @Autowired
    ArticleConsumerService articleConsumerService;



    @Resource(name = "articleService")
    @Override
    public void setService(BaseService<Article, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"cms:article:see", "cms:article:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult find(@PathVariable String id) {

        System.out.println("======consumer2");
        JSONObject data=new JSONObject();
        data.put("key","test");

         articleConsumerService.post(data);


        return super.find(id);
    }




    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("cms:article:list")

    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"cms:article:add", "cms:article:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody Article article) {
        return super.save(article);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("cms:article:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

}
