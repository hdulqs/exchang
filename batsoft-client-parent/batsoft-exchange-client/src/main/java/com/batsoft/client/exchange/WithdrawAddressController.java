/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/
package com.batsoft.client.exchange;


import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.model.module.exchange.WithdrawAddress;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.exchange.service.WithdrawAddressService;
import com.batsoft.service.module.member.service.UserUtils;

/**
* 
* <p>WithdrawAddressController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/
@Api(description = "体现地址Controller")
@RestController("withdrawAddressController")
@RequestMapping("/exchange/withdrawAddress")
public class WithdrawAddressController  {

    @Autowired
    private WithdrawAddressService withdrawAddressService;
    
	/**
	* 保存或修改
	* @param
	* @return
	*/
	@ApiOperation(value = "保存和修改体现地址")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "withdrawAddress",value = "体现地址对象",paramType = "body",required = true,dataType = "WithdrawAddress" )
	})
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult save(@RequestBody WithdrawAddress withdrawAddress) {
	    if(!withdrawAddress.getCoinAddress().substring(0,2).equals("0x")||withdrawAddress.getCoinAddress().length()!=42){
	        new JsonResult(false).setMsg("地址规范错误");
	    }
	    User user = UserUtils.getUser();
	    withdrawAddress.setUserId(user.getId());
	    withdrawAddress.setUserEmail(user.getUserEmail());
	    withdrawAddress.setUserMobile(user.getUserMobile());
	    withdrawAddress.setUserName(user.getUserName());
	    withdrawAddressService.save(withdrawAddress);
	   return new JsonResult(true);
	}
	
	/**
	* 删除操作
	* @param
	* @return
	*/
	@ApiOperation(value = "删除地址",notes = "根据地址id删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "地址id",paramType = "query",required = true,dataType = "String" )
	})
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult remove(String  id) {
	    withdrawAddressService.delete(id);
	return new JsonResult() ;
	}

    /**
     * 获取当前登录用户的所有提现地址
     * @return
     */
	@ApiOperation(value = "获取用户体现地址",notes = "根据当前用户查询用户的体现地址")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id",value = "地址id",paramType = "query",required = true,dataType = "String" )
	})
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult listAll() {
        JsonResult result = new JsonResult(true);
        User user = UserUtils.getUser();
        QueryFilter queryFilter = new QueryFilter(WithdrawAddress.class);
        queryFilter.addFilter("user_id=",user.getId());
        result.setData(withdrawAddressService.find(queryFilter));
        return  result;
    }


   /* @RequestMapping(value = "/modifyWithdrawAddress", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult modifyWithdrawAddress(@RequestParam String withdrawAddressId){
        withdrawAddressService.modifyWithdrawAddress(withdrawAddressId);
        return new JsonResult(true);
    }*/

}
