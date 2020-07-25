package com.apicaller.sosotaxi.webSocket.message;

import com.apicaller.sosotaxi.entity.dispatch.dto.LoginDriver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 张流潇潇
 * @createTime 2020/7/17
 * @updateTime
 * 派单回应，请用户等待
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartOrderResponse implements Message {
    public static final String TYPE = "START_ORDER_RESPONSE";

    private Integer statusCode;

    private String msg;

    private Long orderId;

}
