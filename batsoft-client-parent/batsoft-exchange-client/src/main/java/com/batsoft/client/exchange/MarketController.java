/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
package com.batsoft.client.exchange;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.batsoft.core.web.controller.GenericController;
import com.batsoft.service.module.exchange.service.CoinPairService;
import com.batsoft.utils.StringUtils;

/**
 * <p>交易所行情中心</p>
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017-07-06 17:45:44
 */
@Controller("marketController")
@RequestMapping("/")
public class MarketController extends GenericController {

    @Autowired
    private CoinPairService coinPairService;

    @RequestMapping("/market")
    public String market(HttpServletRequest request, Model model) {
        String symbol = request.getParameter("symbol");
        String tradeCoins =coinPairService.findMapTradeCoins();
        String digits = coinPairService.findCoinPairDigit();
        model.addAttribute("tradeCoins", tradeCoins);
        model.addAttribute("digits", digits);
        if (StringUtils.isEmpty(symbol)) {
            return "redirect:/market?symbol=BT_USDT";
        } else {
            model.addAttribute("symbol", symbol.split("_")[0]);
            model.addAttribute("priceSymbol", symbol.split("_")[1]);
        }
        return "exchange/market";
    }

}