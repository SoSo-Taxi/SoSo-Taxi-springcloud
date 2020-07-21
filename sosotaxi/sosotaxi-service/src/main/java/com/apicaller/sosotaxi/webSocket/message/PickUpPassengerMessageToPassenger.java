package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 * 司机发送接到乘客，发送给乘客
 */
@Data
public class PickUpPassengerMessageToPassenger implements Message{

    public static final String TYPE = "PICK_UP_PASSENGER_MESSAGE_TO_PASSENGER";

    private String msg;

    private int statusCode;


}
