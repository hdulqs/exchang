/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-05-25 10:54:59 
*/
package com.batsoft.service.module.blockchain.service.impl;

import com.batsoft.blockchain.common.WalletResult;
import com.batsoft.blockchain.rpc.BaseRpc;
import com.batsoft.blockchain.rpc.Config;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.RedisKeyConstant;
import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.blockchain.Wallet;
import com.batsoft.service.module.blockchain.dao.WalletDao;
import com.batsoft.service.module.blockchain.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* <p> WalletServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-05-25 10:54:59 
*/
@Service("walletService")
public class WalletServiceImpl extends BaseServiceImpl<Wallet, String> implements WalletService{

@Autowired
private WalletDao walletDao;


    @Autowired
    private RedisService redisService;

    @Override
    public Long findBlockHigh(Wallet wallet) {
        BaseRpc baseRpc = new BaseRpc();
        Config config = new Config(wallet.getWalletType(),wallet.getWalletCode(), wallet.getRpcProtocol(), wallet.getRpcIp(), wallet.getRpcPort(), wallet.getRpcAccount(), wallet.getRpcPassword());

        Long blockHigh=0L;
        WalletResult result=baseRpc.findEthBlockNumber(config);
        if(result.getSuccess()){
            blockHigh=Long.valueOf(result.getData().toString());
            redisService.set(RedisKeyConstant.BLOCK_HIGH + wallet.getWalletCode(),result.getData().toString(),RedisService.CACHE_TIME);
        }

        return  blockHigh;
    }
}
