/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-28 10:34:53
 */
package com.batsoft.service.module.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.JedisDataSourceSignleton;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.common.i18n.Language;
import com.batsoft.model.module.cms.Single;
import com.batsoft.service.module.cms.dao.SingleDao;
import com.batsoft.service.module.cms.service.SingleService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p> SingleServiceImpl </p>
 * @author: LouSir
 * @Date :  2018-05-28 10:34:53
 */
@Service("singleService")
public class SingleServiceImpl extends BaseServiceImpl<Single, String> implements SingleService {

    public static final String ARTICLE_SINGLES = "article_singles";
    
    @Autowired
    private SingleDao singleDao;
    
    @Autowired
    private RedisService redisService;
    
    private JedisDataSourceSignleton jedisClient = JedisDataSourceSignleton.getInstance();
    
    /**
     * 查询有效文章单页并放置缓存
     * @return
     */
    private String singlesCache() {
        String singles = redisService.get(ARTICLE_SINGLES);
        if (StringUtils.isEmpty(singles)) {
            QueryFilter queryFilter = new QueryFilter(Single.class);
            queryFilter.addFilter("status=", 1);
            List<Single> list = this.find(queryFilter);
            singles = JSON.toJSONString(list);
            redisService.set(ARTICLE_SINGLES, singles, RedisService.CACHE_TIME);
        }
        return singles;
    }

    /**
     * 查询有效单页列表
     * @return
     */
    @Override
    public JsonResult singleAll() {
        JsonResult jsonResult = new JsonResult();
        try {
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setData(singlesCache());
            jsonResult.setMsg(Language.L(""));
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
            jsonResult.setMsg("系统异常" + e.getMessage());
        }
        return jsonResult;
    }


}
