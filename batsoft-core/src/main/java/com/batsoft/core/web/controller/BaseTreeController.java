/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月9日 下午3:08:32
 */
package com.batsoft.core.web.controller;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.service.BaseTreeService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * <p> 树Base</p>
 * @author:         Bat Admin
 * @Date :          2017年1月9日 下午3:08:32 
 */
@Slf4j
public abstract class BaseTreeController<T extends Serializable, PK extends Serializable> extends BaseController<T,PK>{

	/**
	 * 递归获取树路径
	 * @param mparentid
	 * @param parpath
	 * @return
	 */
	protected String getparents(String mparentid, String parpath) {//递归方法，循环调用
		String parentid =((BaseTreeService) super.service).findParentId(mparentid);
		if ("0".equals(parentid)) {
			return "0," + mparentid + "," + parpath;
		} else {
			parpath = mparentid + "," + parpath;
			return (getparents(parentid, parpath));
		}
	}

	/**
	 * 获取列表树==表格树
	 * @return
	 */
	protected JsonResult listTree() {
		JsonResult jsonResult=new JsonResult();
		List list = null;
		try {
			list = ((BaseTreeService) super.service).findListTree();
			jsonResult.setCode(Constants.SUCCESS);
			jsonResult.setData(list);
			jsonResult.setMsg("成功");

		} catch (Exception e) {
			e.printStackTrace();
			log.info("查询失败 ：" + e.getMessage());
			jsonResult.setCode(Constants.FAILED);
			jsonResult.setData(null);
			jsonResult.setMsg("查询失败");
		}
		return jsonResult;
	}

	/**
	 *获取树
	 * @return
	 */
	protected String tree() {
		String treeJson="";
		try {
			treeJson = ((BaseTreeService) super.service).findTreeJson();

		} catch (Exception e) {
			log.info("查询失败 ：" + e.getMessage());
		}
		log.info("tree data ：===="+treeJson);
		return treeJson;
	}
}
