package com.batsoft.app.log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.log.MemberLogin;

/**
 * 
 * <p>
 * MemberLoginController
 * </p>
 * 
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-04-24 14:27:23
 */
@RestController("memberLoginController")
@RequestMapping("/log/memberLogin")
public class MemberLoginController extends BaseController<MemberLogin, String> {

	@Resource(name = "memberLoginService")
	@Override
	public void setService(BaseService<MemberLogin, String> service) {
		super.service = service;
	}

	/**
	 * 查找
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value = { "log:memberLogin:see", "log:memberLogin:edit" }, logical = Logical.OR)
	@ResponseBody
	@Override
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
	@RequiresPermissions("log:memberLogin:list")

	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		return super.pageList(request);
	}

	/**
	 * 保存或修改
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@RequiresPermissions(value = { "log:memberLogin:add", "log:memberLogin:edit" }, logical = Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody MemberLogin memberLogin) {
		return super.save(memberLogin);
	}

	/**
	 * 删除操作
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("log:memberLogin:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}

}
