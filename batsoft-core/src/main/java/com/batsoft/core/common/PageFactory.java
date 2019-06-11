package com.batsoft.core.common;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.io.Serializable;

public class PageFactory<T extends Serializable> {
	
	public static Page getPage(QueryFilter filter){
		Page page = null;
		if(filter.getPageSize().compareTo(Integer.valueOf(-1))==0){
			//pageSize = -1 时  
			//pageHelper传pageSize参数传0查询全部
			page= PageHelper.startPage(filter.getPage(), 0);
		}else{
			page = PageHelper.startPage(filter.getPage(), filter.getPageSize());
		}
		return page;
	}
	
}
