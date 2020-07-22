package com.apicaller.sosotaxi.webSocket.handler;


import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.webSocket.message.ArriveDestPointMessage;
import com.apicaller.sosotaxi.webSocket.message.ArriveDestPointMessageToPassenger;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/21 8:07 下午
 * @updateTime:
 */
public class ArriveDestPointMessageHandler implements MessageHandler<ArriveDestPointMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArriveDestPointMessageHandler.class);

    @Override
    public void execute(Session session, ArriveDestPointMessage message) {
        //先更新一下该订单的到达时间
        if(! WebSocketUtil.updateTimeForOrder(message.getOrder())) {
            LOGGER.info("[为订单{}更新到达时间失败]", message.getOrder().getOrderId());
        }

        Order order = WebSocketUtil.getOrderInKeySet(message.getOrder());
        order.setStatus(4);

        double totalCost = (message.getBasicCost() == null ? 0 : message.getBasicCost())
                + (message.getFreewayCost() == null ? 0 : message.getFreewayCost())
                + (message.getParkingCost() == null ? 0 : message.getParkingCost());

        order.setCost(totalCost);

        //然后找到对应的乘客，并给他发消息
        String passengerToken = WebSocketUtil.getUserTokenByOrder(message.getOrder());
        if(passengerToken == null) {
            LOGGER.info("[为订单{}查找对应的乘客失败]", message.getOrder().getOrderId());
            return;
        }

        ArriveDestPointMessageToPassenger msg = new ArriveDestPointMessageToPassenger();
        msg.setOrder(message.getOrder());
        msg.setBasicCost(message.getBasicCost());
        msg.setFreewayCost(message.getFreewayCost());
        msg.setParkingCost(message.getParkingCost());

        if(! WebSocketUtil.send(passengerToken, ArriveDestPointMessageToPassenger.TYPE, msg)) {
            LOGGER.info("[为订单{}发送到达目的地消息到对应的乘客失败]", message.getOrder().getOrderId());
        }
    }

    @Override
    public String getMessageType() {
        return ArriveDestPointMessage.TYPE;
    }
}
