package com.nacos.provider.mapper;

import com.nacos.provider.model.User;
import mybatis.base.mapper.Mapper;
import mybatis.base.mapper.SoftDeleteMapper;

/**
* 用户数据访问层
* @author guos
* @date 2020/12/11 20:00
*/
public interface UserMapper extends Mapper<User>, SoftDeleteMapper<User> {
}