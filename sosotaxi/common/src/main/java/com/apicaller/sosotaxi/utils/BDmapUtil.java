package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.bdmap.SimpleDistance;
import com.apicaller.sosotaxi.entity.bdmap.SimplePosition;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * 调用百度地图WebApi的工具类
 * 此类中的距离，指的都是驾车路线的距离，时间指的都是驾车时间。
 *
 * 此类中的方法有：
 * 1 逆地理编码
 * 2 批量算路
 *
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
    public static JSONObject reverseGeocoding(GeoPoint point, CoordType coordType, int radius) {

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
    public static JSONObject reverseGeocoding(double lat, double lng, CoordType coordType, int radius) {

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

        return JSONObject.parseObject(res);
    }

    /**
     * 调用批量算路Api。计算多个点到多个点的距离
     * 结果顺序是：（假设两个起点两个终点）
     * origin1 -- dest1
     * origin1 -- dest2
     * origin2 -- dest1
     * origin2 -- dest2
     */
    public static <T> List<SimpleDistance<T>> batchCalcDistance(List<SimplePosition<T>> origins,
                                                                List<SimplePosition<T>> dest, CoordType coordType) {
        if(origins == null || dest == null) {
            return null;
        }
        //适应百度起点终点之乘积不能超过50的要求
        if(dest.size() * origins.size() > 50) {
            dest = dest.subList(0, 50 / origins.size());
            if(dest.isEmpty()) {
                return null;
            }
        }
        coordType = coordType == null ? CoordType.bd09ll : coordType;

        //调用百度地图API查询每对可能的乘客司机对之间的距离。
        CoordType finalCoordType = coordType;
        List<SimplePosition<T>> finalDest = dest;
        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/routematrix/v2/driving")
                        .queryParam("ak", AK)
                        .queryParam("coord_type", finalCoordType.toString())
                        .queryParam("tactics", "11")
                        .queryParam("origins", buildPointString(origins))
                        .queryParam("destinations", buildPointString(finalDest))
                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject jsonObject = JSONObject.parseObject(res);
        if(jsonObject.getInteger("status") != 0) {
            return null;
        }

        List<SimpleDistance<T>> resultList = new ArrayList<SimpleDistance<T>>();
        JSONArray resultArr = jsonObject.getJSONArray("result");
        int originCount = origins.size();
        int count = resultArr.size();
        for(int i = 0; i < count; i++) {
            double duration = JSONObject.parseObject(resultArr.get(i).toString())
                                        .getJSONObject("duration").getDouble("value");
            double distance = JSONObject.parseObject(resultArr.get(i).toString())
                    .getJSONObject("distance").getDouble("value");

            resultList.add(new SimpleDistance<T>(origins.get(i/originCount).getTag(),
                    dest.get(i%originCount).getTag(), distance, duration));
        }
        return resultList;
    }

    /**
     * 调用批量算路Api。此方法计算一个点到至多50个点的距离。
     */
    public static <T> List<SimpleDistance<T>> batchCalcDistance(SimplePosition<T> origin,
                                                                List<SimplePosition<T>> dest, CoordType coordType) {
        List<SimplePosition<T>> list = new ArrayList<SimplePosition<T>>();
        list.add(origin);
        return batchCalcDistance(list, dest, coordType);
    }

    /**
     * 计算两点间距离。返回值单位是米。
     */
    public static Double calcDistance(GeoPoint point1, GeoPoint point2, CoordType coordType) {

        List<SimplePosition<String>> arglist = new ArrayList<>();
        arglist.add(new SimplePosition<String>("b", point2));
        List<SimpleDistance<String>> result = batchCalcDistance(new SimplePosition<String>("a", point1), arglist, coordType);
        if(result == null) {
            return null;
        }
        return  result.get(0).getDistance();
    }


    /** 将一个个的点转为百度地图Api调用时需要的字符串格式
     * 点与点之间用"｜"分隔。
     */
    private static <T> String buildPointString(List<SimplePosition<T>> pos) {
        StringBuilder output = new StringBuilder("");
        int count = pos.size();
        for(int i = 0; i < count; i++) {
            if(i != 0) {
                output.append("|");
            }
            output.append(pos.get(i).getPoint().toBDFormat());
        }
        return output.toString();
    }
}
