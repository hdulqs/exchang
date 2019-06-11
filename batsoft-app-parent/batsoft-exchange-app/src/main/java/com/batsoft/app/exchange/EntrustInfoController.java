/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */
package com.batsoft.app.exchange;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.system.manage.AppUser;
import com.batsoft.service.module.exchange.service.CustomerAccountFreezeService;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.system.auth.UserUtils;
import com.batsoft.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>EntrustInfoController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */
@RestController("entrustInfoController")
@RequestMapping("/exchange/entrustInfo")

@Slf4j
public class EntrustInfoController extends BaseController<EntrustInfo, String> {


    @Resource(name = "entrustInfoService")
    @Override
    public void setService(BaseService<EntrustInfo, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:entrustInfo:see", "exchange:entrustInfo:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id,@RequestParam(name = "date",required = false) String dateStr) {
        JsonResult jsonResult = new JsonResult(true);
        Date date ;
        if(dateStr != null){
            date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
        }else{
            date =  new Date();
        }
        EntrustInfo entrustInfo =  ((EntrustInfoService)service).findById(id,date) ;
        jsonResult.setData(entrustInfo);
        return jsonResult;
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("exchange:entrustInfo:list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        int page = 1;
        int pageSize = 10;
        String pageStr = request.getParameter("page");
        if(pageStr != null){
            page = Integer.valueOf(pageStr);
        }
        String pageSizeStr = request.getParameter("pageSize");
        if(pageSizeStr != null){
            pageSize = Integer.valueOf(pageSize);
        }
        String dateStr = request.getParameter("date");
        Date date ;
        if(dateStr != null){
            date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
        }else{
            date =  new Date();
        }
        String order_id = request.getParameter("entrustOrder");//订单id
        String coin_code = request.getParameter("coinCode");//coin_code
        return ((EntrustInfoService)service).findPageBySql( coin_code,order_id, page, pageSize,date);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"exchange:entrustInfo:add", "exchange:entrustInfo:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody EntrustInfo entrustInfo) {
        return super.save(entrustInfo);
    }

    /**
     * 删除操作
     * @param
     * @return
     */

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("exchange:entrustInfo:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData,HttpServletRequest request) {

        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        String dateStr = request.getParameter("date");
        Date date ;
        if(dateStr != null){
            date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
        }else{
            date =  new Date();
        }
        try {
            int res = ((EntrustInfoService)service).deleteById( pksData.getIdsArr(),date);
            if(res == pksData.getIdsArr().length){
                result.setSuccess(true);
                result.setMsg("删除成功");
                result.setCode(Constants.SUCCESS);
                result.setData(data);
            } else {
                result.setSuccess(false);
                result.setMsg("删除失败");
                result.setCode(Constants.FAILED);
            }
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

}
