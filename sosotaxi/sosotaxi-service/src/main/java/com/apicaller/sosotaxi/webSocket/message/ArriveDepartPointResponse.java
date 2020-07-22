package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机发送到达上车点消息,返回给司机
 */
@Data
public class ArriveDepartPointResponse implements Message {
    public static final String TYPE = "ARRIVE_DEPART_POINT_RESPONSE";

    private Order order;

    private String msg;

    private int statusCode;

}
