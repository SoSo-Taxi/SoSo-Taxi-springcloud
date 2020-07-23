package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.PassengerVo;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.UserVo;
import com.apicaller.sosotaxi.service.PassengerService;
import com.apicaller.sosotaxi.service.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.List;
import java.util.regex.Pattern;


/**
 * (User)表控制层
 *
 * @author makejava
 * @createTime 2020-07-11 10:09:17
 * @updateTime 2020-07-2- 10:42:11
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    PassengerService passengerService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public User selectOne(long id) {
        User user = userService.queryById(id);
        if(user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
            user.setAvatarPath(userService.getRealAvatarPath(user.getAvatarPath()));
        }
        return user;
    }


    @PostMapping("/getUserByUserName")
    public User getByName(@RequestBody String userName){
        User user = userService.queryUserByUserName(userName);
        if(user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
            user.setAvatarPath(userService.getRealAvatarPath(user.getAvatarPath()));
        }
        return user;
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
        //假如用户名全是数字，就把它当成用户的手机号码。
        if(Pattern.matches("^\\d+$", userVo.getUserName())) {
            user.setPhoneNumber(userVo.getUserName());
        }

        user = userService.insert(user);
        //如果角色是passenger，在passenger表中建立一条记录，方便之后读出数据。
        if("passenger".equals(user.getRole())) {
            PassengerVo passengerVo = new PassengerVo();
            passengerVo.setUserId(user.getUserId());
            passengerService.insert(passengerVo);
        }
        return user;
    }

    @PostMapping("/isExistUserName")
    public boolean isExistUserName(@RequestBody String userName)
    {
        return userService.ifExistsByUserName(userName);
    }

    @PutMapping("/updateUser")
    public User updateUserInfo(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * 改变头像
     */
    @PostMapping("changeAvatar")
    public boolean changeAvatar(HttpServletRequest request) {
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> images = ((MultipartHttpServletRequest) request).getFiles("avatar");
        if(images.isEmpty()) {
            return false;
        }
        try {
            Long userId = Long.parseLong(params.getParameter("userId"));
            FileInputStream inputStream = (FileInputStream)images.get(0).getInputStream();
            String fileName = userId.toString() + inputStream.hashCode() + images.get(0).getOriginalFilename();
            if(userService.storageAvatar(inputStream, fileName)) {
                User user = new User();
                user.setUserId(userId);
                user.setAvatarPath(fileName);
                userService.update(user);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }

    }

}