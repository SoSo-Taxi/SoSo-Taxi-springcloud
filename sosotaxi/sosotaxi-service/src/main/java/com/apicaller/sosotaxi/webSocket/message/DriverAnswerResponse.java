package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Driver;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/18
 * @updateTime
 * 司机接受订单后，把此消息发送给乘客
 */
@Data
public class DriverAnswerResponse implements Message{
    public static final String TYPE = "ORDER_RESULT_MESSAGE";


    private Driver driver;

    private int estimateTime;

    private double estimateMoney;

    private String msg;

    private int statusCode;
}
