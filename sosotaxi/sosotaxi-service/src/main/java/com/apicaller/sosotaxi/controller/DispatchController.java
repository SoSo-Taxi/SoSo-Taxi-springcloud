package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.ResponseBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张流潇潇
 * @createTime 2020/7/15
 * @updateTime
 */
@RestController
@RequestMapping("/dispatch")
public class DispatchController {


    @PostMapping("/")
    public ResponseBean callCar()
    {

        return new ResponseBean();
    }
}
