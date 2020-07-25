package com.apicaller.sosotaxi.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.io.Serializable;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 1:20 下午
 * @updateTime:
 */
@Data
public class PassengerVo implements Serializable {
    private static final long serialVersionUID = -63748569L;

    /** 用户id */
    private long userId;

    /** 昵称 */
    private String nickname;

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
    private JSONArray preference;

    public static PassengerVo fromPassenger(Passenger passenger) {
        if(passenger == null) {
            return null;
        }
        PassengerVo passengerVo  = new PassengerVo();
        passengerVo.setUserId(passenger.getUserId());
        passengerVo.setNickname(passenger.getNickname());
        passengerVo.setIndustry(passenger.getIndustry());
        passengerVo.setCompany(passenger.getCompany());
        passengerVo.setOccupation(passenger.getOccupation());
        passengerVo.setUrgentContact(passenger.getUrgentContact());
        passengerVo.setCommonAddress(passenger.getCommonAddress());
        passengerVo.setPreference(passenger.getPreference());
        return passengerVo;
    }

}
