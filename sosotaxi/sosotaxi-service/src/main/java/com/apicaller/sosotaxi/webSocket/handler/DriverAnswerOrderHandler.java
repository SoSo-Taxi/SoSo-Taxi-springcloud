package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerOrderMessage;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.Date;

/**
 * @author 张流潇潇
 * @createTime 2020/7/18
 * @updateTime
 * 司机回应订单
 * 如果回应接单则产生订单
 */
@Component
public class DriverAnswerOrderHandler implements MessageHandler<DriverAnswerOrderMessage>{
    @Resource
    OrderFeignClient orderFeignClient;

    @Resource
    UserServiceFeignClient userServiceFeignClient;

    @Override
    public void execute(Session session, DriverAnswerOrderMessage message) {
        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);

        if(message.getTakeOrder())
        {
            String token = message.getToken();
            String userName = JwtTokenUtils.getUsernameByToken(token);
            String driverName = loginDriver.getUserName();

            Order order = new Order();
            order.setCreateTime(new Date());
            Session userSession = WebSocketUtil.getPassengerSessionByToken(token);

            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            driverAnswerResponse.setDriver(message.getDriver());
            User passengerInfo = userServiceFeignClient.getUserByUserName(userName);
            User driverInfo = userServiceFeignClient.getUserByUserName(driverName);

            order.setDriverId(driverInfo.getUserId());
            order.setPassengerId(passengerInfo.getUserId());

            orderFeignClient.addOrder(order);

            driverAnswerResponse.setDriver(message.getDriver());


            WebSocketUtil.send(userSession,DriverAnswerResponse.TYPE,driverAnswerResponse);
        }

    }

    @Override
    public String getMessageType() {
        return DriverAnswerOrderMessage.TYPE;
    }
}
