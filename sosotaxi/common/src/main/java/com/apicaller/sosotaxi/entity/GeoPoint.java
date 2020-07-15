package com.apicaller.sosotaxi.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 表示一个地理上的点。
 *
 * @author 骆荟州
 * @createTime 2020/7/13 13:11
 * @updateTime
 */
@Data
public class GeoPoint implements Serializable {
    private static final long serialVersionUID = 42345441L;

    public GeoPoint() { }

    public GeoPoint(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    /** 纬度 */
    private double lat;

    /** 经度 */
    private double lng;

    @Override
    public String toString(){
        return "Point(lat:" + lat + ", lng:" + lng + ")";
    }

    public String toPointFormat() {
        return "POINT(" + lat + " " + lng + ")";
    }
}
