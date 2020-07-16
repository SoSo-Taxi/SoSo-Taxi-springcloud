package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 */
@Data
public class UpdateRequest implements Message {
    public static final String TYPE = "UPDATE_REQUEST";

    /** 纬度 */
    private double lat;

    /** 经度 */
    private double lng;

    /** 是否接单 yes or no*/
    private String isDispatched;
}
