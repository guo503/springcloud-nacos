package com.nacos.provider.controller;

import com.nacos.provider.business.UserBusiness;
import com.nacos.provider.model.User;
import com.nacos.provider.query.UserQuery;
import com.nacos.provider.vo.UserVO;
import mybatis.base.template.bs.controller.BaseController;
import mybatis.base.template.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
* 用户api类
* @author guos
* @date 2020/12/12 16:28
*/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserBusiness, User, UserQuery, UserVO> {

    @Value("${rsa.publicKey}")
    private String publicKey;


    @GetMapping("/show")
    public Result<Object> show(){
        return Result.success(publicKey);
    }
}
