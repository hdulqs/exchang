/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.otc;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.model.module.otc.vo.ReleaseprojectVo;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.service.ReleaseprojectService;
import com.batsoft.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>OTC项目管理</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "项目管理API" )
@Controller("otcProjectController")
@RequestMapping("/otc")
@Slf4j
public class OtcProjectController extends BaseController<Releaseproject,String> {
    @Resource(name="releaseprojectService")
    @Override
    public void setService(BaseService<Releaseproject, String> service) {
        super.service = service;
    }



    /**
     * 发布广告
     *
     * @return
     */
    @RequestMapping(value = "/advertise",method = RequestMethod.GET)
    public String advertise () {
        return "otc/advertise";
    }


    /**
     * otc 列表頁
     *
     * @return
     */
    @ApiOperation(value = "项目列表")
    @RequestMapping(value = "/findAdList",method = RequestMethod.GET)
    @ResponseBody
    public PageResult findAdList (HttpServletRequest request) {
        return ((ReleaseprojectService)super.service).findList(request);
    }


    /**
     * otc 列表頁
     *
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list () {
        return "otc/list";
    }
    /**
     * 修改广告
     *
     * @return
     */
    @RequestMapping(value = "/user/project/update/{id}",method = RequestMethod.GET)
    public String update (@PathVariable String id,Model model) {
        Releaseproject releaseproject =service.get(id);
        if(UserUtils.getUser().getId().equals(releaseproject.getUserId())) {
            model.addAttribute("project",releaseproject);
            return "otc/update";
        }
        return "";
    }
    /**
     * otc 详情页
     * @return
     */
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public String info (@PathVariable String id,Model model) {
        model.addAttribute("id",id);

        return "otc/info";
    }

    /**
     * otc 详情数据
     * @return
     */
    @ApiOperation(value = "项目详情",notes = "根据项目id查询详情")
    @RequestMapping(value = "/infoData/{id}",method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(value = "项目id",name = "id" ,dataType = "String",paramType = "query")})
    @ResponseBody
    public Releaseproject infoData (@PathVariable String id) {
        return ((ReleaseprojectService)super.service).find(id);
    }


    /**
     * 保存或修改
     * @param
     * @return
     */
    @ApiOperation(value = "保存项目")
    @Override
    @RequestMapping(value = "/user/project/save", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(value = "项目对象",name = "releaseproject" ,paramType = "body")})
    @ResponseBody
    public JsonResult save(@RequestBody Releaseproject releaseproject) {
        releaseproject.setUserId(UserUtils.getUser().getId());
        releaseproject.setUserName(UserUtils.getUser().getUserName());
        releaseproject.setUserNick(UserUtils.getUser().getUserNick());
        if(StringUtils.isEmpty(releaseproject.getProjectNum())) {
            releaseproject.setProjectNum(StringUtils.createRandom(false, 10));
        }
        return super.save(releaseproject);
    }


    /**
     * 删除
     * @param
     * @return
     */
    @RequestMapping(value = "/user/project/remove", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult save(@RequestParam(value = "ids") String[] ids) {
        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        Releaseproject releaseproject =null;
        try {
            for (String PK : ids) {

                releaseproject=service.get(PK);
                //鉴权
                if(UserUtils.getUser().getId().equals(releaseproject.getUserId())) {
                    releaseproject.setDel(Releaseproject.DEL_FLAG_DELETE);
                    service.update(releaseproject);
                }
            }
            result.setSuccess(true);
            result.setMsg("删除成功");
            result.setCode(Constants.SUCCESS);
            result.setData(data);
        } catch (Exception e) {
            log.info("删除失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("删除失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 开启
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/project/check", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult check(@RequestParam(value = "id") String id) {
        JsonResult result = new JsonResult();
        List<String> data = new ArrayList<String>();
        Releaseproject releaseproject =null;
        try {
            releaseproject=service.get(id);
            //鉴权
            if(UserUtils.getUser().getId().equals(releaseproject.getUserId())) {
                releaseproject.setStatus(Releaseproject.SECURITY1);
                service.update(releaseproject);
                result.setSuccess(true);
                result.setMsg("发布成功");
                result.setCode(Constants.SUCCESS);
                result.setData(data);
            }else {
                result.setSuccess(false);
                result.setMsg("操作非法");
                result.setCode(Constants.FAILED);
            }
        } catch (Exception e) {
            log.info("发布失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("发布失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }

    /**
     * 关闭
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/project/cancel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult cancel(@RequestParam(value = "id") String id) {
        JsonResult result = new JsonResult();
        Releaseproject releaseproject =null;
        try {
            releaseproject = service.get(id);
            //鉴权
            if(UserUtils.getUser().getId().equals(releaseproject.getUserId())) {
	            releaseproject.setStatus(Releaseproject.SECURITY0);
	            service.update(releaseproject);
	            result.setSuccess(true);
	            result.setMsg("关闭成功");
	            result.setCode(Constants.SUCCESS);
            } else {
                result.setSuccess(false);
                result.setMsg("操作非法");
                result.setCode(Constants.FAILED);
            }
        } catch (Exception e) {
            log.info("关闭失败：" + e.getMessage());
            result.setSuccess(false);
            result.setMsg("关闭失败");
            result.setCode(Constants.FAILED);
        }
        return result;
    }
    
    /**
     * 首页购买/出售信息
     * @param coin 币种类型
     * @param pageSize 查询数量
     * @param otcType 求购类型
     * @return
     */
    @RequestMapping(value = "/ad/{coin}/{pageSize}/{otcType}", method = RequestMethod.GET)
    @ResponseBody
    public List<ReleaseprojectVo> findProjectIndex(@PathVariable String coin, @PathVariable Integer pageSize, @PathVariable Integer otcType) {
        return ((ReleaseprojectService)super.service).findProjectIndex(coin,pageSize,otcType);
    }



}
