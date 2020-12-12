package com.nacos.provider;

import mybatis.spring.annotation.ExtMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author guos
 * @date 2020/12/7 11:20
 **/
@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication
@ExtMapperScan({"com.nacos.provider.mapper"})
@ServletComponentScan
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
