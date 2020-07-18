package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 */
@Data
public class GetAllDriverResponse implements Message{

    public static final String TYPE = "GETALLDRIVER_RESPONSE";

    List<GeoPoint> geoPoints = new ArrayList<>();



}
