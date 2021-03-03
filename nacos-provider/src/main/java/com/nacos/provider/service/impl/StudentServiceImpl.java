package com.nacos.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nacos.provider.entity.Student;
import com.nacos.provider.mapper.StudentMapper;
import com.nacos.provider.service.StudentService;
import org.springframework.stereotype.Service;

/**
* service实现类
* @author guos
* @date 2021/03/03 12:07
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}