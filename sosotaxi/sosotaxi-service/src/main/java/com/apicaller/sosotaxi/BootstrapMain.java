package com.apicaller.sosotaxi;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Beingwild
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class BootstrapMain {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapMain.class,args);
    }
}
