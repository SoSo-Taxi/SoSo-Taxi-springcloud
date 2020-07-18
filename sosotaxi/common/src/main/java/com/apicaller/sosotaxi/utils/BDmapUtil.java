package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.GeoPoint;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 调用百度地图WebApi的工具类
 * @author: 骆荟州
 * @createTime: 2020/7/17 6:08 下午
 * @updateTime:
 */
public class BDmapUtil {
    private static final String AK = "FeRXxGzcbvblf88iUeEvM74Yo1bXdjIH";

    private static final String HOST = "api.map.baidu.com";

    private static final String REVERSE_GEOCODING = "/reverse_geocoding/v3/";

    /**
     * 逆地理编码，用GeoPoint作为参数。
     * @param point
     * @param coordType
     * @param radius
     * @return
     */
    public static String reverseGeocoding(GeoPoint point, CoordType coordType, int radius) {

        return BDmapUtil.reverseGeocoding(point.getLat(), point.getLng(), coordType, radius);
    }

    /**
     * 逆地理编码
     * @param lat
     * @param lng
     * @param coordType
     * @param radius
     * @return
     */
    public static String reverseGeocoding(double lat, double lng, CoordType coordType, int radius) {

        CoordType ct = coordType == null? CoordType.bd09ll : coordType;
        System.out.println(ct.getStirng());
        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path(REVERSE_GEOCODING)
                        .queryParam("ak", AK)
                        .queryParam("coordtype", ct.getStirng())
                        .queryParam("radius", radius)
                        .queryParam("ret_coordtype", CoordType.bd09ll.getStirng())
                        .queryParam("extensions_poi", "1")
                        .queryParam("output", "json")
                        .queryParam("location", lat + "," + lng)
                        .build())
                .retrieve().bodyToMono(String.class).block();
        JSONObject result = JSONObject.parseObject(res);

        System.out.println(res);

        return res;
    }
}
