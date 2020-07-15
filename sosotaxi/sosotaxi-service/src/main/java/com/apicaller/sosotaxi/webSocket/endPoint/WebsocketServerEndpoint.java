package com.apicaller.sosotaxi.webSocket.endPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@Controller
@ServerEndpoint("/webSocket")
public class WebsocketServerEndpoint implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }
    private Logger logger = LoggerFactory.getLogger(getClass());


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.info("[onOpen][session({}) 接入]", session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("[onOpen][session({}) 接收到一条消息({})]", session, message); // 生产环境下，请设置成 debug 级别
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("[onClose][session({}) 连接关闭。关闭原因是({})}]", session, closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("[onClose][session({}) 发生异常]", session, throwable);
    }

}
