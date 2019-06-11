/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-28 17:39:53 
*/
package com.batsoft.service.module.otc.service.impl;

import com.batsoft.core.common.PageFactory;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.otc.ProjectOrder;
import com.batsoft.model.module.otc.vo.ProjectOrderVo;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.dao.ProjectOrderDao;
import com.batsoft.service.module.otc.service.ProjectOrderService;
import com.batsoft.utils.StringUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
* <p> ProjectOrderServiceImpl </p>
* @author: 
* @Date :  2017-11-28 17:39:53 
*/
@Service("projectOrderService")
public class ProjectOrderServiceImpl extends BaseServiceImpl<ProjectOrder, String> implements ProjectOrderService{

@Autowired
private ProjectOrderDao projectOrderDao;

    @Override
    public PageResult findList(HttpServletRequest request) {
        Integer status = Integer.valueOf(request.getParameter("status").toString());
        String userId = UserUtils.getUser().getId();
        Map<String,Object> map = new HashMap<String,Object>();

        map.put("status",status);
        if(!StringUtils.isEmpty(userId)){
            map.put("userId",userId);
        }

        //封装必要参数
        QueryFilter filter = new QueryFilter(ProjectOrder.class, request);
        //分页插件
        Page<ProjectOrderVo> page = PageFactory.getPage(filter);

        //查询方法
        projectOrderDao.findPageBySql(map);

        return new PageResult(page, filter);
    }
}
