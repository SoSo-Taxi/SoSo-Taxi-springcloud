package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.webSocket.message.Message;
import com.apicaller.sosotaxi.webSocket.message.PickUpPassengerMessage;
import com.apicaller.sosotaxi.webSocket.message.PickUpPassengerMessageResponse;
import com.apicaller.sosotaxi.webSocket.message.PickUpPassengerMessageToPassenger;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 *接到乘客消息处理器
 */
@Component
public class PickUpPassengerMessageHandler implements MessageHandler<PickUpPassengerMessage>{
    @Override
    public void execute(Session session, PickUpPassengerMessage message) {
        Order order = message.getOrder();
        String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(order);
        Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);

        Order realOrder = WebSocketUtil.getOrderByUserToken(userTokenByOrder);
        realOrder.setStatus(3);

//        PickUpPassengerMessageResponse pickUpPassengerMessageResponse = new PickUpPassengerMessageResponse();
//        pickUpPassengerMessageResponse.setOrder(realOrder);
//        pickUpPassengerMessageResponse.setStatusCode(200);
//        pickUpPassengerMessageResponse.setMsg("您已经成功接到乘客");
//
//        WebSocketUtil.send(session,PickUpPassengerMessageResponse.TYPE,pickUpPassengerMessageResponse);


        PickUpPassengerMessageToPassenger pickUpPassengerMessageToPassenger = new PickUpPassengerMessageToPassenger();
        pickUpPassengerMessageToPassenger.setMsg("司机已经接到您了");
        pickUpPassengerMessageToPassenger.setStatusCode(200);

        WebSocketUtil.send(passengerSessionByToken,PickUpPassengerMessageToPassenger.TYPE,pickUpPassengerMessageToPassenger);



    }

    @Override
    public String getMessageType() {
        return PickUpPassengerMessage.TYPE;
    }
}
