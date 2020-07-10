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


    private Long id;
    private String userName;
    private String fullName;
    private String password;
    private Boolean enabled;

    public User() {
    }

    public User(Long id, String userName, String fullName, String password, Boolean enabled, List<UserRole> userRoles) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.enabled = enabled;
        this.userRoles = userRoles;
    }

    @JsonIgnore
    private List<UserRole> userRoles = new ArrayList<>();

    public List<SimpleGrantedAuthority> getRoles() {
        List<Role> roles = userRoles.stream().map(UserRole::getRole).collect(Collectors.toList());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        return authorities;
    }
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
