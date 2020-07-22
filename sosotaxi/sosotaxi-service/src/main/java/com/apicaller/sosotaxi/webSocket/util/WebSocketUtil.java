package com.apicaller.sosotaxi.webSocket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.webSocket.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
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
     * session和已登录司机的映射
     */
    private static final Map<Session,LoginDriver> SESSION_LOGIN_DRIVER_MAP = new ConcurrentHashMap<>();


    /**
     * 司机和订单的映射
     */
    private static final Map<LoginDriver,Order> LOGIN_DRIVER_ORDER_MAP = new ConcurrentHashMap<>();

    /**
     * 订单和司机的映射
     */
    private static final Map<Order,LoginDriver> ORDER_LOGIN_DRIVER_MAP = new ConcurrentHashMap<>();


    /**
     * 订单和用户的映射
     */
    private static final Map<Order,String> ORDER_USER_TOKEN_MAP = new ConcurrentHashMap<>();

    /**
     * 派单后用户与生成订单的映射
     */
    private static final Map<String, Order> USER_TOKEN_ORDER_MAP = new ConcurrentHashMap<>();


    public static void addOrderUserTokenMap(Order order,String userToken) { ORDER_USER_TOKEN_MAP.put(order,userToken); }
    public static void removeOrderUserTokenMap(Order order,String userToken){ORDER_USER_TOKEN_MAP.remove(order,userToken);}
    public static String getUserTokenByOrder(Order order){return ORDER_USER_TOKEN_MAP.get(order);}
    public static void addOrderLoginDriverMap(Order order,LoginDriver loginDriver){ORDER_LOGIN_DRIVER_MAP.put(order, loginDriver);}
    public static void removeOrderLoginDriverMap(Order order,LoginDriver loginDriver){ORDER_LOGIN_DRIVER_MAP.remove(order, loginDriver);}
    public static LoginDriver getLoginDriverByOrder(Order order){return ORDER_LOGIN_DRIVER_MAP.get(order);}



    /**
     * 为一个订单更新起始时间和结束时间
     * @param order
     * @return
     */
    public static boolean updateTimeForOrder(Order order) {

        Order targetOrder = getOrderInKeySet(order);
        if(targetOrder == null) {
            return false;
        }
        targetOrder.setDepartTime(order.getDepartTime());
        targetOrder.setArriveTime(order.getArriveTime());
        return true;
    }

    /**
     * 为一个订单更新状态
     * @param order
     * @return
     */
    public static boolean updateStatusForOrder(Order order, int status) {

        Order targetOrder = getOrderInKeySet(order);
        if(targetOrder == null) {
            return false;
        }
        targetOrder.setStatus(status);
        return true;
    }

    /**
     * 在所有order中，找出事实上与给定order相同的那份order的引用
     * @param order
     * @return
     */
    public static Order getOrderInKeySet(Order order) {
        Set<Order> orderSet = ORDER_USER_TOKEN_MAP.keySet();
        Order targetOrder = null;

        for (Order key : orderSet) {
            if (key.hashCode() == order.hashCode()) {
                targetOrder = key;
            }
        }
        return targetOrder;
    }

    public static void removeOrder(Session session)
    {
        String tokenByUserSession = WebSocketUtil.getTokenByUserSession(session);
        LoginDriver loginDriverBySession = WebSocketUtil.getLoginDriverBySession(session);

        if (tokenByUserSession!=null)
        {
            Order orderByUserToken = WebSocketUtil.getOrderByUserToken(tokenByUserSession);
            LoginDriver loginDriver = ORDER_LOGIN_DRIVER_MAP.get(orderByUserToken);


            ORDER_LOGIN_DRIVER_MAP.remove(orderByUserToken,loginDriver);
            LOGIN_DRIVER_ORDER_MAP.remove(loginDriver,orderByUserToken);
            ORDER_USER_TOKEN_MAP.remove(orderByUserToken,tokenByUserSession);
            USER_TOKEN_ORDER_MAP.remove(tokenByUserSession);
        }

        if (loginDriverBySession!=null)
        {
            Order order = LOGIN_DRIVER_ORDER_MAP.get(loginDriverBySession);
            String passengerToken = ORDER_USER_TOKEN_MAP.get(order);

            ORDER_USER_TOKEN_MAP.remove(order,passengerToken);
            USER_TOKEN_ORDER_MAP.remove(passengerToken,order);
            LOGIN_DRIVER_ORDER_MAP.remove(loginDriverBySession,order);
            ORDER_LOGIN_DRIVER_MAP.remove(order,loginDriverBySession);
        }

    }

    /**
     * 订单生成时，添加用户和订单的映射
     * @param userToken
     * @param order
     */
    public static void addUserTokenOrderMap(String userToken,Order order)
    {
        USER_TOKEN_ORDER_MAP.put(userToken,order);
    }
    public static void removeUserTokenOrderMap(String userToken,Order order) { USER_TOKEN_ORDER_MAP.remove(userToken,order); }

    /**
     * 订单生成时，添加司机和订单的映射
     * @param loginDriver
     * @param order
     */
    public static void addLoginDriverOrderMap(LoginDriver loginDriver,Order order) { LOGIN_DRIVER_ORDER_MAP.put(loginDriver, order); }
    public static void removeLoginDriverOrderMap(LoginDriver loginDriver,Order order) { LOGIN_DRIVER_ORDER_MAP.remove(loginDriver, order); }


    public static Order getOrderByLoginDriver(LoginDriver driver)
    {
        return LOGIN_DRIVER_ORDER_MAP.get(driver);
    }

    /**
     *
     * @param userToken
     * @return order对应的订单
     */
    public static Order getOrderByUserToken(String userToken)
    {
        return USER_TOKEN_ORDER_MAP.get(userToken);
    }



    /**
     * 找到所有可用的司机，返回司机引用
     */
    public static List<LoginDriver> getAllAvailableDrivers()
    {


        List<LoginDriver> loginDrivers = new ArrayList<>(LOGIN_DRIVER_SESSION_MAP.keySet());

        LOGGER.info("[所有司机:{}]",loginDrivers);


        List<LoginDriver> availableLoginDrivers = LOGIN_DRIVER_SESSION_MAP.keySet().stream()
                .filter(a -> (!a.isDispatched() && a.isStartListening()))
                .collect(Collectors.toList());

        LOGGER.info("[可用司机状况{}]",availableLoginDrivers);

        return LOGIN_DRIVER_SESSION_MAP.keySet().stream()
                .filter(a -> (!a.isDispatched()&&a.isStartListening()))
                .collect(Collectors.toList());
    }


    public static Session getPassengerSessionByToken(String token)
    {
        return USER_SESSION_MAP.get(token);
    }

    /**
     * 找到所有未被派遣的司机，返回坐标
     */
    public static List<GeoPoint> getAllAvailableDriverGeo()
    {
        return LOGIN_DRIVER_SESSION_MAP.keySet().stream()
                .filter(loginDriver -> (!loginDriver.isDispatched()&&loginDriver.isStartListening()))
                .map(a ->
                {  return new GeoPoint(a.getGeoPoint().getLat(),a.getGeoPoint().getLng()); })
                .collect(Collectors.toList());

    }


    /**
     *
     * @param session
     * @return LoginDriver
     * 根据session获取登陆司机
     */

    public static LoginDriver getLoginDriverBySession(Session session)
    {
        return SESSION_LOGIN_DRIVER_MAP.get(session);
    }

    public static Session getSessionByLoginDriver(LoginDriver loginDriver)
    {
        LOGGER.info("LOGIN_DRIVER_SESSION_MAP.containsKey(loginDriver){}",LOGIN_DRIVER_SESSION_MAP.containsKey(loginDriver));
        return LOGIN_DRIVER_SESSION_MAP.get(loginDriver);

    }
    /**
     * 添加登陆司机的状态
     *
     */
    public static void addLoginDriver(Session session,LoginDriver loginDriver)
    {
        LOGIN_DRIVER_SESSION_MAP.put(loginDriver, session);

        SESSION_LOGIN_DRIVER_MAP.put(session, loginDriver);
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

    public static String  getTokenByUserSession(Session session)
    {
        return SESSION_USER_MAP.get(session);
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

        Map<String, Object> messageObject = new HashMap<>();
        messageObject.put("type", type);
        messageObject.put("body", message);
        return JSON.toJSONString(messageObject, SerializerFeature.UseISO8601DateFormat);
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
