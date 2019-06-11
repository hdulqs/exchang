/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2017-11-19 12:42:46 
*/
package com.batsoft.service.module.otc.service.impl;

import com.batsoft.core.common.PageFactory;
import com.batsoft.core.common.PageResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.otc.Releaseproject;
import com.batsoft.model.module.otc.vo.ReleaseprojectVo;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.otc.dao.ReleaseprojectDao;
import com.batsoft.service.module.otc.service.ReleaseprojectService;
import com.batsoft.utils.StringUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* <p> ReleaseprojectServiceImpl </p>
* @author: 
* @Date :  2017-11-19 12:42:46 
*/
@Service("releaseprojectService")
public class ReleaseprojectServiceImpl extends BaseServiceImpl<Releaseproject, String> implements ReleaseprojectService{

@Autowired
private ReleaseprojectDao releaseprojectDao;


    @Override
    public Releaseproject find(String id) {
        return releaseprojectDao.find(id);
    }

    @Override
    public PageResult findList(HttpServletRequest request) {
        String coin = request.getParameter("coin");
        String country = request.getParameter("country");
        String otcType = request.getParameter("otcType");
        String currency = request.getParameter("currency");
        String payType = request.getParameter("payType");
        String userId = UserUtils.getUser().getId();
        Map<String,Object> map = new HashMap<String,Object>();

        if(!StringUtils.isEmpty(coin)){
            map.put("coin",coin);
        }
        if(!StringUtils.isEmpty(country)){
            map.put("country",country);
        }
        if(!StringUtils.isEmpty(otcType)){
            map.put("otcType",otcType);
        }
        if(!StringUtils.isEmpty(currency)){
            map.put("currency",currency);
        }
        if(!StringUtils.isEmpty(userId)){
            map.put("userId",userId);
        }
        if(!StringUtils.isEmpty(payType)){
            map.put("payType","%"+payType+"%");
        }
        //封装必要参数
        QueryFilter filter = new QueryFilter(Releaseproject.class, request);
        //分页插件
        Page<ReleaseprojectVo> page = PageFactory.getPage(filter);

        //查询方法
        releaseprojectDao.findPageBySql(map);

        return new PageResult(page, filter);
    }

    @Override
    public List<ReleaseprojectVo> findProjectIndex(String coin, Integer pageSize, Integer otcType) {
        return releaseprojectDao.findProjectIndex(coin,pageSize,otcType);
    }
}
