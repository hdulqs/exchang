/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:02
 */
package com.batsoft.app.exchange;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.CustomerAccountFreeze;
import com.batsoft.model.module.exchange.CustomerAccountRecord;
import com.batsoft.service.module.exchange.service.CustomerAccountFreezeService;
import com.batsoft.service.module.exchange.service.CustomerAccountRecordService;
import com.batsoft.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>CustomerAccountFreezeController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:21:02
 */
@RestController("customerAccountFreezeController")
@RequestMapping("/exchange/customerAccountFreeze")

@Slf4j
public class CustomerAccountFreezeController extends BaseController<CustomerAccountFreeze, String> {


    @Resource(name = "customerAccountFreezeService")
    @Override
    public void setService(BaseService<CustomerAccountFreeze, String> service) {
        super.service = service;
    }

    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:customerAccountFreeze:see", "exchange:customerAccountFreeze:edit"}, logical = Logical.OR)
    @ResponseBody
    public JsonResult find(@PathVariable String id,@RequestParam(name = "date",required = false) String dateStr) {
        JsonResult jsonResult = new JsonResult(true);
        Date date ;
        if(dateStr != null){
            date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
        }else{
            date =  new Date();
        }
        CustomerAccountFreeze entrustInfo =  ((CustomerAccountFreezeService)service).findById(id,date) ;
        jsonResult.setData(entrustInfo);
        return jsonResult;
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("exchange:customerAccountFreeze:list")
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
        if(dateStr!=null){
            date =   DateUtils.parse(dateStr,DateUtils.DEFAULT_FORMAT_YYYY_MM_DD);
        }else{
            date =  new Date();
        }
        String account_id = request.getParameter("accountNumber");
        String coin_code = request.getParameter("coinCode");
        return ((CustomerAccountFreezeService)service).findPageBySql(account_id,coin_code,page,pageSize,date);
    }

    /**
     * 保存或修改
     * @param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiresPermissions(value = {"exchange:customerAccountFreeze:add", "exchange:customerAccountFreeze:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody CustomerAccountFreeze customerAccountFreeze) {
        JsonResult jsonResult = new JsonResult(false);
        int result =  ((CustomerAccountFreezeService)service).saveObject(customerAccountFreeze,new Date());
        if(result == 1) {
            jsonResult.setSuccess(true);
        }
        return jsonResult;
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:customerAccountFreeze:remove")
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
              int res =  ((CustomerAccountFreezeService)service).deleteById( pksData.getIdsArr(),date);
                if(res == pksData.getIdsArr().length){
                    result.setSuccess(true);
                    result.setMsg("删除成功");
                    result.setCode(Constants.SUCCESS);
                    result.setData(data);
                }else {
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
