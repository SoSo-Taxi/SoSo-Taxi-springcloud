package com.apicaller.sosotaxi.webSocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.entity.dispatch.response.DriverCarInfoResponse;
import com.apicaller.sosotaxi.entity.dispatchservice.MinimizedDriver;
import com.apicaller.sosotaxi.entity.dispatchservice.UnsettledOrder;
import com.apicaller.sosotaxi.entity.dispatchservice.message.OpsForOrderMsg;
import com.apicaller.sosotaxi.feignClients.DispatchFeignClient;
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
    Logger logger = LoggerFactory.getLogger(DriverAnswerOrderHandler.class);

    @Resource
    DriverFeignClient driverFeignClient;

    @Resource
    OrderFeignClient orderFeignClient;

    @Resource
    DispatchFeignClient dispatchFeignClient;

    @Override
    public void execute(Session session, DriverAnswerOrderMessage message) {

        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);
        Order order = message.getOrder();
        String userTokenByOrder = WebSocketUtil.getUserTokenByOrder(order);
        if(userTokenByOrder == null) {
            logger.error("订单{}的乘客token未找到", order.getOrderId());
            return;
        }
        //获取真正的订单
        Order realOrder = WebSocketUtil.getOrderByUserToken(userTokenByOrder);
        logger.info(message.getTakeOrder().toString());
        if(message.getTakeOrder())
        {
            loginDriver.setDispatched(true);
            DriverVo driverVo = message.getDriver();
            DriverAnswerResponse driverAnswerResponse = new DriverAnswerResponse();
            //司机已接单
            realOrder.setStatus(1);
            Driver driver = driverFeignClient.getDriverById(driverVo.getUserId());
            if (driver == null) {
                logger.error("未在数据库中找到司机" + driverVo.getUserId());
                return;
            }
            realOrder.setDriverId(driverVo.getUserId());
            DriverCarInfoResponse driverCarInfoResponse = DriverCarInfoResponse.fromDriver(driver);
            driverCarInfoResponse.setRate(orderFeignClient.getDriverAvgRate(driverVo.getUserId()));
            driverCarInfoResponse.setOrderNum(orderFeignClient.getDriverOrderNum(driverVo.getUserId()));
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
            ///
            JSONObject jsonObject = YingYanUtil.getLatestPoint(driver.getUserName());
            double lat = jsonObject.getJSONObject("latest_point").getDouble("latitude");
            double lng = jsonObject.getJSONObject("latest_point").getDouble("longitude");
            GeoPoint point = new GeoPoint(lat,lng);
            OpsForOrderMsg oMsg = new OpsForOrderMsg(point, driver.getUserName(), order.getOrderId().toString());
            //UnsettledOrder uOrder = dispatchFeignClient.accept(oMsg);
            //if(uOrder!=null){
                //TODO:接单成功后的动作
            //}else{
                //TODO:接单失败后的动作
            //}
            ///

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

            ///
            DriverVo driverVo = message.getDriver();
            Driver driver = driverFeignClient.getDriverById(driverVo.getUserId());
            JSONObject jsonObject = YingYanUtil.getLatestPoint(driver.getUserName());
            double lat = jsonObject.getJSONObject("latest_point").getDouble("latitude");
            double lng = jsonObject.getJSONObject("latest_point").getDouble("longitude");
            GeoPoint point = new GeoPoint(lat,lng);
            OpsForOrderMsg oMsg = new OpsForOrderMsg(point, driver.getUserName(), order.getOrderId().toString());
//            MinimizedDriver anotherDriver = dispatchFeignClient.refuseOrder(oMsg);
//            if(anotherDriver == null){
//                //TODO:通知乘客该段时间暂无可用司机
//            }else{
//                //TODO:通知该司机
//            }
            //无论是否成功接单。都需要司机重新听单
            YingYanUtil.updateDriver(driver.getUserName(), false);
            ///
        }

    }

    @Override
    public String getMessageType() {
        return DriverAnswerOrderMessage.TYPE;
    }
}
