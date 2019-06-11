/**
 * Copyright:    http://www.batsoft.cn
 *
 * @author: LouSir
 * @version: V1.0
 * @Date: 2018-05-17 19:59:28
 */
package com.batsoft.service.module.exchange.service.impl;

import com.alibaba.fastjson.JSON;
import com.batsoft.core.cache.RedisService;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.QueryFilter;
import com.batsoft.model.module.exchange.C2cProduct;
import com.batsoft.service.module.exchange.dao.C2cProductDao;
import com.batsoft.service.module.exchange.service.C2cProductService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> C2cProductServiceImpl </p>
 * @author: LouSir
 * @Date :  2018-05-17 19:59:28
 */
@Service("c2cProductService")
public class C2cProductServiceImpl extends BaseServiceImpl<C2cProduct, String> implements C2cProductService {

    public static final String CACHE_PREFIX = "exchange:";
    public static final String CACHE_PREFIX_PRODUCTS = CACHE_PREFIX + "c2c_products";
    @Autowired
    private C2cProductDao c2cProductDao;
    @Autowired
    private RedisService redisService;


    /**
     * 添加C2C产品缓存
     */
    @Override
    public void addProductsCache(){
        List<C2cProduct> list = this.listProducts();
        redisService.set(CACHE_PREFIX_PRODUCTS, JSON.toJSONString(list), RedisService.CACHE_TIME);
    }
    /**
     * 查询所有有效代币
     * @return
     */
    private List<C2cProduct> listProducts() {
        QueryFilter filter = new QueryFilter(C2cProduct.class);
        filter.addFilter("status=", 1);
        filter.orderBy("sort asc");
        List<C2cProduct> list = this.find(filter);
        return list;
    }

    /**
     * 缓存中获取有效代币
     */
    @Override
    public JsonResult findProducts() {
        JsonResult jsonResult = new JsonResult();
        try {
            String products = redisService.get(CACHE_PREFIX_PRODUCTS);
            List<C2cProduct> list = null;
            if (StringUtils.isEmpty(products)) {
                addProductsCache();
            } else {
                list = JSON.parseArray(products, C2cProduct.class);
            }
            jsonResult.setMsg("查询成功");
            jsonResult.setSuccess(true);
            jsonResult.setCode(Constants.SUCCESS);
            jsonResult.setData(list);
        } catch (Exception e) {
            jsonResult.setMsg("查询错误-" + e.getMessage());
            jsonResult.setSuccess(false);
            jsonResult.setCode(Constants.FAILED);
        }
        return jsonResult;
    }

    /**
     * 查询对应代币
     * @param coinCode
     * @return
     */
    @Override
    public C2cProduct getProductByCode(String coinCode){
        C2cProduct product = null;
        JsonResult jsonResult0 = this.findProducts();
        List<C2cProduct> list = (List<C2cProduct>)jsonResult0.getData();
        if (list!=null&&list.size()>0){
            for(C2cProduct p:list){
                if(coinCode.equals(p.getCoinCode())){
                    product = p;
                    break;
                }
            }
        }
        return product;
    }

    /**
     * 获取某个币种的缓存数据
     * @param coinCode
     * @return
     */
    @Override
    public  JsonResult findProduct(String coinCode){
        JsonResult jsonResult = new JsonResult();
        C2cProduct product = getProductByCode(coinCode);
        jsonResult.setSuccess(true);
        jsonResult.setCode(Constants.SUCCESS);
        jsonResult.setData(product);
        jsonResult.setMsg("成功");
        return jsonResult;
    }
}
