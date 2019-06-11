package com.batsoft.utils;

import java.io.Serializable;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.batsoft.utils.gson.GsonSingleton;

/**
 * 火币行情数据同步
 * 
 * @author simon
 */
public class HuobiMarketDataSyncUtil {
	
	// 交易对
	private static Map<String, String> param = new HashMap<String, String>();
	static {
		param.put("BTC_USDT", "btcusdt");
		param.put("ETH_USDT", "ethusdt");
	}
	
	/**
	 * 获取火币行情数据
	 * 
	 * @param coinPair
	 * 			交易对
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> syncTickers(String coinPair) throws Exception {
		try {
			String action = "http://api.huobi.pro/market/tickers";
			String syncResultData = HttpClientUtil.get(action);
			if(StringUtils.hasText(syncResultData)) {
				SyncTickersDTO dto = GsonSingleton.getInstance().fromJson(syncResultData, SyncTickersDTO.class);
				if(dto != null && Objects.equals(dto.getStatus(), "ok")) {
					List<Map<String, Object>> data = dto.getData();
					for(Map<String, Object> value : data) {
						String symbol = (String) value.get("symbol");
						if(Objects.equals(param.get(coinPair), symbol)) {
							return value;
						}
					}
				}
			}
		} catch (SocketException e) {}
		return new HashMap<String, Object>();
	}
	
}

/**
 * 聚合行情（Ticker）
 * 
 * @author simon
 */
class SyncTickersDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String status;
	
	private Long ts;
	
	private List<Map<String, Object>> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
}
