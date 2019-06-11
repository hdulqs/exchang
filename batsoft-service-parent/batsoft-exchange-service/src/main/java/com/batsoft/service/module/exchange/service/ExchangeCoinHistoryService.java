/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2018-04-14 10:22:47
 */

package com.batsoft.service.module.exchange.service;

import com.batsoft.core.service.BaseService;
import com.batsoft.model.module.exchange.EntrustInfo;
import com.batsoft.model.module.exchange.ExchangeCoinHistory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>EntrustInfoService</p>
 *
 * @author: Bat Admin
 * @Date :  2018-04-14 10:22:47
 */
public interface ExchangeCoinHistoryService extends BaseService<ExchangeCoinHistory, String> {

	ExchangeCoinHistory findByCoinCodeAndDate(String coinCode, Date date);

	BigDecimal sumOfTotalByCoinCode(String coinCode);

	List<ExchangeCoinHistory>  getListByDate(Date date);
}
