/**
* Copyright:    http://www.batsoft.cn
* @author:      LouSir
* @version:     V1.0
* @Date:        2018-05-30 12:13:29 
*/
package com.batsoft.service.module.log.service.impl;

import com.batsoft.model.module.log.MessageSend;
import com.batsoft.service.module.log.dao.MessageSendDao;
import com.batsoft.service.module.log.service.MessageSendService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> MessageSendServiceImpl </p>
* @author: LouSir
* @Date :  2018-05-30 12:13:29 
*/
@Service("messageSendService")
public class MessageSendServiceImpl extends BaseServiceImpl<MessageSend, String> implements MessageSendService{

@Autowired
private MessageSendDao messageSendDao;


}
