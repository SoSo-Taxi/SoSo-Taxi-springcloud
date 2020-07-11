package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.JwtUser;

import com.apicaller.sosotaxi.project.entity.User;
import com.apicaller.sosotaxi.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author shuang.kou
 * @description UserDetailsService实现类
 */

/**
 * 本类是用来提供验证方法的
 * login时调用loadUserByUsername()方法验证
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public UserDetailsServiceImpl() {
    }

    @Override
    public JwtUser loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userService.findUserByName(name);
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        return new JwtUser(user);
    }
}
