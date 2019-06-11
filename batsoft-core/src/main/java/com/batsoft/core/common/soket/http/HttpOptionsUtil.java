package com.batsoft.core.common.soket.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

import com.batsoft.core.common.enums.CHS;
import com.batsoft.core.common.enums.RequestMethodEnum;
import com.batsoft.core.common.soket.http.behavior.HttpRequestBehavior;

/**
 * HttpOptions 请求
 * 
 * @author simon
 */
public class HttpOptionsUtil implements HttpRequestBehavior<List<RequestMethodEnum>> {

	@Override
	public List<RequestMethodEnum> request(String action, Map<String, String> param) {
		List<RequestMethodEnum> result = new ArrayList<>();
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			URIBuilder builder = new URIBuilder(action);
			if (param != null && param.size() > 0) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			HttpOptions request = new HttpOptions(uri);
			HttpResponse response = client.execute(request);
			Header hander = response.getLastHeader("Allow");
			if (hander != null && StringUtils.hasText(hander.getValue())) {
				String[] emp = hander.getValue().split(CHS.english_comma.getValue());
				for (int i = 0; i < emp.length; i++) {
					for (RequestMethodEnum method : RequestMethodEnum.values()) {
						if (Objects.equals(emp[i].trim(), method.name())) {
							result.add(method);
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println(" Options request Exception " + e.getMessage());
		} catch (URISyntaxException e) {
			System.out.println(" new URIBuilder throws URISyntaxException " + e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
