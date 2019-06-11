package com.batsoft.app.blockchain.remote;

import com.alibaba.fastjson.JSONObject;
import com.batsoft.blockchain.common.Coin;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.core.web.controller.GenericController;
import com.batsoft.model.module.blockchain.ErcCoin;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.service.module.blockchain.service.DepositWalletService;
import com.batsoft.service.module.blockchain.service.ErcCoinService;
import com.batsoft.service.module.blockchain.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@RestController("depositRemoteController")
//@RequestMapping("/remote")

/**
 * 疑似提币安全漏洞
 */
public class DepositRemoteController extends GenericController {

    @Autowired
    private DepositWalletService depositWalletService;

    @Autowired
    private WalletService walletService;
    @Autowired
    private ErcCoinService ercCoinService;

    /**
     * 转币
     * @param data
     */
    @PostMapping("/coinSend")
    public void coinSend(@RequestBody JSONObject data) {

        String coinCode=data.get("coinCode").toString();
        String toAddress=data.get("toAddress").toString();
        String amount=data.get("amount").toString();

        QueryFilter filter=new QueryFilter(Wallet.class);
        filter.addFilter("coinCode_EQ",coinCode);
        Wallet wallet=this.walletService.get(filter);

        if(wallet!=null){
            if(wallet.getWalletType().equals(Coin.BTC.getType())){
                //depositWalletService.sendBtcChainCoin(coinCode,toAddress,amount);
            }else if(wallet.getWalletType().equals(Coin.ETH.getType())){
                //depositWalletService.sendEthChainCoin(coinCode,toAddress,amount);
            }
        }else {
            QueryFilter filter1=new QueryFilter(ErcCoin.class);
            filter1.addFilter("coinCode_EQ",coinCode);
            ErcCoin ercCoin=ercCoinService.get(filter1);
            if(ercCoin!=null){
                if(ercCoin.getBlock().equals(Coin.ETH.getType())){
                    //depositWalletService.sendEthChainCoin(coinCode,toAddress,amount);
                }
            }

        }


    }
}
