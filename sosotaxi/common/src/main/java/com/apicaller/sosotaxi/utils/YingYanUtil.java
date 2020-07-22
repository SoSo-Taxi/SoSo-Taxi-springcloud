package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.bdmap.AroundSearchDriverResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用百度地图鹰眼Api的工具类。还没有完成，看需要继续编写。
 * 其中的方法有：
 * 1 获取一个实体最新的位置点
 * 2 获取一个实体一段时间内的轨迹里程
 *
 * @author: 骆荟州
 * @createTime: 2020/7/17 9:04 下午
 * @updateTime:
 */
public class YingYanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(YingYanUtil.class);

    private static final String AK = "isBsWKfegvkIbVNwnufKzTsibPln3DIV";

    private static final String HOST = "yingyan.baidu.com";

    private static final Integer SERVICE_ID = 222372;

    /**
     * 获取一个实体最新的位置点。
     * @param username
     * @param denoiseGrade
     * @param mapMatch
     * @param coordType
     */
    public static JSONObject getLatestPoint(String username, int denoiseGrade,
                                      boolean mapMatch, CoordType coordType) {

        

        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("denoise_grade", denoiseGrade);
        argMap.put("need_mapmatch", mapMatch == true? 1 : 0);
        argMap.put("transport_mode", "auto");

        coordType = coordType == null ? CoordType.bd09ll : coordType;
        CoordType finalCoordType = coordType;

        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/api/v3/track/getlatestpoint")
                        .queryParam("ak", AK)
                        .queryParam("service_id", SERVICE_ID)
                        .queryParam("entity_name", username)
                        .queryParam("process_option", buildProcessOption(argMap, "=", ","))
                        .queryParam("coord_type_output", finalCoordType.getStirng())
                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject result = JSONObject.parseObject(res);

        return result;
    }

    /**
     * 获取一个实体最新的位置点。
     * 适用于获取司机的位置。
     * @param username
     */
    public static JSONObject getLatestPoint(String username) {
        return getLatestPoint(username, 4, true, CoordType.bd09ll);
    }

    /**
     * 获取一个实体一段时间内的轨迹里程。
     * @param startTime 开始的时间，以秒记的unix时间戳
     * @param endTime 结束时间，以秒记的unix时间戳
     */
    public static Double getDistance(String username, int startTime, int endTime,
                                   int denoiseGrade, boolean mapMatch) {

        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("denoise_grade", denoiseGrade);
        argMap.put("need_mapmatch", mapMatch == true? 1 : 0);
        argMap.put("transport_mode", "auto");

        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/api/v3/track/getdistance")
                        .queryParam("ak", AK)
                        .queryParam("service_id", SERVICE_ID)
                        .queryParam("entity_name", username)
                        .queryParam("start_time", startTime)
                        .queryParam("end_time", endTime)
                        .queryParam("is_processed", "1")
                        //默认以驾车方式补偿里程。
                        .queryParam("supplement_mode", "driving")
                        .queryParam("process_option", buildProcessOption(argMap, "=", ","))
                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject resultObj = JSONObject.parseObject(res);
        if (resultObj.getInteger("status") == null || resultObj.getInteger("status") != 0) {
            LOGGER.warn("[鹰眼服务]" + resultObj.getString("message"));
            return null;
        }

        return resultObj.getDouble("distance");
    }

    /**
     * 获取一个实体一段时间内的轨迹里程。简略版。
     * @param startTime 开始的时间，以秒记的unix时间戳
     * @param endTime 结束时间，以秒记的unix时间戳
     */
    public static Double getDistance(String username, int startTime, int endTime) {
        return getDistance(username, startTime, endTime, 4, true);
    }

    /**
     * 在一个圆形区域内搜索司机。
     * 半径单位是米。
     * serviceType不填则为搜索所有司机。
     */
    public static List<AroundSearchDriverResponse> aroundSearchDriver(double centerLat, double centerLng, CoordType coordType,
                                                                      int radius, boolean availableOnly, Short serviceType) {
        Map<String, Object> argMap = new HashMap<String, Object>();
        //将30秒前不活跃的点过滤掉
        argMap.put("active_time", System.currentTimeMillis()/1000 - 30);
        if(availableOnly) {
            argMap.put("is_available", "true");
        }
        if(serviceType != null) {
            argMap.put("service_type", serviceType.toString());
        }

        coordType = coordType == null ? CoordType.bd09ll : coordType;
        CoordType finalCoordType = coordType;

        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/api/v3/entity/aroundsearch")
                        .queryParam("ak", AK)
                        .queryParam("service_id", SERVICE_ID)
                        .queryParam("center", centerLat + "," + centerLng)
                        .queryParam("radius", radius)
                        .queryParam("filter", buildProcessOption(argMap, ":", "|"))
                        .queryParam("coord_type_input", finalCoordType.getStirng())

                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject resultObj = JSONObject.parseObject(res);
        if (resultObj.getInteger("status") == null || resultObj.getInteger("status") != 0) {
            LOGGER.warn("[鹰眼服务]" + resultObj.getString("message"));
            return null;
        }
        JSONArray resultArr = resultObj.getJSONArray("entities");
        if (resultArr == null) {
            return null;
        }

        List<AroundSearchDriverResponse> resultList = new ArrayList<>();
        resultArr.forEach(obj -> {

            JSONObject aResult = JSONObject.parseObject(obj.toString());
            JSONObject loc = aResult.getJSONObject("latest_location");
            AroundSearchDriverResponse result = new AroundSearchDriverResponse();
            result.setDriverName(aResult.getString("entity_name"));
            result.setDirection(loc.getInteger("direction"));
            result.setSpeed(loc.getDouble("speed"));
            result.setServiceType(aResult.getShort("service_type"));
            result.setPoint(new GeoPoint(loc.getDouble("latitude"), loc.getDouble("longitude")));
            resultList.add(result);
        });

        return resultList;
    }

    /**
     * 更新司机状态信息（是否可用）
     */
    public static boolean updateDriver(String username, Boolean isAvailable) {
        if(username == null || isAvailable == null) {
            return false;
        }
        MultiValueMap<String, String> formdata = new LinkedMultiValueMap<String, String>();
        formdata.add("ak", AK);
        formdata.add("service_id", SERVICE_ID.toString());
        formdata.add("entity_name", username);
        formdata.add("is_available", isAvailable.toString());

        String res = WebClient.create()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/api/v3/entity/update")
                        .build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData(formdata))
                .retrieve().bodyToMono(String.class).timeout(Duration.of(10, ChronoUnit.SECONDS))
                .block();

        JSONObject result = JSONObject.parseObject(res);
        if (result.getInteger("status") == null) {
            LOGGER.warn("[鹰眼服务]" + result.getString("message"));
            return false;
        }
        return result.getInteger("status") == 0;
    }


    /**
     * 鹰眼api中有"process_option"或"filter"复合参数，用此方法构建。
     * 百度的api有点蛋疼，分隔符可能是不同的，需要注意
     * @param argMap 参数map
     * @param connector key与value之间的连接符
     * @param separator 多个参数间的分隔符
     * @return
     */
    private static String buildProcessOption(Map<String, Object> argMap, String connector, String separator) {
        StringBuilder sb = new StringBuilder("");
        argMap.forEach((k,v) -> {
            sb.append(k);
            sb.append(connector);
            sb.append(v.toString());
            sb.append(separator);
        });
        //去除最后一个分隔符号
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
