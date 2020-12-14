package com.nacos.feign.service.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cb
 */
@FeignClient(value = "nacos-provider")
public interface UserService {

    @GetMapping(value = "user/{id}")
    String get(@PathVariable(value = "id") Integer id);

}

