package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 派单回应，请用户等待
 */
@Data
public class StartOrderResponse implements Message {
    public static final String TYPE = "START_ORDER_RESPONSE";

    private String msg;

    private Integer statusCode;

}
