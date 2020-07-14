package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.entity.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author 张流潇潇
 * @createTime 2020.7.10
 * @updateTime
 */
@FeignClient(name = "user-service")
@Service
public interface UserServiceFeignClient {

    /**
     * 根据用户名获取用户
     * @param userName
     * @return User
     */
    @RequestMapping(method = RequestMethod.POST,value = "/user/getUserByUserName")
    User getUserByUserName(@RequestBody String userName);

    /**
     * 插入用户
     * @param userVo
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/user/insertUser")
    User insertUser(@RequestBody UserVo userVo);

    /**
     * 判断是否存在
     * @param userName
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/user/isExistUserName")
    boolean isExistUserName(@RequestBody String userName);
}
