/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年1月4日 上午11:33:28
 */
package com.batsoft.core.cache;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年1月4日 上午11:33:28 
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {
	  private Converter<Object, byte[]> serializer = new SerializingConverter();
	  private Converter<byte[], Object> deserializer = new DeserializingConverter();
	  static final byte[] EMPTY_ARRAY = new byte[0];
	  @Override
          public Object deserialize(byte[] bytes) {
	    if (isEmpty(bytes)) {
	      return null;
	    }
	    try {
	      return deserializer.convert(bytes);
	    } catch (Exception ex) {
	      throw new SerializationException("Cannot deserialize", ex);
	    }
	  }
	  @Override
          public byte[] serialize(Object object) {
	    if (object == null) {
	      return EMPTY_ARRAY;
	    }
	    try {
	      return serializer.convert(object);
	    } catch (Exception ex) {
	      return EMPTY_ARRAY;
	    }
	  }
	  private boolean isEmpty(byte[] data) {
	    return (data == null || data.length == 0);
	  }
	}
