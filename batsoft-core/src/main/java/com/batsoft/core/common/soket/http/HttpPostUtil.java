package com.batsoft.core.common.soket.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.batsoft.core.common.soket.http.behavior.HttpRequestBehavior;

/**
 * HttpPostUtil 客户端
 * 
 * @author simon
 */
public class HttpPostUtil implements HttpRequestBehavior<String> {

	/**
	 * 简单的Post请求
	 * 
	 * @param action
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @return
	 */
	@Override
	public String request(String action, Map<String, String> param) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		StringBuilder result = null;
		try {
			HttpPost httpPost = new HttpPost(action);
			if (param != null && param.size() > 0) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
				httpPost.setEntity(entity);
			}
			response = httpClient.execute(httpPost);
			result = new StringBuilder(EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(result);
	}

	/**
	 * 简单的Post请求
	 * 
	 * @param action
	 *            请求地址
	 * @param param
	 *            请求参数
	 * @param charset
	 *            编码
	 * @return
	 */
	public String request(String action, Map<String, String> param, String charset) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		StringBuilder result = null;
		try {
			HttpPost httpPost = new HttpPost(action);
			if (param != null && param.size() > 0) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, charset);
				httpPost.setEntity(entity);
			}
			response = httpClient.execute(httpPost);
			result = new StringBuilder(EntityUtils.toString(response.getEntity(), charset));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(result);
	}
	
}
