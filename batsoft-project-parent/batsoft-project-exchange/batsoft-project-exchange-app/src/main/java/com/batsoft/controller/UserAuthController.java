package com.batsoft.controller;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.member.vo.UserAuthVo;
import com.batsoft.model.module.member.vo.UserVo;
import com.batsoft.service.module.member.service.UserService;
import com.batsoft.service.module.member.service.UserUtils;
import com.jcraft.jsch.UserAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "认证controller")
@RequestMapping("/auth")
@RestController
public class UserAuthController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "用户认证")
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public JsonResult postUserAuth(@RequestBody UserAuthVo userAuthVo){
        JsonResult jsonResult = new JsonResult();
        UserVo userVo = UserUtils.getUser(false);
        if(userVo == null || userVo.getId() == null){
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.ResultCode.NO_LOGIN);
            jsonResult.setMsg(Language.L_Failed("msg_no_login"));
        }
        return  userService.addUserAuthVo(userAuthVo);
    }
}
