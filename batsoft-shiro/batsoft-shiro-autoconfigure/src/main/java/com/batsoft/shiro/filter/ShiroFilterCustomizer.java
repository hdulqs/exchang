package com.batsoft.shiro.filter;

import javax.servlet.Filter;
import java.util.Map;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public interface ShiroFilterCustomizer {

    Map<String, Filter> customize(Map<String, Filter> filterMap);
}
