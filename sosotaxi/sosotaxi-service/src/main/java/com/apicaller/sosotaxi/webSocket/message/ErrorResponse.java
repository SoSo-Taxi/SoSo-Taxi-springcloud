package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/21
 * @updateTime
 */
@Data
public class ErrorResponse implements Message {

    public static final String TYPE = "ERROR_RESPONSE";

    private String msg;

    private int statusCode;
}
