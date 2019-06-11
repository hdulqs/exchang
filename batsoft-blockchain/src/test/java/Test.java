

import java.math.BigInteger;

public class Test {
    public static void main(String[] args) {



        BigInteger  num = BigInteger.valueOf(5712200);

        String redisBlockHigh= "5712199";
        Long redisNumber =0L;

        if(redisBlockHigh!=null){
            redisNumber = Long.valueOf(redisBlockHigh);
        }

        // 最新区块高度
        Long count = getScanCount(redisNumber, Long.valueOf(num.toString()), 15L);
        Long endNumber = redisNumber + count;
        //如果缓冲中获取的区块为0 不执行

            while (redisNumber < endNumber) {


                redisNumber++;
            }


        System.out.println("区块高度 info====="+redisNumber.toString());


    }

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
