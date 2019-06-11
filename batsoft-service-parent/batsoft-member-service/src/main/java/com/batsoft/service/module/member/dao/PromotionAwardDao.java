package com.batsoft.service.module.member.dao;

import com.batsoft.core.dao.BaseDao;
import com.batsoft.model.module.member.PromotionAward;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface PromotionAwardDao extends BaseDao<PromotionAward, String> {

    List<PromotionAward> findPromotionAward(HashMap map);

    List<PromotionAward> findParentPromotionAward(HashMap map);

    Long findPromotionAwardTotal(HashMap map);

    BigDecimal findPromotionAwardSumStatus(HashMap map);
}
