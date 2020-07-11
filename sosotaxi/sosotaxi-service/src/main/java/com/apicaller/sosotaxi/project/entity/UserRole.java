package com.apicaller.sosotaxi.project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
public class UserRole extends AbstractAuditBase implements Serializable {

    private Long id;

    private User user;

    private Role role;


}
