package com.nacos.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nacos.provider.entity.User;
import com.nacos.provider.vo.UserVO;

/**
* service类
* @author guos
* @date 2021/03/03 12:01
*/
public interface UserService extends IService<User> {

    /**
     * 多数据源事务测试
     * @param userVO
     * @author guos
     * @date 2021/3/5 9:54
     * @return
     **/
    void multiSave(UserVO userVO);
}
