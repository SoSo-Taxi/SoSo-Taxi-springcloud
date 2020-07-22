package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

import java.util.Date;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 *  发送开始订单后，在处理开始订单消息时把这个消息发送给对应的司机
 */

@Data
public class AskForDriverMessage implements Message {
    public static final String TYPE = "ASK_FOR_DRIVER_MESSAGE";

    private String passengerPhoneNumber;

    private Order order;

}
