package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.webSocket.message.ArriveDepartPointMessage;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 到达上车点
 */

@Component
public class ArriveDepartPointMessageHandler implements MessageHandler<ArriveDepartPointMessage> {
    @Override
    public void execute(Session session, ArriveDepartPointMessage message) {


    }

    @Override
    public String getMessageType() {
        return ArriveDepartPointMessage.TYPE;
    }
}
