package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.utils.JSONUtil;
import com.apicaller.sosotaxi.webSocket.message.Message;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
public interface MessageHandler <T extends Message>{

    /**
     * 执行消息发送
     * @param session
     * @param message
     */
    public void execute (Session session, T message);


    /**
     * 获得消息类型
     * @return String
     */
    public String getMessageType();
}
