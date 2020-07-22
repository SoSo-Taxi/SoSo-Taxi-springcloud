package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.webSocket.message.PassengerPaidMessage;

import javax.websocket.Session;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/22 2:37 下午
 * @updateTime:
 */
public class PassengerPaidMessageHandler implements MessageHandler<PassengerPaidMessage> {
    @Override
    public void execute(Session session, PassengerPaidMessage message) {

    }

    @Override
    public String getMessageType() {
        return PassengerPaidMessage.TYPE;
    }
}
