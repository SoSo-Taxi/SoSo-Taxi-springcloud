package com.apicaller.sosotaxi.entity.dispatch.response;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 可用服务类型
 */
@Data
public class AvailableServiceTypeResponse{
	private int serviceType;
	private int seats;


}
