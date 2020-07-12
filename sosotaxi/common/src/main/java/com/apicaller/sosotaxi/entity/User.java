package com.apicaller.sosotaxi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 骆荟州
 * @CreateTime 2020/7/8
 * UpdateTime 2020/7/11
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -54620145478166527L;

    /** 用户id */
    private long userId;

    /** 用户密码 */
    private String password;

    /** 用户角色，乘客或司机 */
    private String role;

    /** 用户名，暂定就是电话号码 */
    private String userName;

    /** 电话号码 */
    private String phoneNumber;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private Short birthYear;

    /** 头像路径 */
    private String avatarPath;

    /** 真实姓名 */
    private String realName;

    /** 身份证号 */
    private String idCardNumber;

}