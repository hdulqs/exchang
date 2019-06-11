/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.member.message;

import com.batsoft.core.web.controller.GenericController;
import com.batsoft.service.module.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>MessageController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Controller("messageController")
@RequestMapping("/member/message")
@Slf4j
class MessageController extends GenericController {

    @Autowired
    private UserService userService;

    /**
     * 我的系统消息
     *
     * @param model
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        return "member/message/index";
    }

    /**
     * 消息提醒设置
     *
     * @param model
     * @return
     */
    @RequestMapping("/setting")
    public String setting(Model model) {
        return "member/message/setting";
    }

}
