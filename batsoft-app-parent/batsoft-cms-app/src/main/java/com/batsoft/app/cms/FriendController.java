/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:59 
*/
package com.batsoft.app.cms;


import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.core.model.PksData;
import com.batsoft.service.module.cms.service.FriendService;
import com.batsoft.model.module.cms.Friend;
import com.batsoft.service.module.cms.service.FriendService;


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
* <p>FriendController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:59 
*/
@RestController("friendController")
@RequestMapping("/cms/friend")

@Slf4j
public class FriendController extends BaseController<Friend,String> {


@Resource(name="friendService")
@Override
public void setService(BaseService<Friend, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"cms:friend:see","cms:friend:edit"},logical=Logical.OR)
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
@RequiresPermissions("cms:friend:list")

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
@RequiresPermissions(value={"cms:friend:add","cms:friend:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody Friend friend) {
return super.save(friend);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("cms:friend:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

}
