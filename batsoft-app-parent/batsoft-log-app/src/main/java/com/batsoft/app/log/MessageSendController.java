/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-30 12:13:29 
*/
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
import com.batsoft.model.module.log.MessageSend;

/**
 * 
 * <p>
 * MessageSendController
 * </p>
 * 
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-30 12:13:29
 */
@RestController("messageSendController")
@RequestMapping("/log/messageSend")
public class MessageSendController extends BaseController<MessageSend, String> {

	@Resource(name = "messageSendService")
	@Override
	public void setService(BaseService<MessageSend, String> service) {
		super.service = service;
	}

	/**
	 * 查找
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value = { "log:messageSend:see", "log:messageSend:edit" }, logical = Logical.OR)
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
	@RequiresPermissions("log:messageSend:list")

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
	@RequiresPermissions(value = { "log:messageSend:add", "log:messageSend:edit" }, logical = Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody MessageSend messageSend) {
		return super.save(messageSend);
	}

	/**
	 * 删除操作
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("log:messageSend:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}

}
