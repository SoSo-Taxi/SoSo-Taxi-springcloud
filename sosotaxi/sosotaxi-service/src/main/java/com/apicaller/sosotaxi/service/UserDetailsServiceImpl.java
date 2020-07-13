package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.JwtUser;

import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 本类是用来提供验证方法的
 * login时调用loadUserByUsername()方法验证
 * @author 张流潇潇
 * @createTime 2020.7.10
 * @updateTime
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    UserServiceFeignClient userServiceFeignClient;


    public UserDetailsServiceImpl() {
    }
    @Override
    public JwtUser loadUserByUsername(String name) throws UsernameNotFoundException {


        User user = userServiceFeignClient.getUserByUserName(name);

        return new JwtUser(user);
    }



}
