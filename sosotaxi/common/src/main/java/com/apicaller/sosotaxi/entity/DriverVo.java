package com.apicaller.sosotaxi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 10:18 下午
 * @updateTime:
 */
@Data
public class DriverVo implements Serializable {
    private static final long serialVersionUID = 576078345057696L;

    /** 用户id */
    private long userId;

    /** 所在城市 */
    private String city;

    /** 车品牌 */
    private String carBrand;

    /** 车型 */
    private String carModel;

    /** 车辆颜色 */
    private String carColor;

    /** 车牌号码 */
    private String licensePlate;

    /** 司机能提供的服务类型 */
    private Short serviceType;

    /** 驾驶证号码，比较尴尬的是这个号码和身份证号码是一样的 */
    private String driverLicenseNumber;

    /** 行驶证中的车辆识别代码 */
    private String vin;
}
