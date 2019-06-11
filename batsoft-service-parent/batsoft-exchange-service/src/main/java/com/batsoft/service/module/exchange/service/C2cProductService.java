/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-17 19:59:28 
*/

package com.batsoft.service.module.exchange.service;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseService;

import com.batsoft.model.module.exchange.C2cProduct;

/**
* <p>C2cProductService</p>
* @author: LouSir
* @Date :  2018-05-17 19:59:28 
*/
public interface C2cProductService  extends BaseService<C2cProduct, String>{

    /**
     * 查询缓存中有效数据
     * @return
     */
    JsonResult findProducts();

    /**
     * 查询对应代币
     * @param coinCode
     * @return
     */
    JsonResult findProduct(String coinCode);

    /**
     * 插入c2c交易订单记录
     * @param coinCode
     * @return
     */
    C2cProduct getProductByCode(String coinCode);

    /**
     * 添加C2C产品缓存
     */
    void addProductsCache();
}
