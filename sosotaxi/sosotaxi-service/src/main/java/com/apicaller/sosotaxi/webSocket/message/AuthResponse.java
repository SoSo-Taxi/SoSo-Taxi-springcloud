package com.apicaller.sosotaxi.webSocket.message;

import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@Data
public class AuthResponse implements Message {

    public static final String TYPE = "AUTH_RESPONSE";

    /**
     * 响应状态码
     * 200成功
     * 201失败
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

    public AuthResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public AuthResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}