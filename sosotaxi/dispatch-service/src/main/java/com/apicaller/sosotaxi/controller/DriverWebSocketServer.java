package com.apicaller.sosotaxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.bean.MinimizedDriver;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.service.impl.DispatchServiceImpl;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/dispatch/driver/{driverId}")
@Controller
public class DriverWebSocketServer {

    //暂时没加log，后面考虑加上

    @Resource
    private DispatchServiceImpl dispatchService;

    /**
     * 安全存放每个客户端对应的Server
     */
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * 服务器发送消息给Session
     * @param session
     * @param message
     * @throws IOException
     */
    public void sendMessage(Session session, String message) throws IOException{
        if(session != null){
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 给指定用户发送信息
     * @param driverId
     * @param message
     */
    public void sendInfo(String driverId, String message) throws Exception {
        Session session = sessionPools.get(driverId);
        try{
            sendMessage(session, message);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("给指定用户发送信息时出错");
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "driverId") String userId) throws Exception {
        sessionPools.put(userId, session);
        ResponseBean response = new ResponseBean(200, "onOpen", null);
        String msg = JSON.toJSONString(response);
        try{
            sendMessage(session, msg);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("连接建立时发送信息出错");
        }
    }

    @OnMessage
    public void onMessage(String message){
        JSONObject jsonObject = JSON.parseObject(message);
        MinimizedDriver driver = jsonObject.getJSONObject("driverInfo").toJavaObject(MinimizedDriver.class);
        GeoPoint point = jsonObject.getJSONObject("GeoPoint").toJavaObject(GeoPoint.class);
        try{
            dispatchService.updateDriverField(driver, point);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam(value = "driverId") String userId){
        sessionPools.remove(userId);
        try{
            dispatchService.deleteDriverField(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        error.printStackTrace();
    }
}
