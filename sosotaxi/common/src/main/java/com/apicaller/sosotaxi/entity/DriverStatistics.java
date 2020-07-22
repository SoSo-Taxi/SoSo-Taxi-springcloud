package com.apicaller.sosotaxi.entity;

import lombok.Data;


/**
 * 司机端显示统计数据对应的实体类。可能需要和前端统一一下。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 7:39 下午
 * @updateTime:
 */
@Data
public class DriverStatistics {

    private Double serviceScore;

    private Double accountFlow;

    private Integer workMinutes;

    private Integer orderNum;
}
