package com.apicaller.sosotaxi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;

/**
 * (UserCoupon)实体类
 *
 * @author 骆荟州
 * @since 2020-07-17 11:18:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCoupon implements Serializable {
    private static final long serialVersionUID = -20106548230084152L;
    
    private Long id;
    
    private Long userId;
    
    private Double worth;
    
    private Date expireTime;

}