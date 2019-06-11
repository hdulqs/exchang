package com.batsoft.app.exchange;

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
import com.batsoft.model.module.exchange.C2cOrder;
import com.batsoft.service.module.exchange.service.C2cOrderService;

/**
 *
 * <p>
 * C2cOrderController
 * </p>
 * 
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-07 09:16:15
 */
@RestController("c2cOrderController")
@RequestMapping("/exchange/c2cOrder")
public class C2cOrderController extends BaseController<C2cOrder, String> {

	@Resource(name = "c2cOrderService")
	@Override
	public void setService(BaseService<C2cOrder, String> service) {
		super.service = service;
	}

	/**
	 * 查找
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value = { "exchange:c2cOrder:see", "exchange:c2cOrder:edit" }, logical = Logical.OR)
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
	@RequiresPermissions("exchange:c2cOrder:list")

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
	@RequiresPermissions(value = { "exchange:c2cOrder:add", "exchange:c2cOrder:edit" }, logical = Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody C2cOrder c2cOrder) {
		return super.save(c2cOrder);
	}

	/**
	 * 删除操作
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("exchange:c2cOrder:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}

	/**
	 * 订单审核通过
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/passStatus", method = RequestMethod.POST)
	@RequiresPermissions("exchange:c2cOrder:passStatus")
	@ResponseBody
	public JsonResult passStatus(@RequestBody PksData pksData) {
		return ((C2cOrderService) super.service).c2cPass(pksData.getIds());
	}

	/**
	 * 订单审核失败
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/refuseStatus", method = RequestMethod.POST)
	@RequiresPermissions("exchange:c2cOrder:refuseStatus")
	@ResponseBody
	public JsonResult refuseStatus(@RequestBody PksData pksData) {
		return ((C2cOrderService) super.service).c2cRefuse(pksData.getIds());
	}

}
