/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-29 19:32:39
 */
package com.batsoft.app.system.cache;

import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.system.cache.AppCache;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>AppCacheController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-06-29 19:32:39
 */
@RestController("appCacheController")
@RequestMapping("/system/cache/appCache")
@Slf4j
public class AppCacheController extends BaseController<AppCache, String> {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AppConfigService appConfigService;

    @Resource(name="appCacheService")
    @Override
    public void setService(BaseService<AppCache, String> service) {
        super.service = service;
    }

    /**
     * 更新系統參數配置文件缓存
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/updateAppConfigCache")
    @RequiresPermissions("system:cache:appCache:updateAppConfigCache")
    @ResponseBody
    public JsonResult updateAppConfigCache() {
        JsonResult result = new JsonResult();
        try {
            appConfigService.findConfigToCache();
            result.setSuccess(true);
            result.setMsg("系统配置参数缓存更新成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("系统配置参数缓存更新失败" + e.getMessage());
        }
        return result;
    }

    /**
     * 查找
     *
     * @param
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"system:cache:appCache:see", "system:cache:appCache:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("system:cache:appCache:list")
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
    @RequiresPermissions(value = {"system:cache:appCache:add", "system:cache:appCache:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult save(@RequestBody AppCache appCache) {
        JsonResult result = new JsonResult();
        try {
            //敏感词过滤 格式校验
            if (!beanValidatorForJson(appCache)) {
                return this.baseResult;
            }
            if (StringUtils.isEmpty(appCache.getId())) {

                // 通过aop 记录到数据库 aop 参考 com.batsoft.admin.cache
                redisService.set(appCache.getCacheKey(), appCache.getCacheValue(), appCache.getCacheTime());
                result.setSuccess(true);
                result.setMsg("保存成功！");
                result.setCode(Constants.SUCCESS);
            } else {
//修改
                if ( AppCache.CACHEVALUETYPE0.equals(appCache.getCacheValueType())) {
                    redisService.set(appCache.getCacheKey(), appCache.getCacheValue(), appCache.getCacheTime());
                    result.setSuccess(true);
                    result.setMsg("修改成功！");
                    result.setCode(Constants.SUCCESS);
                } else {
                    result.setSuccess(false);
                    result.setMsg("缓存值为对象不允许修改！");
                    result.setCode(Constants.FAILED);
                }

            }
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
    @RequiresPermissions("system:cache:appCache:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        JsonResult result = new JsonResult();

        List
                <String> data = new ArrayList
                <String>();
        try {
            for (String id : pksData.getIdsArr()) {
                AppCache appCache = super.service.get(id);
                redisService.delRedisByKey(appCache.getCacheKey());
                super.service.delete(id);
                data.add(id);
            }
            result.setSuccess(true);
            result.setMsg("删除成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }


}
