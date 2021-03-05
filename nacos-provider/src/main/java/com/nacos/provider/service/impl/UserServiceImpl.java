package com.nacos.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nacos.provider.entity.Student;
import com.nacos.provider.entity.User;
import com.nacos.provider.mapper.StudentMapper;
import com.nacos.provider.mapper.UserMapper;
import com.nacos.provider.service.UserService;
import com.nacos.provider.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * service实现类
 *
 * @author guos
 * @date 2021/03/03 12:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 多数据源事务测试
     *
     * @param userVO
     * @return
     * @author guos
     * @date 2021/3/5 9:54
     **/
    @Override
    @Transactional
    public void multiSave(UserVO userVO) {
        User user = User.builder().id("6").age(25).name("tsyj").email("962131438@qq.com").build();
        baseMapper.insert(user);
        int i = 1 / 0;
        Student student = Student.builder().id("4").age(5).name("lucy").build();
        studentMapper.insert(student);
    }
}
