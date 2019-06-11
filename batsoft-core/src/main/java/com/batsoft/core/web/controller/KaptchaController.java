/**
 * Copyright:   www.batsoft.cn
 *
 * @author: Bat Admin
 * @version: V1.0
 * @Date: 2017年1月10日 下午3:47:04
 */
package com.batsoft.core.web.controller;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.cache.RedisService;
import com.batsoft.utils.CookieUtil;
import com.batsoft.utils.IdGen;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * <p> 图形验证码</p>
 * <p>http://localhost/validCode </p>
 * @author: Bat Admin
 * @Date :          2017年1月10日 下午3:47:04
 */
@Controller("kaptcha")
@RequestMapping("/")
public class KaptchaController {
    /**
     * 验证码超时时间 默认 30秒
     */
    public static final int TIMEOUT=60;
    @Autowired
    DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisService redisService;
    @RequestMapping("/validCode")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = defaultKaptcha.createText();
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        try {
            String uuid = IdGen.uuid().trim().toString();
            redisService.set(ApplicationConfigure.validCode + "_" + uuid, capText, TIMEOUT);
            CookieUtil.addCookie(response, ApplicationConfigure.validCode, uuid, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bi = defaultKaptcha.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }

    }
}