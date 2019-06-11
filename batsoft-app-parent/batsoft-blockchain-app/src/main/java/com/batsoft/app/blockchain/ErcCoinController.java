/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-24 21:08:34 
*/
package com.batsoft.app.blockchain;


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
import com.batsoft.model.module.blockchain.ErcCoin;
/**
* 

* <p>ErcCoinController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-24 21:08:34 
*/
@RestController("ercCoinController")
@RequestMapping("/blockchain/ercCoin")
public class ErcCoinController extends BaseController<ErcCoin,String> {


	@Resource(name="ercCoinService")
	@Override
	public void setService(BaseService<ErcCoin, String> service) {
		super.service = service;
	}
	
	/**
	* 查找
	* @param
	* @return
	*/
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value={"blockchain:ercCoin:see","blockchain:ercCoin:edit"},logical=Logical.OR)
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
	@RequiresPermissions("blockchain:ercCoin:list")
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
	@RequiresPermissions(value={"blockchain:ercCoin:add","blockchain:ercCoin:edit"},logical=Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody ErcCoin ercCoin) {
		return super.save(ercCoin);
	}
	
	/**
	* 删除操作
	* @param
	* @return
	*/
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("blockchain:ercCoin:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}

}
