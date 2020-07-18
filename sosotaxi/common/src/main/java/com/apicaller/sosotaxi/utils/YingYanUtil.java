package com.apicaller.sosotaxi.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用百度地图鹰眼Api的工具类。
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

    private static final int SERVICE_ID = 222373;

    /**
     * 获取一个实体最新的位置点。
     * @param username
     * @param denoiseGrade
     * @param mapMatch
     * @param coordType
     */
    public static void getLatestPoint(String username, int denoiseGrade,
                                      boolean mapMatch, CoordType coordType) {

        Map<String, Object> argMap = new HashMap<String, Object>();
        argMap.put("denoise_grade", denoiseGrade);
        argMap.put("need_mapmatch", mapMatch == true? 1 : 0);
        argMap.put("transport_mode", "auto");

        String res = WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(HOST)
                        .path("/api/v3/track/getlatestpoint")
                        .queryParam("ak", AK)
                        .queryParam("service_id", SERVICE_ID)
                        .queryParam("entity_name", username)
                        .queryParam("process_option", buildProcessOption(argMap))
                        .queryParam("coord_type_output", coordType.getStirng())
                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject result = JSONObject.parseObject(res);

    }

    /**
     * 获取一个实体最新的位置点。
     * 适用于获取司机的位置。
     * @param username
     */
    public static void getLatestPoint(String username) {
        getLatestPoint(username, 4, true, CoordType.bd09ll);
    }

    /**
     * 获取一个实体一段时间内的轨迹里程。
     * @param startTime 开始的时间，以秒记的unix时间戳
     * @param endTime 结束时间，以秒记的unix时间戳
     */
    public static void getDistance(String username, int startTime, int endTime,
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
                        .queryParam("process_option", buildProcessOption(argMap))
                        .build())
                .retrieve().bodyToMono(String.class).block();

        JSONObject result = JSONObject.parseObject(res);
    }

    /**
     * 获取一个实体一段时间内的轨迹里程。简略版。
     * @param startTime 开始的时间，以秒记的unix时间戳
     * @param endTime 结束时间，以秒记的unix时间戳
     */
    public static void getDistance(String username, int startTime, int endTime) {
        getDistance(username, startTime, endTime, 4, true);
    }


    /**
     * 鹰眼api中有一个"process_option"复合参数，用此方法构建。
     * @param argMap
     * @return
     */
    private static String buildProcessOption(Map<String, Object> argMap) {
        StringBuilder sb = new StringBuilder("");
        argMap.forEach((k,v) -> {
            sb.append(k);
            sb.append("=");
            sb.append(v.toString());
            sb.append(",");
        });
        //去除最后一个逗号
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
