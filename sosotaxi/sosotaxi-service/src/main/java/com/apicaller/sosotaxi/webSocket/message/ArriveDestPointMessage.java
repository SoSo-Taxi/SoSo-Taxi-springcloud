package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Order;
import lombok.Data;

/**
 * 司机到达终点后，发给服务器的消息。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/21 7:35 下午
 * @updateTime:
 */

@Data
public class ArriveDestPointMessage implements Message {
    public static final String TYPE = "ARRIVE_DEST_POINT_MESSAGE";

    private Order order;

    private Double basicCost;

    private Double freewayCost;

    private Double parkingCost;

}
