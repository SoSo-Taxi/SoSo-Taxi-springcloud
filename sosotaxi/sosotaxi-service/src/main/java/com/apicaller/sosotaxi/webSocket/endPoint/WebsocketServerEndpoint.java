package com.apicaller.sosotaxi.webSocket.endPoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.webSocket.handler.AuthRequestHandler;
import com.apicaller.sosotaxi.webSocket.handler.MessageHandler;
import com.apicaller.sosotaxi.webSocket.message.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@Controller
@ServerEndpoint("/webSocket")
public class WebsocketServerEndpoint implements InitializingBean {


    private static final Map<String, MessageHandler> HANDLERS = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }
    private Logger logger = LoggerFactory.getLogger(getClass());


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        List<String> accessTokenValues = session.getRequestParameterMap().get("accessToken");
        String accessToken = !CollectionUtils.isEmpty(accessTokenValues) ? accessTokenValues.get(0) : null;
        // 创建 AuthRequest 消息类型
        AuthRequest authRequest = new AuthRequest().setAccessToken(accessToken);
        // 获得消息处理器
        MessageHandler<AuthRequest> messageHandler = new AuthRequestHandler();

        logger.info("[onOpen][session({}) 接收到一个token({})]",session,accessToken);
        messageHandler.execute(session, authRequest);

    }

    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("[onOpen][session({}) 接收到一条消息({})]", session, message);

        JSONObject jsonMessage = JSON.parseObject(message);
        String messageType = jsonMessage.getString("type");
        // 获得消息处理器
        MessageHandler messageHandler = HANDLERS.get(messageType);
        if (messageHandler == null) {
            logger.error("[onMessage][消息类型({}) 不存在消息处理器]", messageType);
            return;
        }
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
