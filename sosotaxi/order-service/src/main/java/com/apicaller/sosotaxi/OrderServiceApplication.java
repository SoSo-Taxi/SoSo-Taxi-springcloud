package com.apicaller.sosotaxi;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 骆荟州
 * @createTime 2020/7/15 00:20:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("/dao")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
