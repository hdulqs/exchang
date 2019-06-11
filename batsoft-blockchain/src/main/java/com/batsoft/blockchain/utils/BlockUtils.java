package com.batsoft.blockchain.utils;

public class BlockUtils {
    /**
     * 获取本次扫描的个数
     *
     * @author: admin
     * @param: @param
     *             redisNumber
     * @param: @param
     *             height
     * @param: @param
     *             maxCount(max scan blocknumber)
     * @param: @return
     * @return: Long
     * @Date : 2018年5月9日 上午10:33:49
     */
    public static Long getScanCount(Long redisNumber, Long height, Long maxCount) {
        Long result = 0L;
        // 每次处理的区块不能超过
        if (height > redisNumber) {
            if ((height - redisNumber) >= maxCount) {
                result = maxCount;
            } else {
                result = height - redisNumber;
            }
        }
        return result;
    }
}
