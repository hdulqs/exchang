package com.batsoft.core.common.soket.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.batsoft.core.common.soket.http.behavior.HttpRequestBehavior;

/**
 * Get请求工具类
 * 
 * @author ppmle
 */
public class HttpGetUtil implements HttpRequestBehavior<String> {

	@Override
	public String request(String action, Map<String, String> param) {
		StringBuilder result = new StringBuilder();
		CloseableHttpClient client = HttpClients.createDefault();
		InputStreamReader inputRead = null;
		BufferedReader bufferedReader = null;
		try {
			URIBuilder builder = new URIBuilder(action);
			if (param != null && param.size() > 0) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			inputRead = new InputStreamReader(stream);
			bufferedReader = new BufferedReader(inputRead);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			System.out.println(" Get request Exception " + e.getMessage());
		} catch (URISyntaxException e) {
			System.out.println(" new URIBuilder throws URISyntaxException " + e.getMessage());
		} finally {
			try {
				if (inputRead != null) {
					inputRead.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return String.valueOf(result);
	}

}
