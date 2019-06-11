package com.batsoft.service.module.member.service;

import com.batsoft.core.common.PageResult;
import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.member.PromotionAward;
import com.batsoft.model.module.member.User;

import java.util.List;


public interface PromotionAwardService extends BaseService<PromotionAward, String> {


     PageResult findPrototionAward(int page, int pageSize, User userid);

     List<PromotionAward> getParentPromotionIdList(String userid);
}
