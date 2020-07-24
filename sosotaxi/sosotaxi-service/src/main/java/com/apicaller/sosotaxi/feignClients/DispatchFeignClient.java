package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.dispatchservice.MinimizedDriver;
import com.apicaller.sosotaxi.entity.dispatchservice.UnsettledOrder;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLoginMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLogoutMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverUpdateMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.OpsForOrderMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 江诗烨
 *
 */

@FeignClient(name = "dispatch-service")
@Service
public interface DispatchFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/driver/login")
    Boolean login(@RequestBody DriverLoginMsg msg);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/driver/update")
    Boolean update(@RequestBody DriverUpdateMsg msg);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/driver/accept")
    UnsettledOrder accept(@RequestBody OpsForOrderMsg msg);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/driver/refuse")
    MinimizedDriver refuseOrder(@RequestBody OpsForOrderMsg msg);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/driver/logout")
    Boolean logout(@RequestBody DriverLogoutMsg msg);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatch/passenger/submit")
    MinimizedDriver submit(@RequestBody UnsettledOrder order);

}
