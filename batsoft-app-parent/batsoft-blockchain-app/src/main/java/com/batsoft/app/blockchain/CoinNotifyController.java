/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:29 
*/
package com.batsoft.app.blockchain;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.blockchain.service.CoinNotifyService;
import com.batsoft.model.module.blockchain.CoinNotify;
import com.batsoft.service.module.blockchain.service.CoinNotifyService;


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
* <p>CoinNotifyController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:29 
*/
@RestController("coinNotifyController")
@RequestMapping("/blockchain/coinNotify")

@Slf4j
public class CoinNotifyController extends BaseController<CoinNotify,String> {


@Resource(name="coinNotifyService")
@Override
public void setService(BaseService<CoinNotify, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"blockchain:coinNotify:see","blockchain:coinNotify:edit"},logical=Logical.OR)
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
@RequiresPermissions("blockchain:coinNotify:list")

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
@RequiresPermissions(value={"blockchain:coinNotify:add","blockchain:coinNotify:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody CoinNotify coinNotify) {
return super.save(coinNotify);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("blockchain:coinNotify:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

}
