/**
* Copyright:    http://www.batsoft.cn
* @author:      Yuan Zhicheng
* @version:     V1.0
* @Date:        2017-12-08 09:56:42 
*/

package com.batsoft.model.module.otc.vo;

import com.batsoft.model.module.otc.ExFinance;
import lombok.Data;
import lombok.ToString;

/**
* 
* <p>ExFinanceVo</p>
* @author: Yuan Zhicheng
* @Date :  2017-12-08 09:56:42 
*/
@Data
@ToString
public class ExFinanceVo extends ExFinance {
    /**
     * coin logo
     */
    private String coinLogo;

    /**
     * 是否允许充值
     */
    private String allowRecharge;

    /**
     * 是否允许提现
     */
    private String allowDeposit;
}
