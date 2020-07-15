package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/15 9:28 上午
 * @updateTime:
 */
@FeignClient(name = "user-service")
@Service
public interface DriverInfofeignClient {
    /**
     * 根据用户id获取司机信息
     * @param userId
     * @return Driver
     */
    @RequestMapping(method = RequestMethod.GET,value = "/driver/getById")
    Driver getDriverById(@RequestParam("userId") long userId);

    /**
     * 根据用户名获取司机信息
     * @param username
     * @return Driver
     */
    @RequestMapping(method = RequestMethod.GET,value = "/driver/getByName")
    Driver getDriverByName(@RequestParam("username") String username);

    /**
     * 新增司机信息
     * @param driver
     * @return 影响的行数
     */
    @RequestMapping(method = RequestMethod.POST,value = "/driver/addDriver")
    int addDriver(@RequestBody DriverVo driver);

    /**
     * 修改司机信息
     * @param driver
     * @return 影响的行数
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/driver/updateDriver")
    int updateDriver(@RequestBody DriverVo driver);
}
