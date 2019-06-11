/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月4日 上午11:42:30
 */
package com.batsoft.core.cache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.batsoft.core.cache.redis.RedisCacheManager;
import com.batsoft.utils.ObjectUtils;
import com.batsoft.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * <p> TODO</p>
 * @author: Bat Admin
 * @Date :          2017年1月4日 上午11:42:30 
 */

/**
 * 这个类过期，新的实现类详细请转到
 * 
 * @see RedisCacheManager
 * 
 * @author simon
 */
@Slf4j
@Service("redisService")
public class RedisService {
    /**
     * 长期
     */
    public static final Integer CACHE_TIME = 0;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        String value = null;
        try {
            if (stringRedisTemplate.hasKey(key)) {
                value = (String) stringRedisTemplate.opsForValue().get(key);
                log.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("get {} = {}", key, value, e);
        } finally {
        }
        return value;
    }

    /**
     * 是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public Object getObject(String key) {
        Object value = null;
        try {
            if (redisTemplate.hasKey(key)) {
                value = redisTemplate.opsForValue().get(key);
                log.debug("getObject {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("getObject {} = {}", key, value, e);
        } finally {

        }
        return value;
    }


    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时  参考 类 ：ExpireTime.java
     * @return
     */
    public void set(String key, String value, int cacheSeconds) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            if (cacheSeconds != 0) {
                stringRedisTemplate.expire(key, Long.valueOf(cacheSeconds), TimeUnit.SECONDS);
            }
            log.debug("set {} = {}", key, value);
        } catch (Exception e) {
            log.warn("set {} = {}", key, value, e);
        } finally {
        }
    }


    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    public String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        try {
            redisTemplate.opsForValue().set(key, value);
            if (cacheSeconds != 0) {
                redisTemplate.expire(key, Long.valueOf(cacheSeconds), TimeUnit.SECONDS);
            }
            log.debug("setObject {} = {}", key, value);
        } catch (Exception e) {
            log.warn("setObject {} = {}", key, value, e);
        } finally {
        }
        return result;
    }

    /**
     *
     * <p> 删除缓存</p>
     * @author: Bat Admin
     * @param:    @param key
     * @return: void
     * @Date :          2017年1月4日 下午5:47:12
     * @throws:
     */
    public void delRedisByKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("setObject {} = {}", key, e);
        }
    }

    /**
     *
     * <p> 批量删除</p>
     * @author: Bat Admin
     * @param:    @param key
     * @return: void
     * @Date :          2017年1月4日 下午5:47:38
     * @throws:
     */
    public void delRedisByKeys(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
        } catch (Exception e) {
            log.warn("setObject {} = {}", keys, e);
        }
    }


    /**
     * 获取byte[]类型Key
     * @param key
     * @return
     */
    public byte[] getBytesKey(Object object) {
        if (object instanceof String) {
            return StringUtils.getBytes((String) object);
        } else {
            return ObjectUtils.serialize(object);
        }
    }

    /**
     * Object转换byte[]类型
     * @param key
     * @return
     */
    public byte[] toBytes(Object object) {
        return ObjectUtils.serialize(object);
    }

    /**
     * byte[]型转换Object
     * @param key
     * @return
     */
    public Object toObject(byte[] bytes) {
        return ObjectUtils.unserialize(bytes);
    }

    public void setValueWithExpiresForShop(String key,Object value,long seconds){
        redisTemplate.opsForValue().set(key,value,seconds, TimeUnit.SECONDS);
    }

    public Object getShopObject(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
