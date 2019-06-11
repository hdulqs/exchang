/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 15:11:40
 */
package com.batsoft.app.system.config;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.system.config.AppConfigType;
import com.batsoft.model.module.system.config.vo.AppConfigTypeVo;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.service.module.system.service.config.AppConfigTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>AppConfigTypeController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 15:11:40
 */
@RestController("appConfigTypeController")
@RequestMapping("/system/config/appConfigType")
@Slf4j
public class  AppConfigTypeController extends BaseController<AppConfigType,String> {

    @Autowired
    private AppConfigService appConfigService;

    @Resource(name="appConfigTypeService")
    @Override
    public void setService(BaseService<AppConfigType, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:config:appConfigType:see", "system:config:appConfigType:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:config:appConfigType:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        return pageList(request);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/findTypes")
    @RequiresPermissions(value = {"system:config:appConfig:add", "system:config:appConfig:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult findTypes() {
        JsonResult jsonResult=new JsonResult();
        try {
            List<AppConfigTypeVo> configTypes=((AppConfigTypeService)super.service).findTypes();
            for(AppConfigTypeVo appConfigTypeVo:configTypes){
                appConfigTypeVo.setChildren(appConfigService.findByTypeKey(appConfigTypeVo.getTypeKey()));
            }
            jsonResult.setSuccess(true);
            jsonResult.setData(configTypes);
            jsonResult.setMsg("获取成功");
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("获取失败");
            log.info("AppConfigTypeController findTypes error：" + e.getMessage());
        }
        return jsonResult;
    }




    /**
     * 编辑或修改
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:config:appConfigType:add", "system:config:appConfigType:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppConfigType appConfigType) {
        return super.save(appConfigType);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("system:config:appConfigType:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }


}
