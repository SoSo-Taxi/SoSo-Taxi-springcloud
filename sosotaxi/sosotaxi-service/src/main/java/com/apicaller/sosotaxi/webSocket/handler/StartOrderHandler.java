package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
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
 */
@Component
public class StartOrderHandler implements MessageHandler<StartOrderMessage> {

    Logger logger = LoggerFactory.getLogger(StartOrderHandler.class);

    @Override
    public void execute(Session session, StartOrderMessage message) {

        GeoPoint departGeoPoint = message.getDepartPoint();
        GeoPoint destGeoPoint = message.getDestPoint();

        Set<LoginDriver> loginDrivers = WebSocketUtil.getAllAvailableDrivers();
        Iterator<LoginDriver> iterator = loginDrivers.iterator();

        List<LoginDriver> loginDriverList = new ArrayList<>();

        while (iterator.hasNext())
        {
            LoginDriver loginDriver = iterator.next();
            loginDriverList.add(loginDriver);

            logger.info("loginDriver==loginDriverList.get(0){}",loginDriver==loginDriverList.get(0));
        }


        List<LoginDriver> availableLoginDrivers = loginDriverList.stream()
                .filter(a -> "no".equals(a.getIsDispatched()))
                .collect(Collectors.toList());



        logger.info("所有可用司机{}",availableLoginDrivers);

//        Optional<LoginDriver> nearestAvailableDriver = availableLoginDrivers.stream().min(Comparator.comparing(x -> calculateDistance(x.getGeoPoint(), departGeoPoint)));

        /**
         * 在这里调用派单算法
         */

        Session driverSession = WebSocketUtil.getSessionByLoginDriver(loginDriverList.get(0));

        AskForDriverMessage askForDriverMessage = new AskForDriverMessage();
        askForDriverMessage.setDepartGeoPoint(departGeoPoint);
        askForDriverMessage.setDestGeoPoint(destGeoPoint);
        askForDriverMessage.setUserToken(message.getToken());


        WebSocketUtil.send(driverSession,AskForDriverMessage.TYPE,askForDriverMessage);
        StartOrderResponse startOrderResponse = new StartOrderResponse();
        startOrderResponse.setMsg("派单成功，请等待司机接单");
        startOrderResponse.setStatusCode(200);
        WebSocketUtil.send(session,StartOrderResponse.TYPE,startOrderResponse);
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
