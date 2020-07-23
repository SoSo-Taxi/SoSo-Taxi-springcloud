package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机到达乘车点，司机发给服务器
 */
@Data
public class ArriveDepartPointMessage implements Message {
    public static final String TYPE = "ARRIVE_DEPART_POINT_MESSAGE";

    private Order order;

}
