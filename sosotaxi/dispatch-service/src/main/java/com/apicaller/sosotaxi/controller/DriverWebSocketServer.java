package com.apicaller.sosotaxi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.service.impl.DispatchServiceImpl;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/dispatch/driver/{driverId}")
@Controller
public class DriverWebSocketServer {

    //暂时没加log，后面考虑加上

    @Resource
    private InfoCacheServiceImpl infoCacheService;

    @Resource
    private DispatchServiceImpl dispatchService;

    public DriverWebSocketServer(){
        dispatchService.dispatch();
    }

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
        ResponseBean response = new ResponseBean(200, "连接成功", null);
        String returnMsg = JSON.toJSONString(response);
        try{
            sendMessage(session, returnMsg);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("连接建立时发送信息出错");
        }
    }

    /**
     * 为了订单刷新的时间由客户端决定，仅在收到要求刷新的message后返回订单分配
     */
    @OnMessage
    //TODO:写接受订单时不要忘了鉴定真实性
    public void onMessage(String message) throws Exception {
        JSONObject msgJson = JSON.parseObject(message);
        PulseMsg msg = msgJson.toJavaObject(PulseMsg.class);
        MinimizedDriver driver = msg.getDriver();
        GeoPoint point = msg.getPoint();
        String requestType = msg.getRequest();
        String detail = msg.getDetail();
        try{
            infoCacheService.updateDriverField(driver, point);
        }catch (Exception e){
            e.printStackTrace();
        }
        ResponseBean response = new ResponseBean(500, "服务器处理出错",null);
        if(PulseMsg.RequestType.getOrders.equals(PulseMsg.RequestType.valueOf(requestType))){
            List<UnsettledOrder> orders = infoCacheService.getDispatch(driver.getDriverId());
            response.setCode(200);
            response.setMsg("订单列表");
            response.setData(orders);
        }
        else if((PulseMsg.RequestType.acceptOrder.equals(PulseMsg.RequestType.valueOf(requestType)))){
            Boolean isSuccess = infoCacheService.acceptOrder(detail,driver.getDriverId());
            response.setCode(200);
            if(isSuccess){
                response.setMsg("接单成功");
                //客户端应该存了一份，或许不需要再发
                //response.setData();
            }else{
                response.setMsg("订单已不存在");
            }
        }
        else{
            response.setCode(200);
            response.setMsg("位置信息上传成功");
        }
        String returnMsg = JSON.toJSONString(response);
        try{
            sendInfo(driver.getDriverId(),returnMsg);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("连接建立时发送信息出错");
        }
    }

    @OnClose
    public void onClose(@PathParam(value = "driverId") String userId){
        sessionPools.remove(userId);
        try{
            infoCacheService.deleteDriverField(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        error.printStackTrace();
    }
}
