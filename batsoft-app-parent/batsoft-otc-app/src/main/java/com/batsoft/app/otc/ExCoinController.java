/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-18 13:20:52 
*/
package com.batsoft.app.otc;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.otc.ExCoin;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
/**
* 
* <p>ExCoinController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-18 13:20:52 
*/
@Controller("exCoinController")
@RequestMapping("/otc/exCoin")

@Slf4j
public class ExCoinController extends BaseController<ExCoin,String> {


@Resource(name="exCoinService")
@Override
public void setService(BaseService<ExCoin, String> service) {
super.service = service;
}
@RequestMapping("/view/add")
@RequiresPermissions("otc:exCoin:add")
public String viewAdd(Model model) {
return  "otc/exCoin/add";

}

@RequestMapping("/view/edit")
@RequiresPermissions("otc:exCoin:edit")

public String viewEdit(Model model) {
return  "otc/exCoin/edit";

}

@RequestMapping("/view/see")
@RequiresPermissions("otc:exCoin:see")

public String viewSee(Model model) {
return  "otc/exCoin/see";

}


@RequestMapping("/view/list")
@RequiresPermissions("otc:exCoin:list")

public String viewList(Model model) {
return  "otc/exCoin/list";

}

/**
* 查找
* @param
* @return
*/
@Override
@RequestMapping(value = "/find")
@RequiresPermissions(value={"otc:exCoin:see","otc:exCoin:edit"},logical=Logical.OR)

@ResponseBody
public JsonResult find( @RequestParam String id) {
return super.find(id);
}

/**
* 分页list
* @param
* @return
*/
@RequestMapping(value = "/list")
@RequiresPermissions("otc:exCoin:list")

@ResponseBody
public PageResult list(HttpServletRequest request) {
return super.pageList(request);
}


/**
* 保存或修改
* @param
* @return
*/
@Override
@RequestMapping(value = "/save", method = RequestMethod.POST)
@RequiresPermissions(value={"otc:exCoin:add","otc:exCoin:edit"},logical=Logical.OR)

@ResponseBody
public JsonResult save(@ModelAttribute ExCoin exCoin) {
return super.save(exCoin);
}

/**
* 删除操作
* @param
* @return
*/
@Override
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("otc:exCoin:remove")

@ResponseBody
public JsonResult remove(@RequestParam(value = "ids[]") String[] ids) {
return super.remove(ids);
}


}
