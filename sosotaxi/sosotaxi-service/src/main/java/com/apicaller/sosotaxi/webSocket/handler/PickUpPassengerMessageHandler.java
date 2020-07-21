package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.webSocket.message.Message;
import com.apicaller.sosotaxi.webSocket.message.PickUpPassengerMessage;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 *接到乘客消息处理器
 */
public class PickUpPassengerMessageHandler implements MessageHandler<PickUpPassengerMessage>{
    @Override
    public void execute(Session session, PickUpPassengerMessage message) {

    }

    @Override
    public String getMessageType() {
        return PickUpPassengerMessage.TYPE;
    }
}
