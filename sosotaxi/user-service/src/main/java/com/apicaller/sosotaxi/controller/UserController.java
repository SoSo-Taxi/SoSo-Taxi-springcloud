package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.UserVo;
import com.apicaller.sosotaxi.service.PassengerService;
import com.apicaller.sosotaxi.service.UserService;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.PublicKey;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-07-11 10:09:17
 */
@RestController
@RequestMapping
public class UserController {
    /**
     * 服务对象
     */


    @Resource
    private UserService userService;

    @Resource
    private PassengerService passengerService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public User selectOne(Integer id) {
        return this.userService.queryById(id);
    }


    @PostMapping("/getUserByUserName")
    public User getByName(@RequestBody String userName){
        return this.userService.queryUserByUserName(userName);
    }

    @PostMapping("/insertUser")
    public User insertUser(@RequestBody UserVo userVo)
    {
        String userName = userVo.getUserName();
        String password = userVo.getPassword();
        String role = userVo.getRole();
        User user = new User();
        user.setPassword(password);
        user.setUserName(userName);
        user.setRole(role);
        return userService.insert(user);
    }

    @PostMapping ("/isExistUserName")
    public boolean isExistUserName(@RequestBody String userName)
    {
        return userService.ifExistsByUserName(userName);
    }

    @GetMapping("/test")
    public Passenger getPassenger(long id) {

        return passengerService.queryById(id);
    }

}