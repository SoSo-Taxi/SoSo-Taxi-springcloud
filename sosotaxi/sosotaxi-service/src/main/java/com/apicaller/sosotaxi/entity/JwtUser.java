package com.apicaller.sosotaxi.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author 张流潇潇
 * @createTime 2020.7.9
 * @updateTime
 * @description JWT用户对象
 */
@Data
public class JwtUser implements UserDetails {

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

    public JwtUser() {
    }

    /**
     * 通过 user 对象创建jwtUser
     */
    public JwtUser(User user) {
        userId = user.getUserId();
        userName = user.getUserName();
        password = user.getPassword();



        role = user.getRole();
        gender = user.getGender();
        birthYear = user.getBirthYear();
        avatarPath = user.getAvatarPath();
        realName = user.getRealName();
        idCardNumber = user.getIdCardNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "id=" + userId +
                ", username='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

}
