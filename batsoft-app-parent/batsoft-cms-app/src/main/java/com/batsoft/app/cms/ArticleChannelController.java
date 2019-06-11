/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 10:21:01
 */
package com.batsoft.app.cms;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.ArticleChannel;
import com.batsoft.service.module.cms.service.ArticleChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * <p>ArticleChannelController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-19 10:21:01
 */
@RestController("articleChannelController")
@RequestMapping("/cms/articleChannel")

@Slf4j
public class ArticleChannelController extends BaseController<ArticleChannel, String> {


    @Resource(name = "articleChannelService")
    @Override
    public void setService(BaseService<ArticleChannel, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"cms:articleChannel:see", "cms:articleChannel:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("cms:articleChannel:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return super.pageList(request);
    }
    @RequestMapping(value = "/findChannels")
    @ResponseBody
    public List<ArticleChannel> findChannels() {
        QueryFilter filter = new QueryFilter(ArticleChannel.class);
        filter.addFilter("del_EQ", ArticleChannel.DEL_FLAG_NORMAL);
        filter.addFilter("status_EQ", ArticleChannel.STATUS1);
        List<ArticleChannel> list = ((ArticleChannelService) super.service).find(filter);
        return list;
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"cms:articleChannel:add", "cms:articleChannel:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody ArticleChannel articleChannel) {
        return super.save(articleChannel);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("cms:articleChannel:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }


}
