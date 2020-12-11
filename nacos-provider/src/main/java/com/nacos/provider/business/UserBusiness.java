package com.nacos.provider.business;

import com.nacos.provider.model.User;
import com.nacos.provider.query.UserQuery;
import com.nacos.provider.vo.UserVO;
import mybatis.base.template.bs.business.IBusiness;

/**
* 用户service类
* @author guos
* @date 2020/12/11 20:00
*/
public interface UserBusiness extends IBusiness<User, UserQuery, UserVO> {
}