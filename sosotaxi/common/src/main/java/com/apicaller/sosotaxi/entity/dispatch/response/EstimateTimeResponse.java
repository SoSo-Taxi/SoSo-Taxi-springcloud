package com.apicaller.sosotaxi.entity.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 * 获取预计接驾时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimateTimeResponse {

    private int serviceType;

    /**
     * 预估时间
     */
    private int etaInSecond;

}
