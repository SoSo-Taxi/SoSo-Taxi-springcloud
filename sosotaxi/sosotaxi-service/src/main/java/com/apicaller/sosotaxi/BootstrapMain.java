package com.apicaller.sosotaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Beingwild
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BootstrapMain {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapMain.class,args);
    }
}
