package com.apicaller.sosotaxi.entity.dispatch.response;


import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 运力计算方法
 */
@Data
public class AvailableServiceCalResponse{
	private int serviceType;

	private double kmPrice;

	private double remotePrice;

	private String currency;

	private double minutePrice;

	private double basePrice;


}
