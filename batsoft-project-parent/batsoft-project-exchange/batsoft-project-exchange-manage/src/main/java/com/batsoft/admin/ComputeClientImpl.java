package com.batsoft.admin;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
@Component
public class ComputeClientImpl implements ComputeClient {

	/* (non-Javadoc)
	 * @see com.batsoft.admin.ComputeClient#add(java.lang.Integer, java.lang.Integer)
	 */
	 @Override
	    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
	        return -9999;
	    }

   }