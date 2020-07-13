package com.apicaller.sosotaxi.entity;

import lombok.Data;

/**
 * @author 骆荟州
 * @CreateTime 2020/7/8
 * UpdateTime 2020/7/11
 */
@Data
public class Driver {

    /** 车型 */
    private String carModel;

    /** 车辆颜色 */
    private String carColor;

    /** 车牌号码 */
    private String licensePlate;

    /** 车辆等级，一般分为A、B、C、D、E级 */
    private Character carLevel;

    /** 驾驶证号码，比较尴尬的是这个号码和身份证号码是一样的 */
    private String driverLicenseNumber;

    /** 行驶证中的车辆识别代码 */
    private String vehicleIdentificationNumber;
}
