package com.batsoft.service.module.member.service.impl;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.member.PromotionAward;
import com.batsoft.model.module.member.User;
import com.batsoft.service.module.member.dao.PromotionAwardDao;
import com.batsoft.service.module.member.service.PromotionAwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("promoterAwardService")
public class PromotionAwardServiceImpl extends BaseServiceImpl<PromotionAward, String> implements PromotionAwardService {

    @Autowired
    private PromotionAwardDao promotionAwardDao;


    @Override
    public PageResult findPrototionAward(int page, int pageSize,User  userDate) {
        PageResult pageResult = new PageResult();
        int from = pageSize*(page-1);
        HashMap<String,Object> mapMap = new HashMap<>();
        mapMap.put("from",from);
        mapMap.put("pageSize",pageSize);
        mapMap.put("id",userDate.getId());
        List<PromotionAward> customerAccountRecords = promotionAwardDao.findPromotionAward(mapMap);
        Long rows = promotionAwardDao.findPromotionAwardTotal(mapMap);
        HashMap<String,Object> sumHashMap = new HashMap<>();
        sumHashMap.put("id",userDate.getId());
        if (userDate.getPromotionAward() == null) {
            userDate.setPromotionAward(new BigDecimal(0));
        }
        BigDecimal sumRealAward = userDate.getPromotionAward();
        BigDecimal sumAward = promotionAwardDao.findPromotionAwardSumStatus(sumHashMap);
        pageResult.setRows(customerAccountRecords);
        Map<String,Long> map = new HashMap<>();
        map.put("total",rows);
        map.put("pageSize", (long) pageSize);
        map.put("current", (long) page);
        map.put("totalAward", sumAward.longValue());
        map.put("totalRealAward", sumRealAward.longValue());
        map.put("totalPromotionNum", rows);
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(rows);
        pageResult.setPagination(map);
        return pageResult;
    }

    @Override
    public List<PromotionAward> getParentPromotionIdList(String userid) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",userid);
        return promotionAwardDao.findParentPromotionAward(map);
    }
}
