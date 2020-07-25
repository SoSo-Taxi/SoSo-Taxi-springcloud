package com.apicaller.sosotaxi.entity;

import lombok.Data;
import java.util.Date;


/**
 * 司机端显示统计数据对应的实体类。可能需要和前端统一一下。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 7:39 下午
 * @updateTime:
 */
@Data
public class DriverStatistics {

    private long driverId;

    private Date recordDate;

    private Integer serviceScore;

    private Double accountFlow;

    private Integer workSeconds;

    private Integer orderNum;
}
