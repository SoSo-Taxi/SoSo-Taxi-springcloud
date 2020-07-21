package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机发送接到乘客
 */
@Data
public class PickUpPassengerMessage implements Message{
    public static final String TYPE = "PICK_UP_PASSENGER_MESSAGE";

    private Order order;

}
