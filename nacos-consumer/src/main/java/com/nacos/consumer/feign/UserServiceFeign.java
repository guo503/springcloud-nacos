package com.nacos.consumer.feign;

import com.nacos.feign.service.provider.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guos
 * @date 2020/12/14 15:48
 **/
@Service
public class UserServiceFeign {

    @Autowired
    private UserService userService;

    public Object get(Integer id) {
        return userService.get(id);
    }

}
