/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-29 23:10:22
 */
package com.batsoft.app.exchange;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.batsoft.app.exchange.pattern.singleton.RobotSingleton;
import com.batsoft.app.exchange.pattern.singleton.RobotSingleton;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.model.PksData;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.exchange.RobotTrade;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * <p>RobotTradeController</p>
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-05-29 23:10:22
 */
@RestController("robotTradeController")
@RequestMapping("/exchange/robotTrade")
@Slf4j
public class RobotTradeController extends BaseController<RobotTrade, String> {
	
    @Autowired
    private UserService userService;
    
    @Resource(name = "robotTradeService")
    @Override
    public void setService(BaseService<RobotTrade, String> service) {
        super.service = service;
    }
    
    /**
     * 查找
     * @param
     * @return
     */
    @RequestMapping(value = "/find/{id}")
    @RequiresPermissions(value = {"exchange:robotTrade:see", "exchange:robotTrade:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult find(@PathVariable String id) {
        return super.find(id);
    }

    /**
     * 分页list
     * @param
     * @return
     */
    @RequestMapping(value = "/list")
    @RequiresPermissions("exchange:robotTrade:list")

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
    @RequiresPermissions(value = {"exchange:robotTrade:add", "exchange:robotTrade:edit"}, logical = Logical.OR)
    @ResponseBody
    @Override
    public JsonResult save(@RequestBody RobotTrade robotTrade) {
        return super.save(robotTrade);
    }

    /**
     * 删除操作
     * @param
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @RequiresPermissions("exchange:robotTrade:remove")
    @ResponseBody
    public JsonResult remove(@RequestBody PksData pksData) {
        return super.remove(pksData.getIdsArr());
    }

    /**
     * 开启
     * @param
     * @return
     */
	@RequestMapping(value = "/open", method = RequestMethod.POST)
    @RequiresPermissions("exchange:robotTrade:open")
    @ResponseBody
    public JsonResult open(@RequestBody PksData pksData) {
        JsonResult jsonResult = new JsonResult();

        try {
            for (String PK : pksData.getIdsArr()) {
                RobotTrade robotTrade = this.findById(PK);
                
                // 调用 开启 方法 robotTrade 中有需要的参数
                User user = userService.findUser(robotTrade.getUserName());
                
                // 新版本机器人
                RobotSingleton.getInstance().cancel(robotTrade);
                RobotSingleton.getInstance().run(user , robotTrade);
                
                //更新状态
                robotTrade.setStatus(RobotTrade.STATUS1);
                this.service.update(robotTrade);
            }
            jsonResult.setSuccess(true);
            jsonResult.setMsg("开启成功");
        }catch (Exception e){
        	e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMsg("开启失败");
        }

        return jsonResult;
    }


    /**
     * 关闭
     * @param
     * @return
     */
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @RequiresPermissions("exchange:robotTrade:stop")
    @ResponseBody
    public JsonResult stop(@RequestBody PksData pksData) {
        JsonResult jsonResult=new JsonResult();
        try {
            for (String PK : pksData.getIdsArr()) {
                RobotTrade robotTrade = this.findById(PK);
				
                // 新版停止机器人
                RobotSingleton.getInstance().cancel(robotTrade);
                
                //更新状态
                robotTrade.setStatus(RobotTrade.STATUS0);
                this.service.update(robotTrade);
            }
            jsonResult.setSuccess(true);
            jsonResult.setMsg("关闭成功");
        }catch (Exception e){
        	e.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMsg("关闭失败");
        }
        return jsonResult;
    }

}
