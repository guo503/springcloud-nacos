package com.nacos.provider.controller;

import com.nacos.common.controller.BaseController;
import com.nacos.common.response.Result;
import com.nacos.provider.entity.User;
import com.nacos.provider.service.UserService;
import com.nacos.provider.vo.UserVO;
import org.springframework.web.bind.annotation.*;

/**
 * api类
 *
 * @author guos
 * @date 2021/03/03 12:01
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserService, User, UserVO> {

    /**
     * 多数据源事务测试
     *
     * @param userVO
     * @return
     * @author guos
     * @date 2020/7/28 15:13
     **/
    @GetMapping("/multi")
    public Result<Object> multi(UserVO userVO) {
        baseService.multiSave(userVO);
        return Result.success();
    }
}
