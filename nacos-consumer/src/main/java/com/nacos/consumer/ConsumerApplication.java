package com.nacos.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guos
 * @date 2020/12/7 11:20
 **/
@RestController
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.nacos.feign.service"})
@ServletComponentScan(basePackages = {"com.nacos.common.conf.druid"})
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
