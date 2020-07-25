package com.apicaller.sosotaxi.entity.dispatch.dto;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.util.Objects;

/**
 * @author 张流潇潇
 * @createTime 2020/7/16
 * @updateTime
 * 登陆了的司机，储存坐标
 */
@Data
public class LoginDriver {


    /**
     * 司机的token
     */
    private String token;

    public LoginDriver()
    {
        geoPoint = new GeoPoint();
    }

    private GeoPoint geoPoint;

    /** 用户名 */
    private String userName;

    /**
     * 服务类型，舒适型
     */
    private Short serviceType;

    /** 司机是否已经接单了  */
    private boolean isDispatched;


    /**司机是否开始听单*/
    private boolean startListening;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoginDriver)) {
            return false;
        }
        LoginDriver that = (LoginDriver) o;
        return Objects.equals(getUserName(), that.getUserName()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash( getUserName());
    }
}
