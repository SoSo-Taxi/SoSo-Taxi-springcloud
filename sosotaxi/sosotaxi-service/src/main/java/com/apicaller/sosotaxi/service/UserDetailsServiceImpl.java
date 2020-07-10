package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.JwtUser;

import com.apicaller.sosotaxi.project.entity.User;
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

//    private final UserService userService;
//
//    public UserDetailsServiceImpl(UserService userService) {
//        this.userService = userService;
//    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    public UserDetailsServiceImpl() {
    }

    @Override
    public JwtUser loadUserByUsername(String name) throws UsernameNotFoundException {

        /**
         * 先假装验证过程会返回如下一个对象
         * 所以登陆输入以下的用户即可成功
         */
        User user = new User();
        user.setFullName("zhangliuxiaoxiao");
        user.setUserName("zlxx");
        String encode = bCryptPasswordEncoder.encode("123456");
        user.setPassword(encode);
        user.setId((long)1);

        return new JwtUser(user);
    }

}
