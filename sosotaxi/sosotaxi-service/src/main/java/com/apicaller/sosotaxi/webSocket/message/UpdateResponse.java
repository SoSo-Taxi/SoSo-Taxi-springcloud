package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/19
 * @updateTime
 */
@Data
public class UpdateResponse implements Message {

    public static final String TYPE = "UPDATE_RESPONSE";

    private int messageId;

    private String msg;

    private int statusCode;

}
