package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.webSocket.message.DriverDepartMessage;
import com.apicaller.sosotaxi.webSocket.message.DriverDepartMessageToPassenger;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;

/**
 * 处理司机"开始行程"时发来的消息。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 8:10 下午
 * @updateTime:
 */
public class DriverDepartMessageHandler implements MessageHandler<DriverDepartMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverDepartMessageHandler.class);

    @Override
    public void execute(Session session, DriverDepartMessage message) {
        //先更新一下该订单的出发时间
        if(!WebSocketUtil.updateTimeForOrder(message.getOrder())) {
            LOGGER.info("[为订单{}更新出发时间失败]", message.getOrder().getOrderId());
        }
        WebSocketUtil.updateStatusForOrder(message.getOrder(), 3);

        //然后找到对应的乘客，并给他发消息
        String passengerToken = WebSocketUtil.getUserTokenByOrder(message.getOrder());
        if(passengerToken == null) {
            LOGGER.info("[为订单{}查找对应的乘客失败]", message.getOrder().getOrderId());
            return;
        }

        DriverDepartMessageToPassenger msg = new DriverDepartMessageToPassenger();
        msg.setOrder(message.getOrder());

        if(WebSocketUtil.send(passengerToken, DriverDepartMessageToPassenger.TYPE, msg)) {
            LOGGER.info("[为订单{}发送开始消息到对应的乘客失败]", message.getOrder().getOrderId());
        }
    }

    @Override
    public String getMessageType() {
        return DriverDepartMessage.TYPE;
    }
}
