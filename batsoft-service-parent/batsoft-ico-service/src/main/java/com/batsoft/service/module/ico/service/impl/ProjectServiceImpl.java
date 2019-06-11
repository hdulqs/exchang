/**
* Copyright:    http://www.batsoft.cn
* @author:      Lucl
* @version:     V1.0
* @Date:        2017-08-22 14:13:21 
*/
package com.batsoft.service.module.ico.service.impl;

import com.batsoft.core.service.impl.BaseServiceImpl;
import com.batsoft.model.module.ico.Project;
import com.batsoft.service.module.ico.dao.ProjectDao;
import com.batsoft.service.module.ico.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* <p> ProjectServiceImpl </p>
* @author: 
* @Date :  2017-08-22 14:13:21 
*/
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl<Project, String> implements ProjectService{

	@Autowired
	private ProjectDao projectDao;


}
