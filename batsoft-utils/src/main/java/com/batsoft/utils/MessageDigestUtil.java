package com.batsoft.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 【MD5】消息摘要
 * 
 * @author simon
 */
public class MessageDigestUtil {
	
	/**
	 * MD5消息摘要
	 * 
	 * @param content
	 * @return
	 */
	public static final String md5(String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		return DigestUtils.md5Hex(content);
	}

	/**
	 * SHA-256消息摘要
	 * 
	 * @param content
	 * @return
	 */
	public static final String sha256(String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		return DigestUtils.sha256Hex(content);
	}

	/**
	 * SHA-512消息摘要
	 * 
	 * @param content
	 * @return
	 */
	public static final String sha512(String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		return DigestUtils.sha512Hex(content);
	}
	
}
