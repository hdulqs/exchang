package com.batsoft.client.cms.banner;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.Banner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lucl on 2017/10/10.
 */
@Controller("bannerController")
@RequestMapping("/banner")
@Slf4j
public class BannerController extends BaseController<Banner,String> {

    @Resource(name="bannerService")
    @Override
    public void setService(BaseService<Banner, String> service) {
        super.service=service;
    }
    /**
     * 查询有效的banner
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(){
        QueryFilter filter = new QueryFilter(Banner.class);
        filter.addFilter("del=", Banner.DEL_FLAG_NORMAL);
        filter.addFilter("display=",Banner.DISPLAY1);
        filter.orderBy("sort ASC");
        List<Banner> banners = service.find(filter);
        return showSuccessJson("查询成功",banners);
    }
}
