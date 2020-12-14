package com.nacos.consumer.controller;


import com.nacos.consumer.feign.UserServiceFeign;
import mybatis.base.template.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户api类
 *
 * @author guos
 * @date 2020/12/12 16:28
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 查询用户信息
     *
     * @param id
     * @return
     * @author guos
     * @date 2020/12/14 15:33
     **/
    @GetMapping("/{id}")
    public Result<Object> get(@PathVariable Integer id) {
        return Result.success(userServiceFeign.get(id));
    }
}
