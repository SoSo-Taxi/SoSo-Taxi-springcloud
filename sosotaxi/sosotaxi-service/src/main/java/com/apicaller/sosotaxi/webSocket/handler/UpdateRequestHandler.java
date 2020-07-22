package com.apicaller.sosotaxi.webSocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.utils.BDmapUtil;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.Message;
import com.apicaller.sosotaxi.webSocket.message.UpdateRequest;
import com.apicaller.sosotaxi.webSocket.message.UpdateResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 * 处理更新坐标请求
 */
@Component
public class UpdateRequestHandler implements MessageHandler<UpdateRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRequestHandler.class);


    @Override
    public void execute(Session session, UpdateRequest message) {
        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);

        LOGGER.info("[接入session{}]",session);
        LOGGER.info("[司机{} ]\"",loginDriver);


        loginDriver.getGeoPoint().setLat(message.getLat());
        loginDriver.getGeoPoint().setLng(message.getLng());
        loginDriver.setDispatched(message.isDispatched());
        loginDriver.setServerType(message.getServerType());
        loginDriver.setStartListening(message.isStartListening());

        LOGGER.info("[司机{}更新状态 {}]\"",JwtTokenUtils.getUsernameByToken(loginDriver.getToken()),loginDriver);
        UpdateResponse updateResponse = new UpdateResponse();

        LOGGER.info("[当前所有司机状态{}]\"",WebSocketUtil.getAllAvailableDrivers());

        updateResponse.setMessageId(message.getMessageId());
        updateResponse.setStatusCode(200);
        updateResponse.setMsg("更新位置和接单状态成功");
        WebSocketUtil.send(session,UpdateResponse.TYPE,updateResponse);


    }

    @Override
    public String getMessageType() {
        return UpdateRequest.TYPE;
    }


    @Test
    public void myTest()
    {
        GeoPoint geoPoint = new GeoPoint();
        GeoPoint geoPoint1 = new GeoPoint();
        geoPoint.setLat(39.939059);
        geoPoint.setLng(116.431002);

        geoPoint1.setLng(116.419217);
        geoPoint1.setLat(39.886373);

        System.out.println("距离"+BDmapUtil.calcDistance(geoPoint,geoPoint1,null));
    }
}
