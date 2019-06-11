package com.batsoft.core.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.batsoft.core.cache.redis.RedisCacheManager;

public class RedisCacheSignleton {
	
	private RedisCacheSignleton() {}
	
	private static RedisCacheSignleton instance = new RedisCacheSignleton();
	
	public static RedisCacheSignleton getInstance() {
		return instance;
	}
	
	// redis 管理服务
	private RedisCacheManager redisCacheManager;
	
	// 对象默认过期时间 60 * 60 = 3600[1小时]
	private static final long expirationTime = 60 * 60;
	
	// 初始化工作
	public void initialize(RedisCacheManager redisCacheManager) throws RuntimeException {
		// 缓冲服务层
		this.redisCacheManager = redisCacheManager;
	}
	
	/**
	 * 判断key是否存在
	 * 
	 * @param key
	 *            键
	 * @return true 存在 false不存在
	 */
	public boolean verifyKeyExist(String key) {
		return redisCacheManager.verifyKeyExist(key);
	}
	
	/**
	 * 设置缓存失效时间
	 * 
	 * @param key
	 *            键
	 * @param time
	 *            时间(秒)
	 * @return
	 */
	public boolean expire(String key, long time) {
		return redisCacheManager.expire(key, time);
	}
	
	/**
	 * 普通缓存放入
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return true成功 false失败
	 */
	public boolean set(String key, Object value) {
		return redisCacheManager.set(key, value, expirationTime);
	}
	
	/**
	 * 普通缓存放入并设置时间
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param second
	 *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long second) {
		return redisCacheManager.set(key, value, second);
	}
	
	/**
	 * 普通缓存获取
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public <R> R get(String key) {
		return (R) redisCacheManager.get(key);
	}
	
	/**
	 * 删除缓存
	 * 
	 * @param key
	 *           可以传一个值 或多个
	 */
	public void del(String... key) {
		redisCacheManager.del(key);
	}
	
	/**
	 * HashMapset 设置Map集合到缓存中
	 * 
	 * @param key
	 *            键
	 * @param map
	 *            对应多个键值
	 *            
	 * @return true 成功 false 失败
	 */
	public boolean hashMapSet(String key, Map<String, Object> map) {
		return redisCacheManager.hashMapSet(key, map, expirationTime);
	}
	
	/**
	 * HashMapset 设置Map集合到缓存中并设置时间
	 * 
	 * @param key
	 *            键
	 * @param map
	 *            对应多个键值
	 * @param second
	 *            时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hashMapSet(String key, Map<String, Object> map, long second) {
		return redisCacheManager.hashMapSet(key, map, second);
	}
	
	/**
	 * HashMapsetValue 向一个HashMap中放入数据,如果不存在将创建
	 * 
	 * @param key
	 *            键
	 * @param item
	 *            项
	 * @param value
	 *            值
	 * @return true 成功 false失败
	 */
	public boolean hashMapSetValue(String key, String item, Object value) {
		return redisCacheManager.hashMapSetValue(key, item, value, expirationTime);
	}
	
	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key
	 *            键
	 * @param item
	 *            项
	 * @param value
	 *            值
	 * @param second
	 *            时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hashMapSetValue(String key, String item, Object value, long second) {
		return redisCacheManager.hashMapSetValue(key, item, value, second);
	}
	
	/**
	 * 获取HashMap对象
	 * 
	 * @param key
	 *            键
	 * @return HashMap
	 */
	public Map<Object, Object> hashMapGet(String key){
		return redisCacheManager.hashMapGet(key);
	}
	
	/**
	 * HashMapgetValue
	 * 
	 * @param key
	 *            键
	 * @param item
	 *            项
	 * @return 值
	 */
	public Object hashMapGetValue(String key, String item) {
		return redisCacheManager.hashMapGetValue(key, item);
	}
	
	/**
	 * 删除hash表中的值
	 * 
	 * @param key
	 *            键
	 * @param item
	 *            项
	 */
	public void hashMapDelValue(String key, Object... item) {
		redisCacheManager.hashMapDelValue(key, item);
	}
	
	/**
	 * 判断hash表中是否有该项的值
	 * 
	 * @param key
	 *            键
	 * @param item
	 *            项
	 * @return true 存在 false不存在
	 */
	public boolean hashMapVerValueExist(String key, String item) {
		return redisCacheManager.hashMapVerValueExist(key, item);
	}
	
	/**
	 * 将数据放入set缓存
	 * 
	 * @param key
	 *            键
	 * @param values
	 *            值 可以是多个
	 * @return 成功个数
	 */
	public long setSetValue(String key, Object... values) {
		return redisCacheManager.setSetValue(key, expirationTime, values);
	}
	
	/**
	 * 将set数据放入缓存
	 * 
	 * @param key
	 *            键
	 * @param second
	 *            时间(秒)
	 * @param values
	 *            值 可以是多个
	 * @return 成功个数
	 */
	public long setSetValue(String key, long second, Object... values) {
		return redisCacheManager.setSetValue(key, second, values);
	}
	
	/**
	 * 根据key获取Set中的所有值
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public Set<Object> setGet(String key) {
		return redisCacheManager.setGet(key);
	}
	
	/**
	 * 根据value从一个set中查询,是否存在
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return true 存在 false不存在
	 */
	public boolean setVerValueExist(String key, Object value) {
		return redisCacheManager.setVerValueExist(key, value);
	}
	
	/**
	 * 获取set缓存的长度
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public long setGetSize(String key) {
		return redisCacheManager.setGetSize(key);
	}
	
	/**
	 * 移除值为value的
	 * 
	 * @param key
	 *            键
	 * @param values
	 *            值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemoveValue(String key, Object... values) {
		return redisCacheManager.setRemoveValue(key, values);
	}
	
	/**
	 * 将list放入缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public boolean listSet(String key, List<Object> value) {
		return redisCacheManager.listSet(key, value, expirationTime);
	}
	
	/**
	 * 将list放入缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param second
	 *            时间(秒)
	 * @return
	 */
	public boolean listSet(String key, List<Object> value, long second) {
		return redisCacheManager.listSet(key, value, second);
	}
	
	/**
	 * 获取list缓存的内容
	 * 
	 * @param key
	 *            键
	 * @param start
	 *            开始
	 * @param end
	 *            结束 0 到 -1代表所有值
	 * @return
	 */
	public List<Object> listGet(String key, long start, long end){
		return redisCacheManager.listGet(key, start, end);
	}
	
	/**
	 * 获取list缓存的长度
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public long listGetSize(String key) {
		return redisCacheManager.listGetSize(key);
	}
	
	/**
	 * 通过索引 获取list中的值
	 * 
	 * @param key
	 *            键
	 * @param index
	 *            索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object listGetIndexValue(String key, long index) {
		return redisCacheManager.listGetIndexValue(key, index);
	}
	
	/**
	 * 根据索引修改list中的某条数据
	 * 
	 * @param key
	 *            键
	 * @param index
	 *            索引
	 * @param value
	 *            值
	 * @return
	 */
	public boolean listUpdateIndexValue(String key, long index, Object value) {
		return redisCacheManager.listUpdateIndexValue(key, index, value);
	}
	
	/**
	 * 移除N个值为value
	 * 
	 * @param key
	 *            键
	 * @param count
	 *            移除多少个
	 * @param value
	 *            值
	 * @return 移除的个数
	 */
	public long listRemoveValue(String key, long count, Object value) {
		return redisCacheManager.listRemoveValue(key, count, value);
	}
	
}
