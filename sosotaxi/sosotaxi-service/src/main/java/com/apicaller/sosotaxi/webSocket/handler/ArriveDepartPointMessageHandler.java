package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.webSocket.message.ArriveDepartPointMessage;
import com.apicaller.sosotaxi.webSocket.message.ArriveDepartPointResponse;
import com.apicaller.sosotaxi.webSocket.message.ArriveDepartPointToPassenger;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ArriveDepartPointMessageHandler.class);

    @Override
    public void execute(Session session, ArriveDepartPointMessage message) {

        String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(message.getOrder());
        if(userTokenByOrder == null) {
            LOGGER.error("[到达出发点]订单{}的乘客token未找到", message.getOrder().getOrderId());
            return;
        }
        Order order = WebSocketUtil.getOrderByUserToken(userTokenByOrder);
        if(order == null) {
            LOGGER.error("[到达出发点]订单{}未找到", message.getOrder().getOrderId());
            return;
        }

        order.setStatus(2);
        ArriveDepartPointResponse arriveDepartPointResponse = new ArriveDepartPointResponse();
        arriveDepartPointResponse.setMsg("您已到达上车点");
        arriveDepartPointResponse.setOrder(order);
        arriveDepartPointResponse.setStatusCode(200);

        WebSocketUtil.send(session,ArriveDepartPointResponse.TYPE,arriveDepartPointResponse);

        ArriveDepartPointToPassenger arriveDepartPointToPassenger = new ArriveDepartPointToPassenger();
        arriveDepartPointToPassenger.setMsg("司机已到达上车点");
        arriveDepartPointToPassenger.setStatusCode(200);

        Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);
        WebSocketUtil.send(passengerSessionByToken,ArriveDepartPointToPassenger.TYPE,arriveDepartPointToPassenger);

    }

    @Override
    public String getMessageType() {
        return ArriveDepartPointMessage.TYPE;
    }
}
