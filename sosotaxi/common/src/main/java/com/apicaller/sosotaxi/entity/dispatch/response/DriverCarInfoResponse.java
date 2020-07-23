package com.apicaller.sosotaxi.entity.dispatch.response;

import com.apicaller.sosotaxi.entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单发配成功后，前端可获取该订单对应的司机信息和车辆信息。
 * 此为返回给前端的信息的类。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/16 3:18 下午
 * @updateTime:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverCarInfoResponse implements Serializable {
    private static final long serialVersionUID = -4484485641L;

    private long driverId;

    private String driverName;

    private String phoneNumber;

    private String avatarPath;

    private Short serviceType;

    private String carBrand;

    private String carModel;

    private String carColor;

    private String licensePlate;

    private Double rate;

    private Integer orderNum;

    public static DriverCarInfoResponse fromDriver(Driver driver) {

        if(driver == null) {
            return null;
        }
        DriverCarInfoResponse driverCarInfoResponse = new DriverCarInfoResponse();
        driverCarInfoResponse.setDriverId(driver.getUserId());
        driverCarInfoResponse.setDriverName(driver.getRealName());
        driverCarInfoResponse.setPhoneNumber(driver.getPhoneNumber());
        driverCarInfoResponse.setAvatarPath(driver.getAvatarPath());
        driverCarInfoResponse.setServiceType(driver.getServiceType());
        driverCarInfoResponse.setCarBrand(driver.getCarBrand());
        driverCarInfoResponse.setCarModel(driver.getCarModel());
        driverCarInfoResponse.setCarColor(driver.getCarColor());
        driverCarInfoResponse.setLicensePlate(driver.getLicensePlate());

        return driverCarInfoResponse;
    }

}
