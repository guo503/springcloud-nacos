package com.nacos.provider.business.impl;

import com.nacos.provider.business.UserBusiness;
import com.nacos.provider.model.User;
import com.nacos.provider.query.UserQuery;
import com.nacos.provider.service.UserService;
import com.nacos.provider.vo.UserVO;
import mybatis.base.template.bs.business.BusinessImpl;
import org.springframework.stereotype.Service;

/**
* 用户业务类
* @author guos
* @date 2020/12/11 20:00
*/
@Service
public class UserBusinessImpl extends BusinessImpl<UserService, User, UserQuery, UserVO> implements UserBusiness {
}