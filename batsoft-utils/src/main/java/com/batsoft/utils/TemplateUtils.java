package com.batsoft.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * freemarker 模版转换工具类
 * Created by Administrator on 2017/7/12.
 *
 */
public class TemplateUtils {
    /**
     * 字符串模版
     * @param tpl 模版
     * @param data 数据
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public static String tplToStr(String tpl,Map<String,Object> data) throws TemplateException, IOException {
        StringWriter result = new StringWriter();
        Template t = new Template("tplToStr", new StringReader(tpl), new Configuration());
        t.process(data, result);
        return result.toString();
    }
}
