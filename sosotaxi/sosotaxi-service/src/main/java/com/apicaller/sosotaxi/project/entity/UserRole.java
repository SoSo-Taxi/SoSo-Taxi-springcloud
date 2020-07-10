package com.apicaller.sosotaxi.project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



public class UserRole extends AbstractAuditBase implements Serializable {

    private Long id;

    private User user;

    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
