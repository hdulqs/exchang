package com.batsoft.client.system.util;

import com.batsoft.core.common.JsonResult;
import com.batsoft.model.module.member.User;

import java.util.Map;

/**
 * @author Administrator
 */
public class CommonUtil {

	public static JsonResult chkUserExist(User user, String errMsg, String errCode){
		JsonResult result = new JsonResult();
		if (null == user){
			result.setSuccess(Boolean.FALSE);
			result.setMsg(errMsg);
			result.setCode(JsonResult.ResultCode.FAILE);
		}
		return result;
	}

	public static JsonResult chkMapNotNull(Map map, String errMsg, String errCode){
		JsonResult result = new JsonResult();
		if (null == map){
			result.setSuccess(Boolean.FALSE);
			result.setMsg(errMsg);
			result.setCode(JsonResult.ResultCode.FAILE);
		}
		return result;
	}
}
