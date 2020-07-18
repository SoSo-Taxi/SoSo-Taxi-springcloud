package com.apicaller.sosotaxi.entity.dispatch.dto;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 * 登陆了的司机，储存坐标
 */
@Data
public class LoginDriver {


    public LoginDriver()
    {
        geoPoint = new GeoPoint();
    }

    private GeoPoint geoPoint;

    /** 用户名 */
    private String userName;

    /** 用户token */
    private String token;

    /** 司机是否已经接单了 yes or no */
    private String isDispatched;

}
