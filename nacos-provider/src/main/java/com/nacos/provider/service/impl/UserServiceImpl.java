package com.nacos.provider.service.impl;

import com.nacos.provider.mapper.UserMapper;
import com.nacos.provider.model.User;
import com.nacos.provider.service.UserService;
import mybatis.base.template.bs.service.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* 用户service实现类
* @author guos
* @date 2020/12/12 16:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}