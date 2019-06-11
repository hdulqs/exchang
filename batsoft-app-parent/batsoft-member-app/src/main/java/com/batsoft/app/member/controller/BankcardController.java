/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:22:51 
*/
package com.batsoft.app.member.controller;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.member.service.BankcardService;
import com.batsoft.model.module.member.Bankcard;
import com.batsoft.service.module.member.service.BankcardService;


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
* <p>BankcardController</p>
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-07 09:22:51 
*/
@RestController("bankcardController")
@RequestMapping("/member/bankcard")

@Slf4j
public class BankcardController extends BaseController<Bankcard,String> {


@Resource(name="bankcardService")
@Override
public void setService(BaseService<Bankcard, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"member:bankcard:see","member:bankcard:edit"},logical=Logical.OR)
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
@RequiresPermissions("member:bankcard:list")

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
@RequiresPermissions(value={"member:bankcard:add","member:bankcard:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody Bankcard bankcard) {
return super.save(bankcard);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("member:bankcard:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

}
