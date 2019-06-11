package com.batsoft.model.module.exchange;

/**
 * Created with IDEA
 * description:
 * author:liquanyu
 * Date:2018/8/26
 * Time:21:27
 */
public class Cancle {
	//[{userid: "b21c0bae3a6741cc985324f3d87f86df", coinpair: "ETC_USDT",…}]
	private String userid;
	private String coinpair;
	private String orderid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCoinpair() {
		return coinpair;
	}

	public void setCoinpair(String coinpair) {
		this.coinpair = coinpair;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
}
