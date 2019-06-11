/**
 * Copyright:   www.batsoft.cn 
 * @author:      Bat Admin
 * @version:      V1.0 
 * @Date:        2017年2月3日 下午5:28:32
 */
package com.batsoft.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * <p> TODO</p>
 * @author:         Bat Admin
 * @Date :          2017年2月3日 下午5:28:32 
 */
@ControllerAdvice
public class ResourceUrlProviderController {

    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }
}
