/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.otc;

import com.batsoft.core.web.controller.GenericController;
import com.batsoft.service.module.otc.service.ExCoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>OTC项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "币的管理")
@Controller("otcCoinController")
@RequestMapping("/otc")
@Slf4j
public class OtcCoinController extends GenericController{
    @Autowired
    private ExCoinService exCoinService ;

    /**
     * 获取coins
     * @param
     * @return
     */
    @ApiOperation(value = "获取coins的列表")
    @RequestMapping(value = "/coins", method = RequestMethod.GET)
    @ResponseBody
    public String coins() {
        return exCoinService.findJsonCoins();
    }

}
