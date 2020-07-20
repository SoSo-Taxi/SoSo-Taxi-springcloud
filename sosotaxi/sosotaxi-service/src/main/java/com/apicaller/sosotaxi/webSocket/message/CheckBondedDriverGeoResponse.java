package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

import java.awt.*;

/**
 * @author 张流潇潇
 * @createTime 2020/7/20
 * @updateTime
 */

@Data
public class CheckBondedDriverGeoResponse implements Message {

    public static final String TYPE = "CHECK_BONDED_DRIVER_GEO_RESPONSE";

    private Point point;

    private Short serverType;
}
