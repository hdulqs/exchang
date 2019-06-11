/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月9日 下午3:08:32
 */
package com.batsoft.core.web.controller;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.batsoft.core.common.Constants;
import com.batsoft.core.common.JsonResult;
import com.batsoft.core.common.i18n.LocaleMessageSourceService;
import com.batsoft.core.common.validator.BeanValidators;
import com.batsoft.utils.DateUtils;
import com.batsoft.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p> 常规通用类</p>
 * @author:         Bat Admin
 * @Date :          2017年1月9日 下午3:08:32 
 */
@Slf4j
public abstract class GenericController {
	protected static final String VALID_TYPE_GOOGLE="google";
	protected static final String VALID_TYPE_MOBILE="mobile";
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	protected JsonResult baseResult=null;

	public GenericController(){
		baseResult=new JsonResult();
	}
	
	@Autowired
	private LocaleMessageSourceService localeMessageSourceService;

	public Integer start = null;
	public Integer end = null;


	/**
	 * 服务端参数有效性验证,返回model
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidatorForModel(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	
	/**
	 * 服务端参数有效性验证,返回json
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidatorForJson(Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> errList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			List<String> msgList=new ArrayList<>();
			msgList.addAll(errList);
//			msgList.add("数据校验错误");
			addJsonMessage( errList,msgList.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证,返回map
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidatorForMap(Map<String, String> map, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMapMessage(map, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	
	/**
	 * 添加Model消息
	 * @param messages
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("msg", sb.toString());
	}
	
	/**
	 * 添加Json消息
	 * @param messages
	 */
	protected void addJsonMessage(Object obj,String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"":"");
		}
		showErrorJson(sb.toString(),obj);
	}
	
	/**
	 * 添加map消息
	 * @param messages
	 */
	protected void addMapMessage(Map<String, String> map, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		map.put("result", "0");
		map.put("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
	 * @param messages
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 返回成功的json串
	 * @param message
	 */
	protected JsonResult showSuccessJson(String message,Object obj){
		baseResult.setCode(Constants.SUCCESS);
		baseResult.setSuccess(true);
		baseResult.setData(obj);
		if(StringUtils.isEmpty(message)){
			baseResult.setMsg("");
		}else{
			baseResult.setMsg(message);
		}
		return baseResult;
	}
	
	/**
	 * 返回失败的json串
	 * @param message
	 */
	protected JsonResult showErrorJson(String message,Object obj){
		baseResult.setCode(Constants.FAILED);
		baseResult.setSuccess(false);
		baseResult.setData(obj);
		if(StringUtils.isEmpty(message)){
			baseResult.setMsg("");
		}else{
			baseResult.setMsg(message);
		}
		return baseResult;
	}
	
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(Timestamp.valueOf(text));
			}
		});
	}

	/**
	 * 根据传的页数设置limit
	 * @param page
	 * @param size
	 */
	public void mergePage(Integer page,Integer size){
		if(page!=null&&size!=null){
			start = (page-1)*size;
			end = size;
		}else{
			start = null;
			end = null;
		}
	}
	
	/**
	 * 设置值到Session域
	 * 
	 * @param request
	 */
	protected void setSessionValue(HttpServletRequest request, String key, Object value) {

		request.getSession().setAttribute(key, value);
	}

	/**
	 * 获取Session域值
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <R> R getSessionValue(HttpServletRequest request, String key) {

		return (R) request.getSession().getAttribute(key);
	}
	
	/**
	 * 移除会话中的值
	 * 
	 * @param key
	 */
	protected void removeSessionValue(HttpServletRequest request,String key){
		
		request.getSession().removeAttribute(key);
	}
	
	
}
