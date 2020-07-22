package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.util.Date;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 发出此消息，开始尝试派单
 */
@Data
public class StartOrderMessage implements Message {

    public static final String TYPE = "START_ORDER_MESSAGE";

    private Long passengerId;

    private String phoneNumber;

    private String userToken;

    private String city;


    /**考虑到可能有预约订单，这里命名为出发时间。*/
    private Date departTime;

    private String userName;

    private GeoPoint departPoint;

    private GeoPoint destPoint;

    private Short serviceType;

    private Short passengerNum;

    private String departName;

    private String destName;

}
