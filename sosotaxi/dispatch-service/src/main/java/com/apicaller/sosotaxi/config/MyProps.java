package com.apicaller.sosotaxi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Repository;

/**
 * 可配置属性集合
 * 暂时不用这个，但考虑到后面可能会有更多参数，还是写了
 */
@ConfigurationProperties(prefix = "baiduapi")
@Repository
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
