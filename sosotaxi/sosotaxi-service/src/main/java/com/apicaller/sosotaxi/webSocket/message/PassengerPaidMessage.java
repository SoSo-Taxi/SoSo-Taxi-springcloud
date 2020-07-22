package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/21 11:37 下午
 * @updateTime:
 */
@Data
public class PassengerPaidMessage implements Message {
    public static final String TYPE = "PASSENGER_PAID_MESSAGE";

    private Order order;

    private Boolean isPaid;
}
