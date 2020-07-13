package com.apicaller.sosotaxi.entity;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;

/**
 * @author 骆荟州
 * @CreateTime 2020/7/8
 * UpdateTime 2020/7/11
 */
@Data
public class Passenger extends User {

    /** 昵称 */
    private String nickName;

    /** 行业 */
    private Short industry;

    /** 公司 */
    private String company;

    /** 职业 */
    private String occupation;

    /** 紧急联系人 */
    private JSONObject urgentContact;

    /** 常用地址 */
    private JSONObject commonAddress;

    /** 乘车偏好 */
    private JSONArray ridePreference;

}
