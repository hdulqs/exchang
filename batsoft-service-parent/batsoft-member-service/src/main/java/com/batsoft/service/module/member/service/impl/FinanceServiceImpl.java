/**
* Copyright:    http://www.batsoft.cn
* @author:      Lucl
* @version:     V1.0
* @Date:        2017-11-17 17:28:47 
*/
package com.batsoft.service.module.member.service.impl;

import com.batsoft.core.common.Constants;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.member.Finance;
import com.batsoft.service.module.member.dao.FinanceDao;
import com.batsoft.service.module.member.service.FinanceService;
import com.batsoft.service.module.member.service.UserUtils;
import com.batsoft.service.module.system.service.config.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* <p> FinanceServiceImpl </p>
* @author: 
* @Date :  2017-11-17 17:28:47 
*/
@Service("financeService")
public class FinanceServiceImpl extends BaseServiceImpl<Finance, String> implements FinanceService{

@Autowired
private FinanceDao financeDao;
    @Autowired
    private AppConfigService appConfigService;

    @Override
    public boolean saveCurrency(List<Finance> list) {
        return false;
    }

    @Override
    public boolean saveCNY() {
        Finance cny=new Finance();
        cny.setCurrencyName(Constants.CNY_NAME);
        cny.setCurrencyCode(Constants.CNY_CODE);
        cny.setCurrencyLogo("");
        cny.setUserId(UserUtils.getUser().getId());
        cny.setUserMobile(UserUtils.getUser().getUserMobile());
        cny.setUserRealName(UserUtils.getUser().getRealName());
        return false;
    }
}
