package com.batsoft.controller;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.common.JsonResult;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.service.config.AppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Api(value = "ConfigController",description = "配置文件")
@RequestMapping(value = "/config")
@RestController
public class ConfigController{

    @Autowired
    private AppConfigService appConfigService;

    @ApiOperation(value = "读取所有的配置")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public JsonResult getAllConfig(){
        HashMap<String,Object> map = new HashMap<>();
        JsonResult jsonResult = new JsonResult(true);
        String fileHost = appConfigService.findValueByKey(AppConfig.FILEHOST);
        map.put(AppConfig.FILEHOST,fileHost);
        map.put("static", ApplicationConfigure.staticAssets);
        jsonResult.setData(map);
        return  jsonResult;
    }
}