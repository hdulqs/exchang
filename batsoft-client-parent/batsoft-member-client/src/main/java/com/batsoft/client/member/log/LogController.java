/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.member.log;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>LogController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Controller("logController")
@RequestMapping("/member/log")
@Slf4j
public class LogController extends GenericController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    @ResponseBody
    public PageResult list(HttpServletRequest request) {
        try {
            QueryFilter filter = new QueryFilter(User.class, request);
            return userService.findPage(filter);
        } catch (Exception e) {
            log.info("ApiController listPage error：" + e.getMessage());
        }
        return null;
    }

}
