package com.nacos.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nacos.provider.entity.User;
import com.nacos.provider.mapper.UserMapper;
import com.nacos.provider.service.UserService;
import org.springframework.stereotype.Service;

/**
* service实现类
* @author guos
* @date 2021/03/03 12:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}