package com.batsoft.app.exchange.pattern.singleton;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

import com.batsoft.utils.HttpClientUtil;
import com.batsoft.utils.gson.GsonSingleton;

/**
 * 同步火币行情价格
 * 
 * @author simon
 */
public class HuobiMarketDataSyncSingleton {
	
	private HuobiMarketDataSyncSingleton() {}
	
	private static HuobiMarketDataSyncSingleton instance = new HuobiMarketDataSyncSingleton();

	public static HuobiMarketDataSyncSingleton getInstance() {
		return instance;
	}
	
	private static ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(2);
	private static boolean startBool = true;
	
	
	public void initialize() {
		if(startBool) {
			startBool = false;
			// 启动同步数据线程
			scheduledPool.scheduleAtFixedRate(new SyncDataJob(), 0, 1, TimeUnit.SECONDS);
		}
	}
	
	// 键值
	private static Map<String, BigDecimal> values = new HashMap<String, BigDecimal>();
	
	/**
	 * 根据交易对获取价格
	 * 
	 * @param symbol
	 * 			交易对
	 * @return
	 */
	public BigDecimal getHuobiMarketPrice(String symbol) {
		BigDecimal price = values.get(symbol);
		if(price == null) {
			price = BigDecimal.ZERO;
		}
		return price;
	}
	
	
	// 交易对
	private static Map<String, String> param = new HashMap<String, String>();
	static {
		param.put("BTC_USDT", "btcusdt");
		param.put("ETH_USDT", "ethusdt");
	}
	
	/**
	 * 执行同步价格
	 * 
	 * @author simon
	 */
	private final class SyncDataJob implements Runnable {
		@Override
		public void run() {
			try {
				String action = "http://api.huobi.pro/market/tickers";
				String result = HttpClientUtil.get(action);
				Set<String> keys = param.keySet();
				if(StringUtils.hasText(result)) {
					SyncTickersDTO dto = GsonSingleton.getInstance().fromJson(result, SyncTickersDTO.class);
					if(dto != null && Objects.equals(dto.getStatus(), "ok")) {
						List<Map<String, Object>> data = dto.getData();
						for(Map<String, Object> value : data) {
							String symbol = (String) value.get("symbol");
							for(String key : keys) {
								if(Objects.equals(param.get(key), symbol)) {
									BigDecimal price = new BigDecimal(String.valueOf(value.get("close")));
									values.put(key, price);
								}
							}
						}
					}
				}
			} catch (Exception e) {}
		}
	}
}
/**
 *  交易对的最新 Tickers
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

