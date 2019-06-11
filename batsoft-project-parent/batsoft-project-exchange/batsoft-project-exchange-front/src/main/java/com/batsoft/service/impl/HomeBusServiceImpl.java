package com.batsoft.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.batsoft.service.HomeBusService;
import com.batsoft.service.module.log.service.MemberLoginService;
import com.batsoft.service.module.member.service.UserService;

@Service(value = "homeBusService")
public class HomeBusServiceImpl implements HomeBusService {
	
	@Resource
	private UserService userService;
	
	@Resource
	private MemberLoginService memberLoginService;

	
}
