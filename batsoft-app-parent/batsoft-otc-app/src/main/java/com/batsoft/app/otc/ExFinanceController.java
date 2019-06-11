/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-12-08 09:56:42 
*/
package com.batsoft.app.otc;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;

import com.batsoft.service.module.otc.service.ExFinanceService;
import com.batsoft.model.module.otc.ExFinance;
import com.batsoft.service.module.otc.service.ExFinanceService;


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
* <p>ExFinanceController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-12-08 09:56:42 
*/
@Controller("exFinanceController")
@RequestMapping("/otc/exFinance")

@Slf4j
public class ExFinanceController extends BaseController<ExFinance,String> {


@Resource(name="exFinanceService")
@Override
public void setService(BaseService<ExFinance, String> service) {
super.service = service;
}
@RequestMapping("/view/add")
@RequiresPermissions("otc:exFinance:add")
public String viewAdd(Model model) {
return  "otc/exFinance/add";

}

@RequestMapping("/view/edit")
@RequiresPermissions("otc:exFinance:edit")

public String viewEdit(Model model) {
return  "otc/exFinance/edit";

}

@RequestMapping("/view/see")
@RequiresPermissions("otc:exFinance:see")

public String viewSee(Model model) {
return  "otc/exFinance/see";

}


@RequestMapping("/view/list")
@RequiresPermissions("otc:exFinance:list")

public String viewList(Model model) {
return  "otc/exFinance/list";

}

/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find")
@RequiresPermissions(value={"otc:exFinance:see","otc:exFinance:edit"},logical=Logical.OR)

@ResponseBody
@Override
public JsonResult find( @RequestParam String id) {
return super.find(id);
}

/**
* 分页list
* @param
* @return
*/
@RequestMapping(value = "/list")
@RequiresPermissions("otc:exFinance:list")

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
@RequiresPermissions(value={"otc:exFinance:add","otc:exFinance:edit"},logical=Logical.OR)

@ResponseBody
@Override
public JsonResult save(@ModelAttribute ExFinance exFinance) {
return super.save(exFinance);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("otc:exFinance:remove")

@ResponseBody
@Override
public JsonResult remove(@RequestParam(value = "ids[]") String[] ids) {
return super.remove(ids);
}


}
