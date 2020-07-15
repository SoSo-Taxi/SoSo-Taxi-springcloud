package com.apicaller.sosotaxi.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import com.alibaba.fastjson.*;
import java.io.Serializable;

/**
 * @author 骆荟州
 * @createTime 2020/7/13 10:00:12
 * @updateTime
 */
@Data
public class Passenger extends User implements Serializable {
    private static final long serialVersionUID = -35577885715985753L;

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

    public boolean mergeInfoFromUser(User user) {
        if(this.getUserId() != user.getUserId()) {
            return false;
        }
        setUserName(user.getUserName());
        setRole(user.getRole());
        setRealName(user.getRealName());
        setGender(user.getGender());
        setPhoneNumber(user.getPhoneNumber());
        setPassword(user.getPassword());
        setIdCardNumber(user.getIdCardNumber());
        setBirthYear(user.getBirthYear());
        setAvatarPath(user.getAvatarPath());
        return true;
    }
}