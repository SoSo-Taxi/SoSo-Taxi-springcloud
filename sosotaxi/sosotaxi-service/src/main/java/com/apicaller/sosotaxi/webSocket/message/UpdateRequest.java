package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 * 司机更新自己位置
 */
@Data
public class UpdateRequest implements Message {
    public static final String TYPE = "UPDATE_REQUEST";

    private int messageId;

    /** 纬度 */
    private double lat;

    /** 经度 */
    private double lng;

    /** 是否接单 */
    private boolean isDispatched;

    /**是否开始听单*/
    private boolean startListening;

    private Short serviceType;
}
