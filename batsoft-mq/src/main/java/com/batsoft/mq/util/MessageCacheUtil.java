package com.batsoft.mq.util;

import java.util.Map;
import java.util.Optional;

public class MessageCacheUtil {

	public static void add(String cacheName,String key,Object message){
		Map<String,Object> cache= MessageCacheManager.instance().get(cacheName);
    	if(!Optional.ofNullable(cache).isPresent()){
    		MessageCacheManager.instance().add(cacheName);
    		cache=MessageCacheManager.instance().get(cacheName);
    	}
    	cache.put(key, message);
	}
	
	public static void remove(String cacheName,String key){
    	Map<String,Object> cache=MessageCacheManager.instance().get(cacheName);
    	cache.remove(key);
	}
	
}
