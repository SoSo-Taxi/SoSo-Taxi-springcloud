package com.apicaller.sosotaxi.entity.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 可用服务类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableServiceTypeResponse{
	private int serviceType;
	private int seats;

}
