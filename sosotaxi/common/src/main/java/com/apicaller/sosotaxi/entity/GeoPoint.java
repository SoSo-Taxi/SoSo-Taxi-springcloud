package com.apicaller.sosotaxi.entity;

import lombok.Data;
import java.io.Serializable;
import java.text.NumberFormat;

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

    public String toBDFormat(){
        return formatDouble(lat) + "," + formatDouble(lng);
    }

    /**
     * 取消科学计数法
     * @param d
     * @return
     */
    private static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(20);
        nf.setGroupingUsed(false);
        return nf.format(d);
    }
}
