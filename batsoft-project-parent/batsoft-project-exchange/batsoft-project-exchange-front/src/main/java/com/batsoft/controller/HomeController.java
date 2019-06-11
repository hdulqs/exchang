package com.batsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.common.base.BaseController;
import com.batsoft.common.beans.bo.HomeBP;
import com.batsoft.common.beans.vo.HomeVO;
import com.batsoft.common.util.data.ResultData;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 演示
 * 
 * @author simon
 */
@Controller
public class HomeController extends BaseController {
	
	/**
	 * 测试案例
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/homeTest" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String home(@RequestBody HomeBP param, ResultData<HomeVO> result1) {
		System.out.println("mobile=" + param.getMobile());
		System.out.println("password=" + param.getPassword());
		
		// 响应数据
		HomeVO vo = result1.getData(HomeVO.class);
		vo.setMobile(param.getMobile());
		vo.setPassword(param.getPassword());
		result1.setSuccessful();
		return GsonSingleton.getInstance().toJson(result1);
	}
	
}
