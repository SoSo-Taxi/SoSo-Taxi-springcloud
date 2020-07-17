package com.apicaller.sosotaxi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 可配置属性集合
 */
@ConfigurationProperties(prefix = "baiduapi")
@Component
public class MyProps {

    /**
     * api验证
     */
    private String ak;

    /**
     * 路线规划api
     */
    private String url;

    public String getAk(){

        return ak;
    }

    public MyProps setAk(String ak){

        this.ak = ak;
        return this;
    }

    public String getUrl(){

        return url;
    }

    public MyProps setUrl(String url){

        this.url = url;
        return this;
    }
}
