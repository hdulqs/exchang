/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.cms.article;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.cms.Single;
import com.batsoft.model.module.cms.vo.SingleVo;
import com.batsoft.service.module.cms.service.SingleService;
import com.batsoft.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>single</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Api(description = "链接")
@Controller("singleController")
@RequestMapping("/")
@Slf4j
public class SingleController extends GenericController {

    @Autowired
    private SingleService singleService;
    /**
     * 关于我们主页
     *
     * @return
     */
    @RequestMapping(value = "/about",method = RequestMethod.GET)
    public String about() {
        return "cms/single/about";
    }

    /**
     * 服务条款
     *
     * @return
     */
    @RequestMapping(value = "/terms",method = RequestMethod.GET)
    public String terms() {
        return "cms/single/terms";
    }

    /**
     * 隐私申明
     *
     * @return
     */
    @RequestMapping(value = "/privacy",method = RequestMethod.GET)
    public String about_team() {
        return "cms/single/privacy";
    }

    /**
     * 费率说明
     *
     * @return
     */
    @RequestMapping(value = "/fees",method = RequestMethod.GET)
    public String about_aptitude() {
        return "cms/single/fees";
    }

    /**
     * 媒体报道
     *
     * @return
     */
    @RequestMapping(value = "/about/about_media",method = RequestMethod.GET)
    public String about_media() {
        return "cms/single/about_media";
    }

    /**
     * 联系我们
     *
     * @return
     */
    @RequestMapping(value = "/contact",method = RequestMethod.GET)
    public String about_contact() {
        return "cms/single/contact";
    }

    /**
     * 合作机构
     *
     * @return
     */
    @RequestMapping(value = "/about/about_cooperation",method = RequestMethod.GET)
    public String about_cooperation() {
        return "cms/single/about_cooperation";
    }

    /**
     * 帮助中心
     *
     * @return
     */
    @RequestMapping(value = "/help",method = RequestMethod.GET)
    public String help() {
        return "cms/single/help";
    }

    /**
     * 安全保障
     *
     * @return
     */
    @RequestMapping(value = "/help/help_security",method = RequestMethod.GET)
    public String help_security() {
        return "cms/single/help_security";
    }

    /**
     * 用户协议
     *
     * @return
     */
    @RequestMapping(value = "/help/help_protocol",method = RequestMethod.GET)
    public String help_protocol() {
        return "cms/single/help_protocol";
    }

    /**
     * 法律声明
     *
     * @return
     */
    @RequestMapping(value = "/help/help_law",method = RequestMethod.GET)
    public String help_law() {
        return "cms/single/help_law";
    }

    /**
     * 费率声明
     *
     * @return
     */
    @RequestMapping(value = "/help/help_detail",method = RequestMethod.GET)
    public String help_detail() {
        return "cms/single/help_detail";
    }

    /**
     * app下载
     *
     * @return
     */
    @RequestMapping(value = "/help/help_app",method = RequestMethod.GET)
    public String help_app() {
        return "cms/single/help_app";
    }


    /**
     * 根据id查询单页面
     * @param key 关键字
     * @return
     */
    @ApiOperation(value = "关键字查询单页面")
    @ApiImplicitParam(value = "关键字",name = "kye",paramType = "path",dataType = "String" ,required = true)
    @RequestMapping(value = "/single/{key}",method = RequestMethod.GET)
    public String single(@PathVariable("key") String key, Model model){
        try {
            if(StringUtils.isNotEmpty(key)){
                QueryFilter filter = new QueryFilter(Single.class);
                filter.addFilter("single_key=",key);
                filter.addFilter("status=",Single.STATUS1);
                filter.orderBy("update_time desc");
                List<Single> list = singleService.find(filter);
                if(list!=null&&list.size()>0){
                    Single single = list.get(0);
                    SingleVo singleVo = new SingleVo();
                    BeanUtils.copyProperties(single,singleVo);
                    model.addAttribute(singleVo);
                }else{
                    SingleVo singleVo = new SingleVo();
                    singleVo.setSingleForm("文章正在努力编写，敬请期待~");
                    model.addAttribute(singleVo);
                }
            }else{
                SingleVo singleVo = new SingleVo();
                singleVo.setSingleForm("文章正在努力编写，敬请期待~");
                model.addAttribute(singleVo);
            }

        }catch (Exception e){
            SingleVo singleVo = new SingleVo();
            singleVo.setSingleForm("查询出错了"+e.getMessage());
            model.addAttribute(singleVo);
            e.printStackTrace();
        }
        return "cms/single/single";
    }

    /**
     * 查询所有文章列表
     * @return
     */
    @RequestMapping(value = "/singleAll",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult singleAll(){
        return singleService.singleAll();
    }

}
