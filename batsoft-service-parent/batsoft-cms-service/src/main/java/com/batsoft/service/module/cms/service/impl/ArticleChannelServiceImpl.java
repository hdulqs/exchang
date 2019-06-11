/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-19 10:21:01 
*/
package com.batsoft.service.module.cms.service.impl;

import com.batsoft.model.module.cms.ArticleChannel;
import com.batsoft.service.module.cms.dao.ArticleChannelDao;
import com.batsoft.service.module.cms.service.ArticleChannelService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> ArticleChannelServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-19 10:21:01 
*/
@Service("articleChannelService")
public class ArticleChannelServiceImpl extends BaseServiceImpl<ArticleChannel, String> implements ArticleChannelService{

@Autowired
private ArticleChannelDao articleChannelDao;


}
