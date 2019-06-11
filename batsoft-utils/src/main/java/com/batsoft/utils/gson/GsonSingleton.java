package com.batsoft.utils.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;


/**
 * Gson工具类
 * 
 * @author simon
 */
public class GsonSingleton {
	
	private GsonSingleton() {};
	
	private Gson gson = new Gson();
	
	private static GsonSingleton instance = new GsonSingleton();

	public static GsonSingleton getInstance() {
		return instance;
	}
	
	/**
	 * 将对象转换成Json数据
	 * 
	 * @param source
	 * @return
	 */
	public String toJson(Object source) {
		return gson.toJson(source);
	}

	/**
	 * JSON字符串转换成对象
	 * 
	 * @param json
	 *            josn数据
	 * @param targetClazz
	 * 			  目标类类型
	 * @return object 对象
	 */
	public <T> T fromJson(String json, Class<T> targetClazz) {
		return gson.fromJson(json, targetClazz);
	}

	/**
	 * JSON字符创转换成对象
	 * 
	 * @param json
	 *            josn数据
	 * @param targetType
	 *			   目标类型
	 * @return 
	 */
	public <T> T fromJson(String json, Type targetType) {
		return gson.fromJson(json, targetType);
	}
	
	/**
	 *  将对象转换成Json数据
	 *  
	 * @param source
	 * 			将被转换对象
	 * @param proxy
	 * 			指定使用模式
	 * @return
	 */
	public String toJson(Object source, FieldNamingPolicy proxy) {
		Gson mgson = new GsonBuilder().setFieldNamingPolicy(proxy).create();
		return mgson.toJson(source);
	}

	/**
	 * Json字符串转换成对象
	 * 
	 * @param json
	 * 			Json字符串
	 * @param targetClazz
	 * 			目标类类型
	 * @param proxy
	 * 			指定使用的模式
	 * @return
	 */
	public <T> T fromJson(String json, Class<T> targetClazz, FieldNamingPolicy proxy) {
		Gson mgson = new GsonBuilder().setFieldNamingPolicy(proxy).create();
		return mgson.fromJson(json, targetClazz);
	}

	/**
	 * JSON字符创转换成对象
	 * 
	 * @param json
	 * 			Json字符串
	 * @param targetType
	 * 			目标Type
	 * @param proxy
	 * 			指定使用的模式
	 * @return
	 */
	public <T> T fromJson(String json, Type targetType, FieldNamingPolicy proxy) {
		Gson mgson = new GsonBuilder().setFieldNamingPolicy(proxy).create();
		return mgson.fromJson(json, targetType);
	}
}
