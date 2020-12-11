package com.nacos.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cb
 */
@SpringBootApplication
@Slf4j
@RestController
public class SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }

    @GetMapping("hello")
    @SentinelResource("hello")
    public String hello(){

      return "hello Sentinel";
    }

}
