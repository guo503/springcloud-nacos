package com.nacos.provider.controller;

import com.nacos.provider.business.UserBusiness;
import com.nacos.provider.model.User;
import com.nacos.provider.query.UserQuery;
import com.nacos.provider.vo.UserVO;
import mybatis.base.template.bs.controller.BaseController;
import org.springframework.web.bind.annotation.*;

/**
* 用户api类
* @author guos
* @date 2020/12/11 20:00
*/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserBusiness, User, UserQuery, UserVO> {
}