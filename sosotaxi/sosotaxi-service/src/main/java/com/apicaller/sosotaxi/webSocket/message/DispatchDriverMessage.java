package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 */
@Data
public class DispatchDriverMessage implements Message {

    public static final String TYPE = "DISPATCH_DRIVER_MESSAGE";

    /**
     * 起点
     */
    private GeoPoint departPoint;

    /**
     * 终点
     */
    private GeoPoint destPoint;


    private String userName;
}
