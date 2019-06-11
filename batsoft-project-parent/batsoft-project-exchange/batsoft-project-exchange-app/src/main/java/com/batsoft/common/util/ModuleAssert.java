package com.batsoft.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.batsoft.common.enums.ModuleMessageEnum;
import com.batsoft.core.common.enums.CHS;
import com.batsoft.exception.ModuleBusinessIllegalArgumentException;

/**
 * 断言
 * 
 * @author simon
 */
public class ModuleAssert {
	
	/**
	 * 断言结果为真
	 * 
	 * @param expression
	 * @param message
	 */
	public static void state(boolean expression, ModuleMessageEnum message) {
		if (!expression) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言结果为真
	 * 
	 * @param expression
	 * @param message
	 */
	public static void isTrue(boolean expression, ModuleMessageEnum message) {
		if (!expression) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言对象为空
	 * 
	 * @param object
	 * @param message
	 */
	public static void isNull(Object object, ModuleMessageEnum message) {
		if (object != null) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言对象不为空
	 * 
	 * @param object
	 * @param message
	 */
	public static void notNull(Object object, ModuleMessageEnum message) {
		if (object == null) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言字符串有长度
	 * 
	 * @param text
	 * @param message
	 */
	public static void hasLength(String text, ModuleMessageEnum message) {
		if (!StringUtils.hasLength(text)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言文本有值
	 * 
	 * @param text
	 * @param message
	 */
	public static void hasText(String text, ModuleMessageEnum message) {
		if (!StringUtils.hasText(text)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言两个值相等
	 * 
	 * @param textToSearch
	 * @param substring
	 * @param message
	 */
	public static void doesNotContain(String textToSearch, String substring, ModuleMessageEnum message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言数组对象不为空
	 * 
	 * @param array
	 * @param message
	 */
	public static void notEmpty(Object[] array, ModuleMessageEnum message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言数组对象每一个节点都不为空
	 * 
	 * @param array
	 * @param message
	 */
	public static void noNullElements(Object[] array, ModuleMessageEnum message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new ModuleBusinessIllegalArgumentException(message);
				}
			}
		}
	}

	/**
	 * 断言迭代对象不为空
	 * 
	 * @param collection
	 * @param message
	 */
	public static void notEmpty(Collection<?> collection, ModuleMessageEnum message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言集合对象不为空
	 * 
	 * @param map
	 * @param message
	 */
	public static void notEmpty(Map<?, ?> map, ModuleMessageEnum message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}

	/**
	 * 断言内容符合正则要求
	 * 
	 * @param content
	 *            需要校验的文本
	 * @param pperent
	 *            正则表达式
	 * @param message
	 *            断言成功抛出的提示信息
	 */
	public static void isPattern(String content, String regex, ModuleMessageEnum message) {
		String SUFFIX_REGEX = "(/.)|(/)"; // 清除正则后缀
		if (regex.indexOf(CHS.left_slash.getValue()) == 0) {
			regex = regex.substring(1, regex.length()).replaceAll(SUFFIX_REGEX, "");
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (!matcher.find()) {
			throw new ModuleBusinessIllegalArgumentException(message);
		}
	}
	
}
