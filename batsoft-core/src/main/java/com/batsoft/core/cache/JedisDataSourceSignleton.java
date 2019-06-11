package com.batsoft.core.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * jedis client signleton
 * 
 * @author simon
 */
public final class JedisDataSourceSignleton {
	
	private JedisDataSourceSignleton() {}
	
	private static JedisDataSourceSignleton instance = new JedisDataSourceSignleton();
	
	public static JedisDataSourceSignleton getInstance() {
		return instance;
	}
	
	// DB0
	public static final Integer DB0 = 0;
	
	// DB1
	public static final Integer DB1 = 1;
	

	// JedisPool 连接池
	private JedisPool jedisPool;
	
	// 初始化工作
	public void initialize(JedisPool jedisPool) {
		// jedis pool
		this.jedisPool = jedisPool;
	}
	
	/**
	 * 获取Jedis客户端
	 * 
	 * @return
	 */
	public Jedis getJedis() {
        Jedis jedis = null;
        int timeoutCount = 0;
        while (true) {
            try {
            	jedis = jedisPool.getResource();
            	if(jedis != null) {
                	return jedis;
                }
            } catch (Exception e) {
                if (e instanceof JedisConnectionException) {
                    timeoutCount++; // 重复尝试获取三次
                    if (timeoutCount > 5) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return jedis;
    }
	
	/**
	 * 指定数据库获取Jedis数据库
	 * 
	 * @param index
	 * 			数据库标识
	 * @return
	 */
	public Jedis getJedis(int index) {
		Jedis jedis = getJedis();
		jedis.select(index);
		return jedis;
	}
	

    /**
     * 释放Jedis客户端
     * 
     * @param jedis
     */
	public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
	
    
    // ---------------------------------------------------------------------------------------------------------------------
    
    /**
     * 获取指定key的值,如果key不存在返回null，如果该Key存储的不是字符串，会抛出一个错误
     *
     * @param key
     * @return
     */
    public String get(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
			return jedis.get(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 设置key的值为value
     *
     * @param key
     * @param value
     * @return
     */
    public String set(int dbIndex, String key, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.set(key, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 删除指定的key,也可以传入一个包含key的数组
     *
     * @param keys
     * @return
     */
    public Long del(int dbIndex, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.del(keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key向指定的value值追加值
     *
     * @param key
     * @param str
     * @return
     */
    public Long append(int dbIndex, String key, String str) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.append(key, str);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.exists(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 设置key value,如果key已经存在则返回0
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(int dbIndex, String key, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.setnx(key, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 设置key value并指定这个键值的有效期
     *
     * @param key
     * 			数据键
     * @param value
     * 			数据值
     * @param seconds
     * 			过期时间
     * @return
     */
    public String setex(int dbIndex, String key, String value, int seconds) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.setex(key, seconds, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key 和offset 从指定的位置开始将原先value替换
     *
     * @param key
     * @param offset
     * @param str
     * @return
     */
    public Long setrange(int dbIndex, String key, int offset, String str) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.setrange(key, offset, str);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过批量的key获取批量的value
     *
     * @param keys
     * @return
     */
    public List<String> mget(int dbIndex, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.mget(keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 批量的设置key:value,也可以一个
     *
     * @param keysValues
     * @return
     */
    public String mset(int dbIndex, String... keysValues) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.mset(keysValues);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚
     *
     * @param keysValues
     * @return
     */
    public Long msetnx(int dbIndex, String... keysValues) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.msetnx(keysValues);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 设置key的值,并返回一个旧值
     *
     * @param key
     * @param value
     * @return
     */
    public String getSet(int dbIndex, String key, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.getSet(key, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过下标 和key 获取指定下标位置的 value
     *
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public String getrange(int dbIndex, String key, int startOffset, int endOffset) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.getrange(key, startOffset, endOffset);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     *
     * @param key
     * @return
     */
    public Long incr(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.incr(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key给指定的value加值,如果key不存在,则这是value为该值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(int dbIndex, String key, long integer) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.incrBy(key, integer);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     *
     * @param key
     * @return
     */
    public Long decr(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.decr(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 减去指定的值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(int dbIndex, String key, long integer) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.decrBy(key, integer);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取value值的长度
     *
     * @param key
     * @return
     */
    public Long strLen(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.strlen(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hsetnx(int dbIndex, String key, String field, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hsetnx(key, field, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key给field设置指定的值,如果key不存在,则先创建
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(int dbIndex, String key, String field, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hset(key, field, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key同时设置 hash的多个field
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(int dbIndex, String key, Map<String, String> hash) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hmset(key, hash);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key 和 field 获取指定的 value
     *
     * @param key
     * @param failed
     * @return
     */
    public String hget(int dbIndex, String key, String failed) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hget(key, failed);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 设置key的超时时间为seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(int dbIndex, String key, int seconds) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.expire(key, seconds);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
     *
     * @param key
     * @param fields 可以是 一个String 也可以是 String数组
     * @return
     */
    public List<String> hmget(int dbIndex, String key, String... fields) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hmget(key, fields);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key给指定的field的value加上给定的值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrby(int dbIndex, String key, String field, Long value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hincrBy(key, field, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key和field判断是否有指定的value存在
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(int dbIndex, String key, String field) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hexists(key, field);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回field的数量
     *
     * @param key
     * @return
     */
    public Long hlen(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hlen(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key 删除指定的 field
     *
     * @param key
     * @param fields 可以是 一个 field 也可以是 一个数组
     * @return
     */
    public Long hdel(int dbIndex, String key, String... fields) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hdel(key, fields);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回所有的field
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hkeys(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回所有和key有关的value
     *
     * @param key
     * @return
     */
    public List<String> hvals(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hvals(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取所有的field和value
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetall(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.hgetAll(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key向list头部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long lpush(int dbIndex, String key, String... strs) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lpush(key, strs);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key向list尾部添加字符串
     *
     * @param key
     * @param strs 可以是一个string 也可以是string数组
     * @return 返回list的value个数
     */
    public Long rpush(int dbIndex, String key, String... strs) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.rpush(key, strs);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key在list指定的位置之前或者之后 添加字符串元素
     *
     * @param key
     * @param where LIST_POSITION枚举类型
     * @param pivot list里面的value
     * @param value 添加的value
     * @return
     */
    public Long linsert(int dbIndex, String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.linsert(key, where, pivot, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key设置list指定下标位置的value
     * 如果下标超过list里面value的个数则报错
     *
     * @param key
     * @param index 从0开始
     * @param value
     * @return 成功返回OK
     */
    public String lset(int dbIndex, String key, long index, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lset(key, index, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key从对应的list中删除指定的count个 和 value相同的元素
     *
     * @param key
     * @param count 当count为0时删除全部
     * @param value
     * @return 返回被删除的个数
     */
    public Long lrem(int dbIndex, String key, long count, String value) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lrem(key, count, value);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key保留list中从strat下标开始到end下标结束的value值
     *
     * @param key
     * @param start
     * @param end
     * @return 成功返回OK
     */
    public String ltrim(int dbIndex, String key, long start, long end) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.ltrim(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key从list的头部删除一个value,并返回该value
     *
     * @param key
     * @return
     */
    public synchronized String lpop(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lpop(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key从list尾部删除一个value,并返回该元素
     *
     * @param key
     * @return
     */
    public synchronized String rpop(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.rpop(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
     * 如果第一个list为空或者不存在则返回null
     *
     * @param srckey
     * @param dstkey
     * @return
     */
    public String rpoplpush(int dbIndex, String srckey, String dstkey) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.rpoplpush(srckey, dstkey);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取list中指定下标位置的value
     *
     * @param key
     * @param index
     * 			下标充从0开始
     * @return 如果没有返回null
     */
    public String lindex(int dbIndex, String key, long index) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lindex(key, index);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回list的长度
     *
     * @param key
     * @return
     */
    public Long llen(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.llen(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取list指定下标位置的value
     * 如果start 为 0 end 为 -1 则返回全部的list中的value
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(int dbIndex, String key, long start, long end) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.lrange(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key向指定的set中添加value
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 添加成功的个数
     */
    public Long sadd(int dbIndex, String key, String... members) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sadd(key, members);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key删除set中对应的value值
     *
     * @param key
     * @param members 可以是一个String 也可以是一个String数组
     * @return 删除的个数
     */
    public Long srem(int dbIndex, String key, String... members) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.srem(key, members);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key随机删除一个set中的value并返回该值
     *
     * @param key
     * @return
     */
    public String spop(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.spop(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取set中的差集
     * 以第一个set为标准
     *
     * @param keys 可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Set<String> sdiff(int dbIndex, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sdiff(keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取set中的差集并存入到另一个key中
     * 以第一个set为标准
     *
     * @param dstkey 差集存入的key
     * @param keys   可以 是一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public Long sdiffstore(int dbIndex, String dstkey, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sdiffstore(dstkey, keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取指定set中的交集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sinter(int dbIndex, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sinter(keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取指定set中的交集 并将结果存入新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long sinterstore(int dbIndex, String dstkey, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sinterstore(dstkey, keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回所有set的并集
     *
     * @param keys 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Set<String> sunion(int dbIndex, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sunion(keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回所有set的并集,并存入到新的set中
     *
     * @param dstkey
     * @param keys   可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long sunionstore(int dbIndex, String dstkey, String... keys) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sunionstore(dstkey, keys);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key将set中的value移除并添加到第二个set中
     *
     * @param srckey 需要移除的
     * @param dstkey 添加的
     * @param member set中的value
     * @return
     */
    public Long smove(int dbIndex, String srckey, String dstkey, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.smove(srckey, dstkey, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取set中value的个数
     *
     * @param key
     * @return
     */
    public Long scard(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.scard(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key判断value是否是set中的元素
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(int dbIndex, String key, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.sismember(key, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取set中随机的value,不删除元素
     *
     * @param key
     * @return
     */
    public String srandmember(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.srandmember(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取set中所有的value
     *
     * @param key
     * @return
     */
    public Set<String> smembers(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.smembers(key);
		} finally {
			this.close(jedis);
		}
    }


    /**
     * 通过key向zset中添加value,score,其中score就是用来排序的
     * 如果该value已经存在则根据score更新元素
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(int dbIndex, String key, double score, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zadd(key, score, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key删除在zset中指定的value
     *
     * @param key
     * @param members 可以 是一个string 也可以是一个string数组
     * @return
     */
    public Long zrem(int dbIndex, String key, String... members) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrem(key, members);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key增加该zset中value的score的值
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Double zincrby(int dbIndex, String key, double score, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zincrby(key, score, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从小到大排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrank(int dbIndex, String key, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrank(key, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回zset中value的排名
     * 下标从大到小排序
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrevrank(int dbIndex, String key, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrevrank(key, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key将获取score从start到end中zset的value
     * socre从大到小排序
     * 当start为0 end为-1时返回全部
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(int dbIndex, String key, long start, long end) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrevrange(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangebyscore(int dbIndex, String key, String max, String min) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrevrangeByScore(key, max, min);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回指定score内zset中的value
     *
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrangeByScore(int dbIndex, String key, double max, double min) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zrevrangeByScore(key, max, min);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 返回指定区间内zset中value的数量
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(int dbIndex, String key, double min, double max) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zcount(key, min, max);
		} finally {
			this.close(jedis);
		}
    }
    
    /**
     * 根据指定的下标获取指定键中的数据集合
     * 
     * @param key
     * 			键位
     * @param start
     * 			开始下标
     * @param end 
     * 			结束下标
     * @return
     */
    public Set<String> zrange(int dbIndex, String key, long start, long end){
    	Jedis jedis = getJedis(dbIndex);
    	try {
    		return jedis.zrange(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key返回zset中的value个数
     *
     * @param key
     * @return
     */
    public Long zcard(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zcard(key);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key获取zset中value的score值
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(int dbIndex, String key, String member) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zscore(key, member);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key删除给定区间内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(int dbIndex, String key, long start, long end) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zremrangeByRank(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过key删除指定score内的元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByScore(int dbIndex, String key, double start, double end) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.zremrangeByScore(key, start, end);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 返回满足pattern表达式的所有key
     * keys(*)
     * 返回所有的key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(int dbIndex, String pattern) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.keys(pattern);
		} finally {
			this.close(jedis);
		}
    }

    /**
     * 通过Key判断值得类型
     * 
     * @param key
     * 			数据的Key
     * @return
     */
    public String type(int dbIndex, String key) {
        Jedis jedis = getJedis(dbIndex);
        try {
        	return jedis.type(key);
		} finally {
			this.close(jedis);
		}
    }
    

    
}
