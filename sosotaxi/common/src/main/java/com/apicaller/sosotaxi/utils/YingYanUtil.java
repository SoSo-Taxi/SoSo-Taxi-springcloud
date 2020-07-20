package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
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
    private static final String AK = "FeRXxGzcbvblf88iUeEvM74Yo1bXdjIH";

    private static final String HOST = "yingyan.baidu.com";

    private static final Integer SERVICE_ID = 222373;

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
                        .queryParam("process_option", buildProcessOption(argMap, "="))
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
    public static JSONObject getDistance(String username, int startTime, int endTime,
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
                        //默认以驾车方式补偿里程。
                        .queryParam("supplement_mode", "driving")
                        .queryParam("process_option", buildProcessOption(argMap, "="))
                        .build())
                .retrieve().bodyToMono(String.class).block();

        return JSONObject.parseObject(res);
    }

    /**
     * 获取一个实体一段时间内的轨迹里程。简略版。
     * @param startTime 开始的时间，以秒记的unix时间戳
     * @param endTime 结束时间，以秒记的unix时间戳
     */
    public static JSONObject getDistance(String username, int startTime, int endTime) {
        return getDistance(username, startTime, endTime, 4, true);
    }

    /**
     * 在一个圆形区域内搜索司机。
     */
    public static JSONObject aroundSearchDriver(double centerLat, double centerLng, CoordType coordType, int radius) {
        Map<String, Object> argMap = new HashMap<String, Object>();
        //将30秒前不活跃的点过滤掉
        argMap.put("active_time", System.currentTimeMillis()/1000 - 30);
        //自定义的role字段
        argMap.put("role", "driver");

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
                        .queryParam("filter", buildProcessOption(argMap, ":"))
                        .queryParam("coord_type_input", finalCoordType.getStirng())

                        .build())
                .retrieve().bodyToMono(String.class).block();

        return JSONObject.parseObject(res);
    }

    public static boolean updateDriver(String username, Boolean isDispatched) {
        if(username == null || isDispatched == null) {
            return false;
        }
        MultiValueMap<String, String> formdata = new LinkedMultiValueMap<String, String>();
        formdata.add("ak", AK);
        formdata.add("service_id", SERVICE_ID.toString());
        formdata.add("entity_name", username);
        formdata.add("is_dispatched", isDispatched.toString());

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
            return false;
        }
        return result.getInteger("status") == 0;
    }


    /**
     * 鹰眼api中有"process_option"或"filter"复合参数，用此方法构建。
     * @param argMap 参数map
     * @param separator key与value之间的分隔符
     * @return
     */
    private static String buildProcessOption(Map<String, Object> argMap, String separator) {
        StringBuilder sb = new StringBuilder("");
        argMap.forEach((k,v) -> {
            sb.append(k);
            sb.append(separator);
            sb.append(v.toString());
            sb.append(",");
        });
        //去除最后一个逗号
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
