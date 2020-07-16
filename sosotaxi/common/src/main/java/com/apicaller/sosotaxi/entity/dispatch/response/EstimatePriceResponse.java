package com.apicaller.sosotaxi.entity.dispatch.response;


import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 预计价格
 */
@Data
public class EstimatePriceResponse{
	private int serviceType;

	private double distance;

	private double price;

	private int etaInSecond;

	private String currency;


}
