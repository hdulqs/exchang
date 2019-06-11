/**
* Copyright:    http://www.batsoft.cn
* @author:      Bat Admin
* @version:     V1.0
* @Date:        2018-04-14 10:24:31 
*/
package com.batsoft.service.module.cms.service.impl;

import com.batsoft.model.module.cms.Banner;
import com.batsoft.service.module.cms.dao.BannerDao;
import com.batsoft.service.module.cms.service.BannerService;

import com.batsoft.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p> BannerServiceImpl </p>
* @author: Bat Admin
* @Date :  2018-04-14 10:24:31 
*/
@Service("bannerService")
public class BannerServiceImpl extends BaseServiceImpl<Banner, String> implements BannerService{

@Autowired
private BannerDao bannerDao;


}
