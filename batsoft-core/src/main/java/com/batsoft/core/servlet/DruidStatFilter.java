/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月14日 下午2:39:34
 */
package com.batsoft.core.servlet;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月14日 下午2:39:34 
 */

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Druid的StatFilter
 *
 * @author   Bat Admin
 */
@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",
    initParams={
        @WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.otc,/druid/*")// 忽略资源
})
public class DruidStatFilter extends WebStatFilter {

}
