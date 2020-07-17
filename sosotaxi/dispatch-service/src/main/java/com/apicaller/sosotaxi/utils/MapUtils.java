package com.apicaller.sosotaxi.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.config.MyProps;
import com.apicaller.sosotaxi.entity.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    @Value("${baiduapi.ak}")
    private static String AK;

    @Value("${baiduapi.url}")
    private static String url;

    public static String getDirection(GeoPoint originPoint, GeoPoint destination) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("origin",originPoint.toBDFormat());
        paramMap.put("destination",destination.toBDFormat());
        paramMap.put("ak",AK);
        String result = "";
        try{
            result = HttpUtil.post(url+"/directionlite/v1/driving",paramMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api调用异常");
        }
        //TODO 处理result，直接返回更具体有用的信息
        //JSONObject jObject = JSON.parseObject(result);
        return result;
    }

    /**
     * 从api返回json中获得路程
     * @param json
     * @return
     * @throws Exception
     */
    public static String getDistance(JSONObject json) throws Exception {
        String distance = "";
        try{
            distance = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getString("distance");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api返回json解析失败");
        }
        return distance;
    }

    /**
     * 从api返回json中获得预计时间
     * @param json
     * @return
     * @throws Exception
     */
    public static String getDuration(JSONObject json) throws Exception {
        String duration;
        try{
            duration = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getString("duration");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api返回json解析失败");
        }
        return duration;
    }
}
