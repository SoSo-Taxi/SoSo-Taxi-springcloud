package com.apicaller.sosotaxi.entity.bdmap;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/22 9:07 上午
 * @updateTime:
 */
@Data
public class AroundSearchDriverResponse {
    private String driverName;

    private GeoPoint point;

    private Double speed;

    private Integer direction;

    private Short serviceType;
}
