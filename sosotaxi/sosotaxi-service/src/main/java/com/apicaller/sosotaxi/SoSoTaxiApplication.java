package com.apicaller.sosotaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 张流潇潇
 * @createTime 2020.7.8
 * @updateTime
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients(basePackages = "com.apicaller.sosotaxi.feignClients")
public class SoSoTaxiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoSoTaxiApplication.class,args);
    }
}
