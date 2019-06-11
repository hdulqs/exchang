/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/
package com.batsoft.app.exchange;


import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.WithdrawAddress;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.exchange.service.WithdrawAddressService;
import com.batsoft.service.module.member.service.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 
* <p>WithdrawAddressController</p>
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-25 15:16:42 
*/
@RestController("withdrawAddressController")
@RequestMapping("/exchange/withdrawAddress")

@Slf4j
public class WithdrawAddressController extends BaseController<WithdrawAddress,String> {

    @Autowired
    private WithdrawAddressService withdrawAddressService;


@Resource(name="withdrawAddressService")
@Override
public void setService(BaseService<WithdrawAddress, String> service) {
super.service = service;
}
/**
* 查找
* @param
* @return
*/
@RequestMapping(value = "/find/{id}")
@RequiresPermissions(value={"exchange:withdrawAddress:see","exchange:withdrawAddress:edit"},logical=Logical.OR)
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
@RequiresPermissions("exchange:withdrawAddress:list")

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
@RequiresPermissions(value={"exchange:withdrawAddress:add","exchange:withdrawAddress:edit"},logical=Logical.OR)
@ResponseBody
@Override
public JsonResult save(@RequestBody WithdrawAddress withdrawAddress) {
    User user = UserUtils.getUser();
    withdrawAddress.setUserId(user.getId());
    withdrawAddress.setUserEmail(user.getUserEmail());
    withdrawAddress.setUserMobile(user.getUserMobile());
    withdrawAddress.setUserName(user.getUserName());
//    withdrawAddress.setDefaultAddress(Boolean.FALSE);
return super.save(withdrawAddress);
}

/**
* 删除操作
* @param
* @return
*/
@RequestMapping(value = "/remove", method = RequestMethod.POST)
@RequiresPermissions("exchange:withdrawAddress:remove")
@ResponseBody
public JsonResult remove(@RequestBody PksData pksData) {
return super.remove(pksData.getIdsArr());
}

    /**
     * 获取当前登录用户的所有提现地址
     * @param request
     * @return
     */
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @RequiresPermissions("exchange:withdrawAddress:listAll")
    @ResponseBody
    public List<WithdrawAddress> listAll(HttpServletRequest request) {
        User user = UserUtils.getUser();
        QueryFilter queryFilter = new QueryFilter(WithdrawAddress.class);
        queryFilter.addFilter("user_id",user.getId());
        return  withdrawAddressService.find(queryFilter);
    }

}
