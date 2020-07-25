package com.apicaller.sosotaxi.webSocket.endPoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.webSocket.handler.AuthRequestHandler;
import com.apicaller.sosotaxi.webSocket.handler.MessageHandler;
import com.apicaller.sosotaxi.webSocket.message.AuthRequest;
import com.apicaller.sosotaxi.webSocket.message.ErrorResponse;
import com.apicaller.sosotaxi.webSocket.message.Message;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@Controller
@ServerEndpoint("/webSocket")
public class WebsocketServerEndpoint implements InitializingBean {


    private static final Map<String, MessageHandler> HANDLERS = new HashMap<>();

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MessageHandler.class).values()
                // 获得所有 MessageHandler Bean
                .forEach(messageHandler -> HANDLERS.put(messageHandler.getMessageType(), messageHandler));
        // 添加到 handlers 中
        logger.info("[afterPropertiesSet][消息处理器数量：{}]", HANDLERS.size());

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
        logger.info("[onMessage][当前时间{} session({}) 接收到一条消息({})]", new Date(),session, message);

        JSONObject jsonMessage = null;
        try {
            jsonMessage = JSON.parseObject(message);
        }
        catch (Exception e) {
            logger.info("parse message错误，请检查json");
            return;
        }

        String messageType = jsonMessage.getString("type");
        // 获得消息处理器
        MessageHandler messageHandler = HANDLERS.get(messageType);
        if (messageHandler == null) {
            logger.error("[onMessage][消息类型({}) 不存在消息处理器]", messageType);
            return;
        }
        // 解析消息
        Class<? extends Message> messageClass = this.getMessageClass(messageHandler);
        // 处理消息

        Message messageObj;
        try {
            messageObj = JSON.parseObject(jsonMessage.getString("body"), messageClass);
        }
        catch (Exception e) {
            logger.info("parse body错误，请检查json");
            return;
        }
        messageHandler.execute(session, messageObj);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("[onClose][session({}) 连接关闭。关闭原因是({})}]", session, closeReason);
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setMsg("你已关闭连接");
//        errorResponse.setStatusCode(201);
//        WebSocketUtil.send(session,ErrorResponse.TYPE,errorResponse);
//        WebSocketUtil.removeOrder(session);
        WebSocketUtil.removeOrder(session);
        WebSocketUtil.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("[onClose][session({}) 发生异常]", session, throwable);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMsg("发生异常，请重头再来");
        errorResponse.setStatusCode(201);
        WebSocketUtil.send(session,ErrorResponse.TYPE,errorResponse);
        WebSocketUtil.removeOrder(session);

    }


    private Class<? extends Message> getMessageClass(MessageHandler handler) {
        // 获得 Bean 对应的 Class 类名。因为有可能被 AOP 代理过。
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        // 获得接口的 Type 数组
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();
            // 此处，是以父类的接口为准
        while ((Objects.isNull(interfaces) || 0 == interfaces.length) && Objects.nonNull(superclass)) {
            interfaces = superclass.getGenericInterfaces();
            superclass = targetClass.getSuperclass();
        }
        if (Objects.nonNull(interfaces)) {
            // 遍历 interfaces 数组
            for (Type type : interfaces) {
                // 要求 type 是泛型参数
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    // 要求是 MessageHandler 接口
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        // 取首个元素
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                            return (Class<Message>) actualTypeArguments[0];
                        } else {
                            throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
                        }
                    }
                }
            }
        }
        throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
    }
}
