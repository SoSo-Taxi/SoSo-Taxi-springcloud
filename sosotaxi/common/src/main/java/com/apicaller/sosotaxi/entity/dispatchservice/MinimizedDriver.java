package com.apicaller.sosotaxi.entity.dispatchservice;

import lombok.Data;

import java.io.Serializable;

@Data
public class MinimizedDriver implements Serializable {

    /**
     * 司机所在城市
     */
    private String city;

    /**
     * 司机从事服务的车型
     */
    private int type;

    /**
     * 司机唯一标识
     */
    private String driverId;

    public MinimizedDriver(){
        //
    }

    public MinimizedDriver(String city, int type, String driverId){
        this.city = city;
        this.type = type;
        this.driverId = driverId;
    }
}
