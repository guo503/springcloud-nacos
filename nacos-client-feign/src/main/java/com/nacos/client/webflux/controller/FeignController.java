package com.nacos.client.webflux.controller;

import com.nacos.client.webflux.service.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cb
 */
@RestController
public class FeignController {

    @Autowired
    private FeignService feignService;

    @RequestMapping("test")
    public String testFeign(){

        String hello = feignService.hello("Nacos!");
        System.out.println(hello);
        return hello;
    }
}
