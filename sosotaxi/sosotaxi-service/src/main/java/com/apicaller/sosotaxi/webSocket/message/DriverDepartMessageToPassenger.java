package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * 司机接到乘客并出发时，服务器发给乘客的消息
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 7:34 下午
 * @updateTime:
 */

@Data
public class DriverDepartMessageToPassenger implements Message {
    public static final String TYPE = "DRIVER_DEPART_MESSAGE_TO_PASSENGER";
    private Order order;
}
