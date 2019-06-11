package com.batsoft.third.module.sms.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.batsoft.core.common.JsonResult;
import com.batsoft.model.module.system.config.AppConfig;
import com.batsoft.service.module.system.service.config.AppConfigService;
import com.batsoft.third.module.sms.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 */
@Service("juHeSendServiceImpl")
public class JuHeSendServiceImpl implements SendService {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    @Autowired
    private AppConfigService appConfigService;
    @Override
    public JsonResult send(String areaCode,String mobile,Map<String,Object> data,String tplCode){

        JsonResult result = new JsonResult();
        String url = appConfigService.findValueByKey(AppConfig.SMSHOST);//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("mobile",mobile);//接收短信的手机号码
        params.put("tpl_id",tplCode);//短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value",urlencode(data));//变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
        params.put("key",appConfigService.findValueByKey(AppConfig.SMSAPPKEY));//应用APPKEY(应用详细页查询)
        params.put("dtype","");//返回数据的格式,xml或json，默认json

        try {
            String ret =net(url, params, "GET");
            JSONObject object = JSON.parseObject(ret);
            result.setMsg(object.get("reason").toString());
            result.setData(object.get("result"));
            result.setSuccess(true);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg("短信发送异常"+e.getMessage());
           // e.printStackTrace();
        }

        return result;
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || "GET".equals(method)){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || "GET".equals(method)){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && "POST".equals(method)) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
