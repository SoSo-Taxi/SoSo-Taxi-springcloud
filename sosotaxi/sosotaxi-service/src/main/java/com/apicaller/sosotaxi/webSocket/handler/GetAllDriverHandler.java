package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.webSocket.message.GetAllDriverMessage;
import com.apicaller.sosotaxi.webSocket.message.GetAllDriverResponse;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.List;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 获取所有可用司机位置
 */
@Component
public class GetAllDriverHandler implements MessageHandler<GetAllDriverMessage> {
    @Override
    public void execute(Session session, GetAllDriverMessage message) {

        List<GeoPoint> geoPoints = WebSocketUtil.getAllAvailableDriverGeo();
        GetAllDriverResponse getAllDriverResponse = new GetAllDriverResponse();
        getAllDriverResponse.setStatusCode(200);
        getAllDriverResponse.setMsg("查询位置成功！");
        getAllDriverResponse.setGeoPoints(geoPoints);

        WebSocketUtil.send(session,GetAllDriverResponse.TYPE,getAllDriverResponse);
    }

    @Override
    public String getMessageType() {
        return GetAllDriverMessage.TYPE;
    }
}
