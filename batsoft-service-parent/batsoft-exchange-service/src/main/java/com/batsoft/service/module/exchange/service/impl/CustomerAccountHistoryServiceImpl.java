package com.batsoft.service.module.exchange.service.impl;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.exchange.CustomerAccount;
import com.batsoft.model.module.exchange.CustomerAccountHistory;
import com.batsoft.service.module.exchange.service.CustomerAccountHistoryService;
import org.springframework.stereotype.Service;

@Service("customerAccountHistoryService")
public class CustomerAccountHistoryServiceImpl extends BaseServiceImpl<CustomerAccountHistory, String> implements CustomerAccountHistoryService {

}
