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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoginDriver)) {
            return false;
        }
        LoginDriver that = (LoginDriver) o;
        return Objects.equals(getGeoPoint(), that.getGeoPoint()) &&
                Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getToken(), that.getToken()) &&
                Objects.equals(getIsDispatched(), that.getIsDispatched());
    }

    @Override
    public int hashCode() {
        return Objects.hash( getUserName(), getToken());
    }
}
