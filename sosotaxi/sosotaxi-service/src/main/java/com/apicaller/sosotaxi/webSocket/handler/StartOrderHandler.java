package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import com.apicaller.sosotaxi.webSocket.message.AskForDriverMessage;
import com.apicaller.sosotaxi.webSocket.message.StartOrderMessage;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    OrderFeignClient orderFeignClient;

    Logger logger = LoggerFactory.getLogger(StartOrderHandler.class);

    @Override
    public void execute(Session session, StartOrderMessage message) {

        GeoPoint departGeoPoint = message.getDepartPoint();
        GeoPoint destGeoPoint = message.getDestPoint();
        Short serviceType = message.getServiceType();

        //新建订单
        Order order = new Order();
        order.setPassengerId(message.getPassengerId());
        order.setCreateTime(new Date());
        //设置订单状态，0代表还未开始
        order.setStatus(0);
        order.setCity(message.getCity());
        order.setDepartPoint(departGeoPoint);
        order.setDestPoint(destGeoPoint);
        order.setDepartName(message.getDepartName());
        order.setDestName(message.getDestName());
        order.setServiceType(serviceType);

        //先将订单订单插入数据库，以获取订单id
        orderFeignClient.addOrder(order);

        //设置给司机的消息
        AskForDriverMessage askForDriverMessage = new AskForDriverMessage();
        askForDriverMessage.getOrder().setCity(message.getCity());
        askForDriverMessage.setPassengerPhoneNumber(message.getPhoneNumber());
        askForDriverMessage.setOrder(order);

        List<LoginDriver> availableDrivers = WebSocketUtil.getAllAvailableDrivers();

        logger.info("未筛选所有可用司机{}",availableDrivers);


        List<LoginDriver> fitTypeDrivers = availableDrivers.stream()
                .filter(a -> a.getServiceType()==(message.getServiceType()))
                .collect(Collectors.toList());


        logger.info("筛选后所有可用司机{}",fitTypeDrivers);

        /**
         * 在这里调用派单算法
         */


        //添加到map中
        WebSocketUtil.addUserTokenOrderMap(message.getUserToken(),order);
        WebSocketUtil.addOrderUserTokenMap(order,message.getUserToken());
        WebSocketUtil.addLoginDriverOrderMap(fitTypeDrivers.get(0),order);
        WebSocketUtil.addOrderLoginDriverMap(order,fitTypeDrivers.get(0));
        Session driverSession = WebSocketUtil.getSessionByLoginDriver(fitTypeDrivers.get(0));
        WebSocketUtil.send(driverSession,AskForDriverMessage.TYPE,askForDriverMessage);

    }

    @Override
    public String getMessageType() {
        return StartOrderMessage.TYPE;
    }

}
