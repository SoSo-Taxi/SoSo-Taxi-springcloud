package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLoginMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLogoutMsg;
import com.apicaller.sosotaxi.feignClients.DispatchFeignClient;
import com.apicaller.sosotaxi.utils.BDmapUtil;
import com.apicaller.sosotaxi.utils.JwtTokenUtils;
import com.apicaller.sosotaxi.utils.YingYanUtil;
import com.apicaller.sosotaxi.webSocket.message.UpdateRequest;
import com.apicaller.sosotaxi.webSocket.message.UpdateResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 * 处理更新坐标请求
 */
@Component
public class UpdateRequestHandler implements MessageHandler<UpdateRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRequestHandler.class);

    @Resource
    DispatchFeignClient dispatchFeignClient;


    @Override
    public void execute(Session session, UpdateRequest message) {
        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);

        LOGGER.info("[接入session{}]",session);
        LOGGER.info("[司机{} ]\"",loginDriver);

        loginDriver.getGeoPoint().setLat(message.getLat());
        loginDriver.getGeoPoint().setLng(message.getLng());
        loginDriver.setDispatched(message.isDispatched());
        loginDriver.setServiceType(message.getServiceType());
        //更新鹰眼状态
        if(message.isStartListening() && !loginDriver.isStartListening()) {
            YingYanUtil.updateDriver(loginDriver.getUserName(), true);
            //开始司机的听单状态
            DriverLoginMsg liMsg = new DriverLoginMsg(loginDriver.getGeoPoint(),"NL",loginDriver.getServiceType(),loginDriver.getUserName());
            dispatchFeignClient.login(liMsg);
        }
        if(! message.isStartListening() && loginDriver.isStartListening()) {
            YingYanUtil.updateDriver(loginDriver.getUserName(), false);
            //取消司机的听单状态
            DriverLogoutMsg loMsg = new DriverLogoutMsg(loginDriver.getUserName());
            dispatchFeignClient.logout(loMsg);
        }
        loginDriver.setStartListening(message.isStartListening());

        /**
         * 这里需要：
         * 1 更新鹰眼服务中的状态
         * 2 更新诗烨那边的状态
         */

        LOGGER.info("[司机{}更新状态 {}]\"",JwtTokenUtils.getUsernameByToken(loginDriver.getToken()),loginDriver);
        LOGGER.info("[当前所有司机状态{}]\"",WebSocketUtil.getAllAvailableDrivers());

        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setMessageId(message.getMessageId());
        updateResponse.setStatusCode(200);
        updateResponse.setMsg("更新位置和接单状态成功");
        WebSocketUtil.send(session,UpdateResponse.TYPE,updateResponse);


    }

    @Override
    public String getMessageType() {
        return UpdateRequest.TYPE;
    }


    @Test
    public void myTest()
    {
        GeoPoint geoPoint = new GeoPoint();
        GeoPoint geoPoint1 = new GeoPoint();
        geoPoint.setLat(39.939059);
        geoPoint.setLng(116.431002);

        geoPoint1.setLng(116.419217);
        geoPoint1.setLat(39.886373);

        System.out.println("距离"+BDmapUtil.calcDistance(geoPoint,geoPoint1,null));
    }
}
