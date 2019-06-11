/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-08-08 18:29:21
 */
package com.batsoft.client.system;

import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseTreeController;
import com.batsoft.model.module.system.navigation.AppNavigation;
import com.batsoft.service.module.system.service.navigation.AppNavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 前端导航管理
 * <p>AppNavigationController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-08-08 18:29:21
 */
@Controller("appNavigationController")
@RequestMapping("/system/navigation/appNavigation")
@Slf4j
public class AppNavigationController extends BaseTreeController<AppNavigation, String> {


    @Resource(name = "appNavigationService")
    @Override
    public void setService(BaseService<AppNavigation, String> service) {
        super.service = service;
    }



    /**
     * 权限树
     *
     * @return
     */
    @RequestMapping(value = "/nav")
    @ResponseBody
    public String nav() {
        return ((AppNavigationService)super.service).findNav();
    }
}
