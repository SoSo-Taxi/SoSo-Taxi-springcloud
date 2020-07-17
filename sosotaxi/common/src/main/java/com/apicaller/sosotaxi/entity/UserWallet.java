package com.apicaller.sosotaxi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserWallet实体类
 *
 * @author 骆荟州
 * @createTime 2020-07-17 10:39:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWallet implements Serializable {
    private static final long serialVersionUID = -15350474798607641L;
    
    private Long userId;
    
    private Double balance;
    
    private Integer point;

}