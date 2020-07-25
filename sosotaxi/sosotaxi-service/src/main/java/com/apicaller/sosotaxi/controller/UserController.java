package com.apicaller.sosotaxi.controller;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.UserVo;
import com.apicaller.sosotaxi.feignClients.UserServiceFeignClient;
import com.apicaller.sosotaxi.feignClients.WalletFeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserController {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    UserServiceFeignClient userServiceFeignClient;

    @Resource
    WalletFeignClient walletFeignClient;

    @GetMapping(value ="/get")
    public String  getString() {
        return "测试成功";
    }


    @PreAuthorize("hasAnyRole('ROLE_admin')")
    @PostMapping(value = "/getUserByUserName")
    public ResponseBean getUserByName(@RequestBody String userName)
    {
        JSONObject object = JSONObject.parseObject(userName);
        System.out.println(userName);
        String userNameString = (String) object.get("userName");
        System.out.println(userNameString);
        User user = userServiceFeignClient.getUserByUserName(userNameString);
        System.out.println(user);
        return new ResponseBean(200,"查询成功",user);
    }

    @PostMapping(value = "/registry")
    public ResponseBean registry(@RequestBody UserVo userVo)
    {
        String userName = userVo.getUserName();
        System.out.println(userVo);
        System.out.println(userName);
        if(userServiceFeignClient.isExistUserName(userName))
        {
            return new ResponseBean(201,"用户已存在",null);
        }
        String password = userVo.getPassword();
        userVo.setPassword(bCryptPasswordEncoder.encode(password));
        User user = userServiceFeignClient.insertUser(userVo);
        //建立一个用户时，就建立其钱包账户。
        walletFeignClient.setupAccount(user.getUserId());
        return new ResponseBean(200,"创建成功",user);
    }

    @PostMapping(value = "/isRegistered")
    public ResponseBean isRegistered(@RequestBody String userName)
    {
        JSONObject object = JSONObject.parseObject(userName);
        String userNameString = (String) object.get("userName");
        System.out.println(userNameString);
        boolean isRegistered = userServiceFeignClient.isExistUserName(userNameString);
        if (isRegistered)
        {
            return new ResponseBean(201,"该手机号已经注册",null);
        }
        else {
            return new ResponseBean(200,"该手机号未注册",null);
        }
    }

    @PostMapping(value = "/updateUser")
    public ResponseBean updateUser(@RequestBody User user) {
        User user1 = userServiceFeignClient.updateUser(user);
        if(user1 == null) {
            return new ResponseBean(403,"更新用户信息失败",null);
        }
        return new ResponseBean(403,"更新用户信息成功",null);
    }
}

