/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:59 
*/
package com.batsoft.app.blockchain;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.blockchain.service.CoinApiUserService;
import com.batsoft.model.module.blockchain.CoinApiUser;
import com.batsoft.service.module.blockchain.service.CoinApiUserService;


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
* <p>CoinApiUserController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-22 14:20:59 
*/
@RestController("coinApiUserController")
@RequestMapping("/blockchain/coinApiUser")

@Slf4j
public class CoinApiUserController extends BaseController<CoinApiUser,String> {


@Resource(name="coinApiUserService")
@Override
public void setService(BaseService<CoinApiUser, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"blockchain:coinApiUser:see","blockchain:coinApiUser:edit"},logical=Logical.OR)
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
@RequiresPermissions("blockchain:coinApiUser:list")

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
@RequiresPermissions(value={"blockchain:coinApiUser:add","blockchain:coinApiUser:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody CoinApiUser coinApiUser) {
return super.save(coinApiUser);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("blockchain:coinApiUser:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

}
