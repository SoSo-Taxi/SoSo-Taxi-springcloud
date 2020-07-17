package com.apicaller.sosotaxi.webSocket.message;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
public class AuthRequest implements Message {



    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public AuthRequest setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
