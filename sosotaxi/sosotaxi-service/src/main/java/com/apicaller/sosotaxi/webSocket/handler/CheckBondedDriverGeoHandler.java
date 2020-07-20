package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.webSocket.message.CheckBondedDriverGeoRequest;
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

    }

    @Override
    public String getMessageType() {
        return CheckBondedDriverGeoRequest.TYPE;
    }
}
