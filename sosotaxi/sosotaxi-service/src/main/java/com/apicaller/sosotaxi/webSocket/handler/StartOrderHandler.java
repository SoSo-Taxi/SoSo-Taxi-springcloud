package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.webSocket.message.AskForDriverMessage;
import com.apicaller.sosotaxi.webSocket.message.StartOrderMessage;
import com.apicaller.sosotaxi.webSocket.message.StartOrderResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 */
@Component
public class StartOrderHandler implements MessageHandler<StartOrderMessage> {
    @Override
    public void execute(Session session, StartOrderMessage message) {

        GeoPoint departGeoPoint = message.getDepartPoint();
        GeoPoint destGeoPoint = message.getDestPoint();


        List<LoginDriver> allAvailableDrivers = WebSocketUtil.getAllAvailableDrivers();
        Optional<LoginDriver> nearestAvailableDriver = allAvailableDrivers.stream().min(Comparator.comparing(x -> calculateDistance(x.getGeoPoint(), departGeoPoint)));

        LoginDriver loginDriver = new LoginDriver();
        if (nearestAvailableDriver.isPresent()) {
            loginDriver = nearestAvailableDriver.get();
        }

        Session driverSession = WebSocketUtil.getSessionByLoginDrier(loginDriver);

        AskForDriverMessage askForDriverMessage = new AskForDriverMessage();
        askForDriverMessage.setDepartGeoPoint(departGeoPoint);
        askForDriverMessage.setDestGeoPoint(destGeoPoint);
        askForDriverMessage.setUserToken(message.getToken());


        WebSocketUtil.send(driverSession,AskForDriverMessage.TYPE,askForDriverMessage);
        StartOrderResponse startOrderResponse = new StartOrderResponse();
        startOrderResponse.setMsg("派单成功，请等待");
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
