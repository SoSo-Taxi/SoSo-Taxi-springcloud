package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/20
 * @updateTime
 */
@Data
public class CheckBondedDriverGeoRequest implements Message {

    public static final String TYPE = "CHECK_BONDED_DRIVER_GEO_MESSAGE";

    private String userToken;

    private GeoPoint point;
}
