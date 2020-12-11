package com.nacos.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cb
 */
@RestController
@RefreshScope
public class NacosConfigController {

    @Value("${config:}")
     String str;

    @GetMapping("/test")
    public String testConfig() {

        System.out.println(str);
        return str;
    }
}
