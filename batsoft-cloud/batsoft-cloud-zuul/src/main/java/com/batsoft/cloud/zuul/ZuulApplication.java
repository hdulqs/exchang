/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2016年12月18日 下午2:12:50
 */
package com.batsoft.cloud.zuul;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2016年12月18日 下午2:12:50 
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
	}
}
