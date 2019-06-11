/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:23:08
 */
package com.batsoft.app.exchange;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.Cancle;
import com.batsoft.model.module.exchange.EntrustIng;
import com.batsoft.model.module.exchange.vo.CancleVo;
import com.batsoft.service.module.exchange.service.EntrustInfoService;
import com.batsoft.service.module.exchange.service.EntrustIngService;
import com.batsoft.service.module.exchange.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>EntrustIngController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:23:08
 */
@RestController("entrustIngController")
@RequestMapping("/exchange/entrustIng")

@Slf4j
public class EntrustIngController extends BaseController<EntrustIng, String> {

	@Autowired
	TradeService tradeService;

	@Resource(name = "entrustIngService")
	@Override
	public void setService(BaseService<EntrustIng, String> service) {
		super.service = service;
	}

	/**
	 * 查找
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/{id}")
	@RequiresPermissions(value = {"exchange:entrustIng:see", "exchange:entrustIng:edit"}, logical = Logical.OR)
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
	@RequiresPermissions("exchange:entrustIng:list")

	@ResponseBody
	public PageResult list(HttpServletRequest request) {
		return ((EntrustIngService) service).findPageBySql(request);
	}

	/**
	 * 保存或修改
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@RequiresPermissions(value = {"exchange:entrustIng:add", "exchange:entrustIng:edit"}, logical = Logical.OR)
	@ResponseBody
	@Override
	public JsonResult save(@RequestBody EntrustIng entrustIng) {
		return super.save(entrustIng);
	}

	/**
	 * 删除操作
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@RequiresPermissions("exchange:entrustIng:remove")
	@ResponseBody
	public JsonResult remove(@RequestBody PksData pksData) {
		return super.remove(pksData.getIdsArr());
	}


	/**
	 * 撤销操作
	 *
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult cancel(@RequestBody CancleVo cancleVo) {
		List<Cancle> cancles = cancleVo.getList();
		JsonResult jsonResult = new JsonResult();
		try {
			if (!org.apache.shiro.util.CollectionUtils.isEmpty(cancles)) {
				for (Cancle cancle : cancles) {
					tradeService.cancel(cancle.getUserid(), cancle.getCoinpair(), cancle.getOrderid());
				}
				jsonResult.setSuccess(true);
			}
		} catch (Exception e) {
			jsonResult.setSuccess(false);
			e.printStackTrace();
		}
		return jsonResult;
	}

}
