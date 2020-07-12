package com.apicaller.sosotaxi.entity;

import lombok.Data;

/**
 * @author 骆荟州
 * @CreateTime 2020/7/8
 * UpdateTime 2020/7/11
 */
@Data
public class Passenger extends User {
    /**乘客的用户id */
    private long userId;

    /** 昵称 */
    private String nickName;

    /** 行业 */
    private Short industry;

    /** 公司 */
    private String company;

    /** 职业 */
    private String occupation;

    //这些东西暂时不懂怎么存放在数据库中好
    /** 紧急联系人 */
    //private HashMap<String, String> urgentContact;

    /** 常用地址 */
    //private HashMap<String, String> commonAddress;

    /** 乘车偏好 */
    //private String[] ridePreference;

}
