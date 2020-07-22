package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.utils.BDmapUtil;
import com.apicaller.sosotaxi.webSocket.message.CheckBondedDriverGeoRequest;
import com.apicaller.sosotaxi.webSocket.message.CheckBondedDriverGeoResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/20
 * @updateTime
 * 打到车的用户查询司机位置
 */
@Component
public class CheckBondedDriverGeoHandler implements MessageHandler<CheckBondedDriverGeoRequest> {
    @Override
    public void execute(Session session, CheckBondedDriverGeoRequest message) {

        String tokenByUserSession = WebSocketUtil.getTokenByUserSession(session);
        Order order = WebSocketUtil.getOrderByUserToken(tokenByUserSession);
        LoginDriver loginDriverByOrder = WebSocketUtil.getLoginDriverByOrder(order);
        Double distance = BDmapUtil.calcDistance(message.getGeoPoint(), loginDriverByOrder.getGeoPoint(), null);

        CheckBondedDriverGeoResponse checkBondedDriverGeoResponse = new CheckBondedDriverGeoResponse();
        checkBondedDriverGeoResponse.setGeoPoint(loginDriverByOrder.getGeoPoint());
        checkBondedDriverGeoResponse.setServerType(loginDriverByOrder.getServiceType());
        checkBondedDriverGeoResponse.setStatusCode(200);
        checkBondedDriverGeoResponse.setMsg("查询司机位置成功！");
        checkBondedDriverGeoResponse.setDistance(distance);

        WebSocketUtil.send(session,CheckBondedDriverGeoResponse.TYPE,checkBondedDriverGeoResponse);

    }

    @Override
    public String getMessageType() {
        return CheckBondedDriverGeoRequest.TYPE;
    }

}


