package com.nacos.provider.controller;

import com.nacos.common.controller.BaseController;
import com.nacos.provider.entity.Student;
import com.nacos.provider.service.StudentService;
import com.nacos.provider.vo.StudentVO;
import org.springframework.web.bind.annotation.*;

/**
* api类
* @author guos
* @date 2021/03/03 12:07
*/
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController<StudentService, Student, StudentVO> {
}