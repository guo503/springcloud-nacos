package com.nacos.provider.controller;

import com.nacos.common.controller.BaseController;
import com.nacos.provider.entity.User;
import com.nacos.provider.service.UserService;
import com.nacos.provider.vo.UserVO;
import org.springframework.web.bind.annotation.*;

/**
* api类
* @author guos
* @date 2021/03/03 12:01
*/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User, UserVO> {
}