package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 乘客端获取所有的司机坐标
 */
@Data
public class GetAllDriverResponse implements Message{

    public static final String TYPE = "GET_ALL_DRIVER_RESPONSE";

    List<GeoPoint> geoPoints = new ArrayList<>();

    private String msg;

    private int statusCode;

}
