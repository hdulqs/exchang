package com.batsoft.mq.util;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class MessageCacheManager {

	private static class CacheManagerSingleton{
		private static final Map<String,Map<String,Object>> cache=new ConcurrentHashMap<String,Map<String,Object>>();
		private static final MessageCacheManager instance=new MessageCacheManager();
	}
	
	public static MessageCacheManager instance(){
		return CacheManagerSingleton.instance;	
	}
	
	public Map<String,Map<String,Object>> getAll(){
		return CacheManagerSingleton.cache;
	}
	
	public Map<String,Object> get(String name){
		return CacheManagerSingleton.cache.get(name);
	}
	
	public boolean add(String name){
		if(Optional.ofNullable(get(name)).isPresent())
			return false;
		else{
			Map<String,Object> cache=new ConcurrentHashMap<String,Object>();
			CacheManagerSingleton.cache.put(name, cache);
		}
		return true;
	}
	
	public boolean add(String name,Map<String,Object> cache){
		if(Optional.ofNullable(get(name)).isPresent())
			return false;
		else{
			CacheManagerSingleton.cache.put(name, cache);
		}
		return true;
	}
	
	public boolean remove(String name){
		if(Optional.ofNullable(get(name)).isPresent())
			return false;
		else{
			CacheManagerSingleton.cache.remove(name);
		}
		return true;
	}
	
}
