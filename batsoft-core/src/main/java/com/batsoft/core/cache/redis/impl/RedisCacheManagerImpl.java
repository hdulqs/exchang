package com.batsoft.core.cache.redis.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.batsoft.core.cache.redis.RedisCacheManager;

/**
 * Spring Redis Cache
 * 
 * @author simon
 */
@Service(value = "redisCacheManager")
public class RedisCacheManagerImpl implements RedisCacheManager {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	@Override
	public boolean verifyKeyExist(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				@SuppressWarnings("unchecked")
				Collection<String> keys = CollectionUtils.arrayToList(key);
				redisTemplate.delete(keys);
			}
		}
	}

	@Override
	public Object get(String key) {
		return key == null ? null : redisTemplate.opsForValue().get(key);
	}

	@Override
	public boolean set(String key, Object value, long seconds) {
		try {
			if (seconds <= 0) {
				return false;
			}else {
				redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, -delta);
	}

	@Override
	public Object hashMapGetValue(String key, String item) {
		return redisTemplate.opsForHash().get(key, item);
	}

	@Override
	public Map<Object, Object> hashMapGet(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	@Override
	public boolean hashMapSet(String key, Map<String, Object> map, long time) {
		try {
			if (time == 0) {
				return false;
			}else {
				redisTemplate.opsForHash().putAll(key, map);
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hashMapSetValue(String key, String item, Object value, long time) {
		try {
			if(time == 0) {
				return false;
			}else {
				redisTemplate.opsForHash().put(key, item, value);
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void hashMapDelValue(String key, Object... item) {
		redisTemplate.opsForHash().delete(key, item);
	}

	@Override
	public boolean hashMapVerValueExist(String key, String item) {
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	@Override
	public double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	@Override
	public double hdecr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	@Override
	public Set<Object> setGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean setVerValueExist(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long setSetValue(String key, long time, Object... values) {
		try {
			if(time == 0 || time < 0) {
				return 0;
			}else {
				Long count = redisTemplate.opsForSet().add(key, values);
				expire(key, time);
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long setGetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long setRemoveValue(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Object> listGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long listGetSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object listGetIndexValue(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean listSet(String key, List<Object> value, long time) {
		try {
			if(time == 0 || time < 0) {
				return false;
			}else {
				redisTemplate.opsForList().rightPushAll(key, value);
				expire(key, time);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean listUpdateIndexValue(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public long listRemoveValue(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
