package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerOrderMessage;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;

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

        Order order = message.getOrder();
        if(message.getTakeOrder())
        {
            DriverVo driver = message.getDriver();
            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            order.setDriverId(driver.getUserId());
            //司机已接单
            order.setStatus(1);


            driverAnswerResponse.setDriver(driver);
            driverAnswerResponse.setStatusCode(200);
            driverAnswerResponse.setMsg("司机已接单");

            String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(order);
            Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);
            WebSocketUtil.send(passengerSessionByToken,DriverAnswerResponse.TYPE,driverAnswerResponse);
            /**
             *
             * 这里还需要做一些事：
             * 1 告知诗烨那边一个司机已经接单，即不可分配了。
             * 2 修改鹰眼上该司机的状态
             * 3 如果LoginDriver中的状态还有用，还要更新LoginDriver中的状态位
             */
        }
        /**
         * 这里应该要改一下，司机拒单则系统自动寻找下一个司机（可能由诗烨那边来做)
         */
        else
        {
            String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(order);
            Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);

            WebSocketUtil.removeOrderUserTokenMap(order,userTokenByOrder);
            WebSocketUtil.removeUserTokenOrderMap(userTokenByOrder,order);
            WebSocketUtil.removeOrderLoginDriverMap(order,loginDriver);
            WebSocketUtil.removeLoginDriverOrderMap(loginDriver,order);
            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            driverAnswerResponse.setMsg("司机拒单");
            driverAnswerResponse.setStatusCode(200);
            driverAnswerResponse.setDriver(null);
            WebSocketUtil.send(passengerSessionByToken,DriverAnswerResponse.TYPE,driverAnswerResponse);

        }

    }

    @Override
    public String getMessageType() {
        return DriverAnswerOrderMessage.TYPE;
    }
}
