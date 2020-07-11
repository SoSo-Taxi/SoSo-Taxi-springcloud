package com.apicaller.sosotaxi.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shuang.kou
 */

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class User extends AbstractAuditBase {


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


//    @JsonIgnore
//    private List<UserRole> userRoles = new ArrayList<>();
//
//    public List<SimpleGrantedAuthority> getRoles() {
//        List<Role> roles = userRoles.stream().map(UserRole::getRole).collect(Collectors.toList());
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
//        return authorities;
//    }
//    public UserRepresentation toUserRepresentation() {
//        return UserRepresentation.builder().fullName(this.fullName)
//                .userName(this.userName).build();
//    }
//
//    public static User of(UserRegisterRequest userRegisterRequest) {
//        return User.builder().fullName(userRegisterRequest.getFullName())
//                .userName(userRegisterRequest.getUserName())
//                .enabled(true).build();
//    }
//
//    public void updateFrom(UserUpdateRequest userUpdateRequest) {
//        if (Objects.nonNull(userUpdateRequest.getFullName())) {
//            this.setFullName(userUpdateRequest.getFullName());
//        }
//        if (Objects.nonNull(userUpdateRequest.getPassword())) {
//            this.setPassword(userUpdateRequest.getPassword());
//        }
//        if (Objects.nonNull(userUpdateRequest.getEnabled())) {
//            this.setEnabled(userUpdateRequest.getEnabled());
//        }
//    }

}
