package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * 司机接到乘客并出发时，发给服务器的消息
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 7:33 下午
 * @updateTime:
 */

@Data
public class DriverDepartMessage implements Message{
    public static final String TYPE = "DRIVER_DEPART_MESSAGE";
    private Order order;
}
