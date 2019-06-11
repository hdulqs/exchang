package com.batsoft.client.cms.friend;

import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.BaseService;
import com.batsoft.core.web.controller.BaseController;
import com.batsoft.model.module.cms.Friend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lucl on 2017/12/7.
 */
@Controller("friendLinkController")
@RequestMapping("/friend")
@Slf4j
public class FriendLinkController extends BaseController<Friend,String> {

    @Resource(name="friendService")
    @Override
    public void setService(BaseService<Friend, String> service) {
        super.service=service;
    }
    /**
     * 查询有效的banner
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult list(){
        QueryFilter filter = new QueryFilter(Friend.class);
        filter.addFilter("del=", 0);
        filter.orderBy("sort ASC");
        List<Friend> frends = service.find(filter);
        return showSuccessJson("查询成功",frends);
    }
}
