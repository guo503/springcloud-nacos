package com.nacos.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guos
 * @date 2020/12/8 11:42
 **/
@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController {

    @Value("${rsa.publicKey}")
    public String publicKey;

    @GetMapping("/publicKey")
    public Object getPublicKey() {
        return publicKey;
    }

}
