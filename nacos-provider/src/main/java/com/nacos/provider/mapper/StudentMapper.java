package com.nacos.provider.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nacos.provider.entity.Student;

/**
* 数据访问层
* @author guos
* @date 2021/03/03 12:07
*/
@DS("slave")
public interface StudentMapper extends BaseMapper<Student> {
}
