package com.batsoft.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
@ConfigurationProperties(prefix = "shiro.cookie.sessionIdCookie")
@Data
public class ShiroCookieIdProperties {

    private String name = "admin";

    private String value;

    private int maxAge = -1; //

    private int version = -1;

    private boolean secure;

    private boolean httpOnly = false;

    }
