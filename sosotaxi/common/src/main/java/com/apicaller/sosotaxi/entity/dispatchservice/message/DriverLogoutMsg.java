package com.apicaller.sosotaxi.entity.dispatchservice.message;

import lombok.Data;

@Data
public class DriverLogoutMsg {

    /**
     * 司机id
     */
    private String driverId;
}
