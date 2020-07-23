package com.apicaller.sosotaxi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class AlipayConfig {

    @Resource
    private AliPayProperties aliPayProperties;


}
