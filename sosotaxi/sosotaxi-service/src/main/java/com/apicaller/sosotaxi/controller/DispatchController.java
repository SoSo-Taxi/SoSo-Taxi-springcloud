package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.dispatch.dto.GenerateOrderDTO;
import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import com.apicaller.sosotaxi.webSocket.message.DispatchDriverMessage;
import com.apicaller.sosotaxi.webSocket.util.WebSocketUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchController {


    @PostMapping("/")
    public ResponseBean callCar(GenerateOrderDTO generateOrderDTO)
    {

        DispatchDriverMessage dispatchDriverMessage = new DispatchDriverMessage();

        /**
         * 根据算法找到loginDriver
         */

        LoginDriver loginDriver = new LoginDriver();
        WebSocketUtil.getSessionByLoginDrier(loginDriver);
        return new ResponseBean();

    }
}
