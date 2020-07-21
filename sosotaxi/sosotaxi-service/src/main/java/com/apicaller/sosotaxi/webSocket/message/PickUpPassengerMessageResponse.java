package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机发送接到乘客后，服务器返回给司机的消息
 */
@Data
public class PickUpPassengerMessageResponse implements Message {
    public static final String TYPE = "PICK_UP_PASSENGER_MESSAGE_RESPONSE";

    private Order order;

    private String msg;

    private int statusCode;
}
