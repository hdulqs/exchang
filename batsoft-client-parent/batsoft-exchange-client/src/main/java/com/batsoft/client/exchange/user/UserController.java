/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.service.module.log.service.MemberLoginService;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>UserController</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(value = "UserController",description = "个人中心")
@Controller("ex_userController")
@RequestMapping("/ex/member/user")
@Slf4j
public class UserController extends GenericController {

    @Autowired
    private UserService userService;
    @Autowired
    private MemberLoginService memberLoginService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("totalMoney","1111111");
        return "exchange/member/user/index";
    }


    /**
     * 查找基础数据  非安全数据
     *
     * @param
     * @return
     */
    @ApiOperation(value = "查找个人资料",notes = "根据用户id查找")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "用户id",paramType = "query",required = true,dataType = "String" )})
    @RequestMapping(value = "/findBaseInfo/{id}",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findBaseInfo(@PathVariable String id) {
        JsonResult jsonResult=new JsonResult();
        try {
            jsonResult.setData(userService.findUserById(id));
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
            jsonResult.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("MarketController findUser error：" + e.getMessage());
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L_Failed(""));
            jsonResult.setCode(Constants.FAILED);
        }
        return jsonResult;
    }

    /**
     * 查询用户登陆日志
     * @param
     */
    @ApiOperation(value = "查找登陆日志",notes = "根据当前用户查找")
    @RequestMapping(value = "/findLogs",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult findLogs() {
        String userId = UserUtils.getUser().getId();
        JsonResult jsonResult = new JsonResult();
        try {
            jsonResult.setData(memberLoginService.find(userId));
            jsonResult.setSuccess(true);
            jsonResult.setMsg(Language.L_Success(""));
            jsonResult.setCode(Constants.SUCCESS);
        } catch (Exception e) {
            log.info("MarketController findUser error：" + e.getMessage());
            jsonResult.setSuccess(false);
            jsonResult.setMsg(Language.L_Failed(""));
            jsonResult.setCode(Constants.FAILED);
            e.printStackTrace();
        }
        return jsonResult;
    }


}
