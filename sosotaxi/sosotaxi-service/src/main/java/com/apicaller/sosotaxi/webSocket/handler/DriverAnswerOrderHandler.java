package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.entity.dispatch.response.DriverCarInfoResponse;
import com.apicaller.sosotaxi.feignClients.DriverFeignClient;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import com.apicaller.sosotaxi.utils.YingYanUtil;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerOrderMessage;
import com.apicaller.sosotaxi.webSocket.message.DriverAnswerResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class DriverAnswerOrderHandler implements MessageHandler<DriverAnswerOrderMessage> {
    Logger logger = LoggerFactory.getLogger(StartOrderHandler.class);

    @Resource
    DriverFeignClient driverFeignClient;

    @Override
    public void execute(Session session, DriverAnswerOrderMessage message) {

        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);
        Order order = message.getOrder();
        String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(order);

        //获取真正的订单
        Order realOrder = WebSocketUtil.getOrderByUserToken(userTokenByOrder);
        logger.info(message.getTakeOrder().toString());
        if(message.getTakeOrder())
        {
            loginDriver.setDispatched(true);
            DriverVo driverVo = message.getDriver();
            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            realOrder.setDriverId(driverVo.getUserId());
            //司机已接单
            realOrder.setStatus(1);
            Driver driver = driverFeignClient.getDriverById(driverVo.getUserId());
            if (driver == null) {
                logger.error("未在数据库中找到司机" + driverVo.getUserId());
                return;
            }
            DriverCarInfoResponse driverCarInfoResponse = DriverCarInfoResponse.fromDriver(driver);
            driverAnswerResponse.setDriverCarInfo(driverCarInfoResponse);
            driverAnswerResponse.setStatusCode(200);
            driverAnswerResponse.setMsg("司机已接单");

            Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);
            WebSocketUtil.send(passengerSessionByToken,DriverAnswerResponse.TYPE,driverAnswerResponse);
            logger.info("回应信息发给乘客，乘客token为：" + userTokenByOrder);
            /**
             *
             * 这里还需要做一些事：
             * 1 告知诗烨那边一个司机已经接单，即不可分配了。
             * 2 修改鹰眼上该司机的状态
             *
             */

            YingYanUtil.updateDriver(driver.getUserName(), false);
        }
        /**
         * 这里应该要改一下，司机拒单则系统自动寻找下一个司机（可能由诗烨那边来做)
         */
        else
        {
            Session passengerSessionByToken = WebSocketUtil.getPassengerSessionByToken(userTokenByOrder);

            WebSocketUtil.removeOrderUserTokenMap(order,userTokenByOrder);
            WebSocketUtil.removeUserTokenOrderMap(userTokenByOrder,order);
            WebSocketUtil.removeOrderLoginDriverMap(order,loginDriver);
            WebSocketUtil.removeLoginDriverOrderMap(loginDriver,order);
            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            driverAnswerResponse.setMsg("司机拒单");
            driverAnswerResponse.setStatusCode(200);
            driverAnswerResponse.setDriverCarInfo(null);
            WebSocketUtil.send(passengerSessionByToken,DriverAnswerResponse.TYPE,driverAnswerResponse);

        }

    }

    @Override
    public String getMessageType() {
        return DriverAnswerOrderMessage.TYPE;
    }
}
