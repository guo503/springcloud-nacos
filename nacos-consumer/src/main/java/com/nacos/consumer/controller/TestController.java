package com.nacos.consumer.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guos
 * @date 2020/12/8 11:42
 **/
@RestController
@RequestMapping("/test")
public class TestController {



    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String get(@PathVariable String string) {
        return string;
    }

}
