/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.ico;

import com.batsoft.core.web.controller.GenericController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>ICO项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Controller("icoController")
@RequestMapping("/ico")
@Slf4j
public class IcoController extends GenericController {

    /**
     * ico 首頁
     *
     * @return
     */
    @RequestMapping("")
    public String index () {
        return "ico/index";
    }

    /**
     * ico 列表頁
     *
     * @return
     */
    @RequestMapping("/list")
    public String list () {
        return "ico/list";
    }
    /**
     * ico 详情页
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    public String info (@PathVariable String id) {
        return "ico/info";
    }

    /**
     * ico 投资
     *
     * @return
     */
    @RequestMapping("/invest/{id}")
    public String invest (@PathVariable String id) {
        return "ico/invest";
    }

    /**
     * 我支持的项目
     *
     * @return
     */
    @RequestMapping("/member/support")
    public String support() {
        return "ico/support";
    }

    /**
     * 我发布的项目
     *
     * @return
     */
    @RequestMapping("/member/project")
    public String project() {
        return "ico/project";
    }
}
