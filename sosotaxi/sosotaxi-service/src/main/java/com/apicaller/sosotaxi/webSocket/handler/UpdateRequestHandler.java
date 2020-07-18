package com.apicaller.sosotaxi.webSocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.Message;
import com.apicaller.sosotaxi.webSocket.message.UpdateRequest;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
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
        LoginDriver loginDriver = WebSocketUtil.findDriverBySession(session);

//        loginDriver.setLat(message.getLat());
//        loginDriver.setLng(message.getLng());

        LOGGER.info("[司机{} ]\"",loginDriver);


        loginDriver.getGeoPoint().setLat(message.getLat());
        loginDriver.getGeoPoint().setLng(message.getLng());
        loginDriver.setIsDispatched(message.getIsDispatched());

        LOGGER.info("[司机{}更新状态 ]\"",JwtTokenUtils.getUsernameByToken(loginDriver.getToken()));
        LOGGER.info("[坐标{}更新状态 ]\"",loginDriver.getGeoPoint());
    }

    @Override
    public String getMessageType() {
        return UpdateRequest.TYPE;
    }
}
