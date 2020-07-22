package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机到达地点，发送给乘客的消息
 */
@Data
public class ArriveDepartPointToPassenger implements Message {
    public static final String TYPE = "ARRIVE_DEPART_POINT_TO_MESSAGE";

    public int statusCode;

    public String msg;

}
