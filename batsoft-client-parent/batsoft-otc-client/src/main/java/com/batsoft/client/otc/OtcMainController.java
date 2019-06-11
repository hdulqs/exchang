/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.otc;

import com.batsoft.core.web.controller.GenericController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>OTC项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "otcMainController")
@Controller("otcMainController")
@RequestMapping("/otc")
@Slf4j
public class OtcMainController extends GenericController {
    /**
     * otc 首頁
     *
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index () {
        return "otc/index";
    }
}
