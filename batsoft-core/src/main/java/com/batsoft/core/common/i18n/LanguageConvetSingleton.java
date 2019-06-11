package com.batsoft.core.common.i18n;

import com.batsoft.utils.StringUtils;

/**
 * 语言切换单例
 * 
 * @author simon
 */
public class LanguageConvetSingleton {
	
	private static LanguageConvetSingleton instance = new LanguageConvetSingleton();

	public static LanguageConvetSingleton getInstance() {
		return instance;
	}
	
	private LocaleMessageSourceService messageService;
	
	public void initialize(LocaleMessageSourceService messageService) {
		this.messageService = messageService;
	}
	
	// ---------------------------------------------------------------------
	
	public String message(String code) {
		if (StringUtils.isEmpty(code)) {
            return "code is not find!";
        } else {
            return messageService.getMessage(code);
        }
	}
}
