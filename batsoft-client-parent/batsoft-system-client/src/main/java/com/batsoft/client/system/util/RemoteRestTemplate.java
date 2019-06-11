package com.batsoft.client.system.util;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
public class RemoteRestTemplate {

	@Bean(name="remoteRestTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
