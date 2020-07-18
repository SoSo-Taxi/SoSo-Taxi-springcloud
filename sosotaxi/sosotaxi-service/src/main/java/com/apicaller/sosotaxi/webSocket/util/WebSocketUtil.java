package com.apicaller.sosotaxi.webSocket.util;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
public class WebSocketUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketUtil.class);

    // ========== 会话相关 ==========

    /**
     * Session 与用户token的映射
     */
    private static final Map<Session, String> SESSION_USER_MAP = new ConcurrentHashMap<>();
    /**
     * 用户token与 Session 的映射
     */
    private static final Map<String, Session> USER_SESSION_MAP = new ConcurrentHashMap<>();


    /**
     * 已登录司机状态和session的映射
     */
    private static final Map<LoginDriver,Session> LOGIN_DRIVER_SESSION_MAP = new ConcurrentHashMap<>();


    /**
     * 派单后乘客用户名和司机的映射
     */
    private static final Map<String, LoginDriver> USERNAME_LOGIN_DRIVER_MAP = new ConcurrentHashMap<>();


    /**
     * 订单开始时，添加司机和用户的映射
     * @param session
     * @param loginDriver
     */

    public static void bondUserNameAndLoginDriver (Session session, LoginDriver loginDriver)
    {
        String userToken = SESSION_USER_MAP.get(session);
        String usernameByToken = JwtTokenUtils.getUsernameByToken(userToken);
        USERNAME_LOGIN_DRIVER_MAP.put(usernameByToken,loginDriver);
    }

    /**
     * 找到所有可用的司机，返回司机引用
     */
    public static List<LoginDriver> getAllAvailableDrivers()
    {
        List<LoginDriver> availableLoginDrivers = LOGIN_DRIVER_SESSION_MAP.keySet().stream()
                .filter(a -> a.getIsDispatched().equals("no"))
                .collect(Collectors.toList());

        return availableLoginDrivers;
    }


    /**
     * 找到所有未被派遣的司机，返回坐标
     */
    public static List<GeoPoint> getAllAvailableDriverGeo()
    {
        String no = "no";
        List<GeoPoint> geoPointList = LOGIN_DRIVER_SESSION_MAP.keySet().stream()
                .filter(loginDriver -> loginDriver.getIsDispatched().equals(no))
                .map(a ->
                { return new GeoPoint(a.getGeoPoint().getLat(),a.getGeoPoint().getLng()); })
                .collect(Collectors.toList());
        return geoPointList;
    }


    /**
     *
     * @param session
     * @return LoginDriver
     * 根据司机session找到司机
     */

    public static LoginDriver findDriverBySession(Session session)
    {
        LoginDriver loginDriver = new LoginDriver();
        for (LoginDriver key:LOGIN_DRIVER_SESSION_MAP.keySet()) {
            if(session == LOGIN_DRIVER_SESSION_MAP.get(key))
            {
                loginDriver = key;
                break;
            }
        }
        return loginDriver;
    }

    public static Session getSessionByLoginDrier(LoginDriver loginDriver)
    {
        return LOGIN_DRIVER_SESSION_MAP.get(loginDriver);
    }
    /**
     * 添加登陆司机的状态
     */
    public static void addLoginDriver(Session session,LoginDriver loginDriver)
    {
        LOGIN_DRIVER_SESSION_MAP.put(loginDriver, session);

    }


    /**
     * 添加 Session 。在这个方法中，会添加用户和 Session 之间的映射
     *
     * @param session Session
     * @param userToken 用户token
     */
    public static void addSession(Session session, String userToken) {
        // 更新 USER_SESSION_MAP
        USER_SESSION_MAP.put(userToken, session);
        // 更新 SESSION_USER_MAP
        SESSION_USER_MAP.put(session, userToken);
    }

    /**
     * 移除 Session 。
     *
     * @param session Session
     */
    public static void removeSession(Session session) {
        // 从 SESSION_USER_MAP 中移除
        String userToken = SESSION_USER_MAP.remove(session);
        // 从 USER_SESSION_MAP 中移除
        if (userToken != null && userToken.length() > 0) {
            USER_SESSION_MAP.remove(userToken);
        }
    }

    public static <T extends Message> void send(Session session, String type, T message) {
        // 创建消息
        String messageText = buildTextMessage(type, message);
        // 遍历给单个 Session ，进行逐个发送
        sendTextMessage(session, messageText);
    }

    /**
     * 发送消息给指定用户
     *
     * @param userToken 指定用户token
     * @param type 消息类型
     * @param message 消息体
     * @param <T> 消息类型
     * @return 发送是否成功你那个
     */
    public static <T extends Message> boolean send(String userToken, String type, T message) {
        // 获得用户对应的 Session
        Session session = USER_SESSION_MAP.get(userToken);
        if (session == null) {
            LOGGER.error("[send][userToken({}) 不存在对应的 session]", userToken);
            return false;
        }
        // 发送消息
        send(session, type, message);
        return true;
    }

    /**
     * 构建完整的消息
     *
     * @param type 消息类型
     * @param message 消息体
     * @param <T> 消息类型
     * @return 消息
     */
    private static <T extends Message> String buildTextMessage(String type, T message) {
        JSONObject messageObject = new JSONObject();
        messageObject.put("type", type);
        messageObject.put("body", message);
        return messageObject.toString();
    }

    /**
     * 真正发送消息
     *
     * @param session Session
     * @param messageText 消息
     */
    private static void sendTextMessage(Session session, String messageText) {
        if (session == null) {
            LOGGER.error("[sendTextMessage][session 为 null]");
            return;
        }
        RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            LOGGER.error("[sendTextMessage][session 的  为 null]");
            return;
        }
        try {
            basic.sendText(messageText);
        } catch (IOException e) {
            LOGGER.error("[sendTextMessage][session({}) 发送消息{}) 发生异常",
                    session, messageText, e);
        }
    }

}