package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.webSocket.message.FinishOrderRequest;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 完成订单处理器
 */
@Component
public class FinishOrderHandler implements MessageHandler<FinishOrderRequest> {
    @Override
    public void execute(Session session, FinishOrderRequest message) {
        
    }

    @Override
    public String getMessageType() {
        return FinishOrderRequest.TYPE;
    }
}
