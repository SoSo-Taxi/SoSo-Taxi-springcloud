package com.apicaller.sosotaxi.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.config.MyProps;
import com.apicaller.sosotaxi.entity.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapUtils {

    @Autowired
    private MyProps props;

    public String getDirection(GeoPoint originPoint, GeoPoint destination) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("origin",originPoint.toBDFormat());
        paramMap.put("destination",destination.toBDFormat());
        String AK = props.getAk();
        String url = props.getUrl();
        paramMap.put("ak",AK);
        String result = "";
        try{
            result = HttpUtil.get(url+"/directionlite/v1/driving",paramMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("错误的调用:"+url+"_"+AK);
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
    public static int getDistance(JSONObject json) throws Exception {
        int distance;
        try{
            distance = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getInteger("distance");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api返回json解析失败");
        }
        return distance;
    }

    /**
     * 从api返回json中获得路程
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static int getDistance(String jsonStr) throws Exception {
        int distance;
        try{
            JSONObject json = JSONObject.parseObject(jsonStr);
            distance = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getInteger("distance");
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
    public static int getDuration(JSONObject json) throws Exception {
        int duration;
        try{
            duration = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getInteger("duration");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api返回json解析失败:"+json);
        }
        return duration;
    }

    /**
     * 从api返回json中获得预计时间
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static int getDuration(String jsonStr) throws Exception {
        int duration;
        try{
            JSONObject json = JSONObject.parseObject(jsonStr);
            duration = json.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getInteger("duration");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("api返回json解析失败:"+jsonStr);
        }
        return duration;
    }
}
