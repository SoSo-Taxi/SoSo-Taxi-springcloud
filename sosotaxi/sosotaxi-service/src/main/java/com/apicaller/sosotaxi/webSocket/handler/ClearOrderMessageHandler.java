package com.apicaller.sosotaxi.webSocket.handler;

import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.webSocket.message.ClearOrderMessage;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/24 12:03 上午
 * @updateTime:
 */
@Component
public class ClearOrderMessageHandler implements MessageHandler<ClearOrderMessage> {
    @Override
    public void execute(Session session, ClearOrderMessage message) {
        LoginDriver loginDriver = WebSocketUtil.getLoginDriverBySession(session);
        if(loginDriver == null) {
            return;
        }
        loginDriver.setDispatched(false);
        loginDriver.setStartListening(true);
        WebSocketUtil.removeOrder(session);
    }

    @Override
    public String getMessageType() {
        return ClearOrderMessage.TYPE;
    }
}
