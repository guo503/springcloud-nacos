package com.nacos.client.webflux.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author cb
 */
@FeignClient(name = "alibaba-nacos-producer")
public interface FeignService {

    @GetMapping("hello")
    String hello(@RequestParam String name);

}

