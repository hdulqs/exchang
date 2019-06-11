/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 17:29:20
 */
package com.batsoft.app.system.config;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.model.module.system.config.vo.AppConfigVo;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.service.module.system.service.config.AppConfigTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>AppConfigController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-26 17:29:20
 */
@RestController("appConfigController")
@RequestMapping("/system/config/appConfig")
@Slf4j
public class AppConfigController extends BaseController<AppConfig,String> {

  
    @Autowired
    private AppConfigTypeService appConfigTypeService;

    @Resource(name="appConfigService")
    @Override
    public void setService(BaseService<AppConfig, String> service) {
        super.service = service;
    }

    /**
     * 查找
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:config:appConfig:see", "system:config:appConfig:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        try {
            AppConfig appConfig=super.service.get(id);
            AppConfigVo appConfigVo=new AppConfigVo();
            BeanUtils.copyProperties(appConfig,appConfigVo);

            appConfigVo.setTypes(appConfigTypeService.findTypes());
            baseResult.setData(appConfigVo);
            baseResult.setSuccess(true);
            baseResult.setMsg("加载成功");
            baseResult.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("AppConfigController findAppConfig error：" + e.getMessage());
            baseResult.setSuccess(false);
            baseResult.setMsg("加载失败,error:" + e.getMessage());
            baseResult.setCode(Constants.FAILED);
        }
        return this.baseResult;
    }

    /**
     * 通过typeKey 查询
     * @param
     * @return
     */
    @RequestMapping(value = "/listByTypeKey/{typeKey}")
    @RequiresPermissions("system:config:appConfig:setting")
    @ResponseBody
    public JsonResult listByTypeKey(@PathVariable String typeKey) {
        JsonResult result=new JsonResult();
        try {
            result.setSuccess(true);
            result.setData(((AppConfigService)super.service).findByTypeKey(typeKey));
            result.setMsg("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("获取失败"+e.getMessage());
            log.info("AppConfigController listPage error：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:config:appConfig:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {

        return pageList(request);
    }


    /**
     * 编辑或修改
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"system:config:appConfig:add", "system:config:appConfig:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppConfig appConfig) {
       return super.save(appConfig);
    }


    /**
     * 配置参数
     * @param data 参数字符串
     * @return
     */
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST, consumes = "application/json")
    @RequiresPermissions("system:config:appConfig:setting")
    @ResponseBody
    public JsonResult updateConfig(@RequestBody JSONObject data) {
        JsonResult result = new JsonResult();
        try {

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                ((AppConfigService)super.service).updateConfig(entry.getKey(),entry.getValue().toString());
            }

            result.setSuccess(true);
            result.setMsg("配置成功！");
            result.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("导航添加/修改失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("操作失败！");
            result.setCode(Constants.FAILED);
        }
        return result;
    }


    /**
     * 删除操作
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("system:config:appConfig:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }


}
