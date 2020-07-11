package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.UserVo;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import com.apicaller.sosotaxi.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author 张流潇潇
 * @createTime 2020.7.9
 * @updateTime 2020.7.11
 *
 * 这个类名字别改，改了出错，我tm服了
 */


@RestController
@RequestMapping("/user")
public class TestController {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    UserServiceFeignClient userServiceFeignClient;

    @GetMapping(value ="/get")
    public String  getString() {
        return "测试成功";
    }


    @GetMapping(value = "/getUserByUserName")
    public ResponseBean getUserByName(@RequestParam(value = "userName",required = true) String userName)
    {
        System.out.println(userName);
        System.out.println(userServiceFeignClient.getUserByUserName(userName));
        User user = userServiceFeignClient.getUserByUserName(userName);
        return new ResponseBean(200,"查询成功",user);
    }

    @PostMapping(value = "/registry")
    public ResponseBean registry(@RequestBody UserVo userVo)
    {
        String userName = userVo.getUserName();
        System.out.println(userVo);
        if(userServiceFeignClient.isExistUserName(userName))
        {
            return new ResponseBean(201,"用户已存在",null);
        }
        String password = userVo.getPassword();
        userVo.setPassword(bCryptPasswordEncoder.encode(password));
        User user = userServiceFeignClient.insertUser(userVo);
        return new ResponseBean(200,"创建成功",user);
    }
}

