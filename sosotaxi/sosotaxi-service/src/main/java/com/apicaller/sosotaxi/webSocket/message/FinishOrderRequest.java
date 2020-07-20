package com.apicaller.sosotaxi.webSocket.message;

import java.util.Date;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 用户发出完成订单消息
 */
public class FinishOrderRequest implements Message {
    public static final String TYPE = "FINISH_ORDER_REQUEST";

    private Date startTime;

    private Date endTime;



}
