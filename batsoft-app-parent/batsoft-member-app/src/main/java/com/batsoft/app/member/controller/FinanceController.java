package com.batsoft.app.member.controller;

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
import com.batsoft.model.module.member.Finance;

/**
 * 
 * <p>
 * FinanceController
 * </p>
 * 
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-17 18:52:28
 */
@RestController("financeController")
@RequestMapping("/member/finance")
public class FinanceController extends BaseController<Finance, String> {

	@Resource(name = "financeService")
	@Override
	public void setService(BaseService<Finance, String> service) {
		super.service = service;
	}

	/**
	 * 查找
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value = { "member:finance:see", "member:finance:edit" }, logical = Logical.OR)
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
	@RequiresPermissions("member:finance:list")
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
	@RequiresPermissions(value = { "member:finance:add", "member:finance:edit" }, logical = Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody Finance finance) {
		return super.save(finance);
	}

	/**
	 * 删除操作
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("member:finance:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}

}
