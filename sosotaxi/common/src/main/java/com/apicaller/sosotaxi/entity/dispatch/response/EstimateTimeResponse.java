package com.apicaller.sosotaxi.entity.dispatch.response;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 获取预计接驾时间
 */
@Data
public class EstimateTimeResponse {

    private int serviceType;

    /**
     * 预估时间
     */
    private int etaInSecond;

}
