/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:21:20 
*/
package com.batsoft.app.blockchain;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.blockchain.service.CoinApiLogService;
import com.batsoft.model.module.blockchain.CoinApiLog;
import com.batsoft.service.module.blockchain.service.CoinApiLogService;


import com.batsoft.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.batsoft.core.service.BaseService;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
/**
* 
* <p>CoinApiLogController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:21:20 
*/
@RestController("coinApiLogController")
@RequestMapping("/blockchain/coinApiLog")

@Slf4j
public class CoinApiLogController extends BaseController<CoinApiLog,String> {


@Resource(name="coinApiLogService")
@Override
public void setService(BaseService<CoinApiLog, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"blockchain:coinApiLog:see","blockchain:coinApiLog:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult find(@PathVariable  String id) {
return super.find(id);
}

/**
* 分页list
* @param
* @return
*/
@RequestMapping(value = "/list")
@RequiresPermissions("blockchain:coinApiLog:list")

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
@RequiresPermissions(value={"blockchain:coinApiLog:add","blockchain:coinApiLog:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody CoinApiLog coinApiLog) {
return super.save(coinApiLog);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("blockchain:coinApiLog:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

}
