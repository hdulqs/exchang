package com.batsoft.common.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.batsoft.common.util.result.ResultData;
import com.batsoft.core.common.Constants;
import com.batsoft.core.common.enums.KeyEnum;
import com.batsoft.core.common.enums.MessageEnum;
import com.batsoft.core.exception.BusinessIllegalArgumentException;
import com.batsoft.core.exception.BusinessRuntimeException;
import com.batsoft.utils.gson.GsonSingleton;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * 捕捉全局异常
 * 
 * @author simon
 */
@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		try {
			handleBusinessRuntimeException(request, response, handler, e);
		} catch (IOException ioe) {
			// ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * 处理异常
	 * 
	 * @param response
	 * @param handler
	 * @param exception
	 * @throws IOException
	 * @throws Exception
	 */
	private void handleBusinessRuntimeException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws IOException {
		try {
			// 打印异常信息
			// exception.printStackTrace();
			// 模拟继续抛出异常
			throw exception;
		} catch (BusinessRuntimeException e) {
			// 0.本系统业务异常
			buildReturnValue(request, response, handler, e.toMap());
		} catch (BusinessIllegalArgumentException e) {
			// 1.检测到错误的参数传递
			buildReturnValue(request, response, handler, e.toMap());
		} catch (NullPointerException e) {
			// 3.空指针异常
			buildReturnValue(request, response, handler, MessageEnum.NULL_POINTER_EXCEPTION.toMap(e));
		} catch (ConstraintViolationException | DataIntegrityViolationException | MySQLIntegrityConstraintViolationException e) {
			// 4.违反数据约束异常
			buildReturnValue(request, response, handler, MessageEnum.DATA_EXIST_RESTRAIN.toMap());
		} catch (Throwable e) {
			// 5.未知异常
			buildReturnValue(request, response, handler, MessageEnum.UNKNOWN_EXCETION.toMap());
		}
	}

	/**
	 * 抛出结果的通知
	 * 
	 * @param request
	 * 			请求域
	 * @param response
	 * 			响应域
	 * @param handler
	 * 			当前异常方法
	 * @param notification
	 * 			通知内容
	 * @throws IOException
	 */
	protected void buildReturnValue(HttpServletRequest request, HttpServletResponse response, Object handler, Map<String, String> notification) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		ServletOutputStream out = response.getOutputStream(); 
		try {
			GsonSingleton instance = GsonSingleton.getInstance();
			ResultData<Object> result = new ResultData<Object>();
			result.setCode(notification.get(KeyEnum.code.name()));
			result.setMsg(notification.get(KeyEnum.message.name()));
			result.setSuccess(false);
			ResponseBody responseBody = ((HandlerMethod)handler).getMethodAnnotation(ResponseBody.class);
			if(responseBody != null){
				out.write(instance.toJson(result).getBytes(Constants.UTF8));
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
}
