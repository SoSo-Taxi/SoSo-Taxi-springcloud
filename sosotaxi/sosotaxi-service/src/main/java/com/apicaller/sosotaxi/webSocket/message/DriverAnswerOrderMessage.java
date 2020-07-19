package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.Driver;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/18
 * @updateTime
 * 司机回应订单消息，从司机端发出
 */
@Data
public class DriverAnswerOrderMessage implements Message {

    public static final String TYPE = "DRIVER_ANSWER_ORDER_MESSAGE";

    /**
     * 是否接受订单
     */
    private Boolean takeOrder;

    /**
     * 用户token
     */
    private String token;

    /**
     * 返回的司机
     */
    private Driver driver;

}
