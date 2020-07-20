package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.webSocket.message.AskForDriverMessage;
import com.apicaller.sosotaxi.webSocket.message.StartOrderMessage;
import com.apicaller.sosotaxi.webSocket.message.StartOrderResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 用户请求打车
 */
@Component
public class StartOrderHandler implements MessageHandler<StartOrderMessage> {

    Logger logger = LoggerFactory.getLogger(StartOrderHandler.class);

    @Override
    public void execute(Session session, StartOrderMessage message) {

        GeoPoint departGeoPoint = message.getDepartPoint();
        GeoPoint destGeoPoint = message.getDestPoint();
        Order order = new Order();
        Date date = new Date();
        int serverType = message.getServiceType();

        order.setPassengerId(message.getPassengerId());
        order.setCreateTime(date);



        AskForDriverMessage askForDriverMessage = new AskForDriverMessage();
        askForDriverMessage.setDestPoint(destGeoPoint);
        askForDriverMessage.setDepartPoint(departGeoPoint);
        askForDriverMessage.setCity(message.getCity());
        askForDriverMessage.setDepartTime(date);
        askForDriverMessage.setPassengerName(message.getUserName());
        askForDriverMessage.setPassengerNum(message.getPassengerNum());
        askForDriverMessage.setOrder(order);

        List<LoginDriver> availableDrivers = WebSocketUtil.getAllAvailableDrivers();

        List<LoginDriver> fitTypeDrivers = availableDrivers.stream()
                .filter(a -> a.getServerType() == message.getServiceType())
                .collect(Collectors.toList());


        logger.info("所有可用司机{}",fitTypeDrivers);

        /**
         * 在这里调用派单算法
         */

        Session driverSession = WebSocketUtil.getSessionByLoginDriver(fitTypeDrivers.get(0));
        WebSocketUtil.send(driverSession,AskForDriverMessage.TYPE,askForDriverMessage);


    }

    @Override
    public String getMessageType() {
        return StartOrderMessage.TYPE;
    }

    public static double calculateDistance(GeoPoint a, GeoPoint b)
    {
        return 0;
    }
}
