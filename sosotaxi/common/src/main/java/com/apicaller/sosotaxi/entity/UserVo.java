package com.apicaller.sosotaxi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 张流潇潇
 * @createTime 2020.7.12
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String role;
}
