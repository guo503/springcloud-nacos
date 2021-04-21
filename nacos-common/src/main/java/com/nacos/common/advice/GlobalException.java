package com.nacos.common.advice;

import com.nacos.common.response.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常捕捉
 * controller层 要使用@RestController与RestControllerAdvice对应，
 * 如果使用@Controller 则使用@ControllerAdvice与之对应
 *
 * @author guos
 * @date 2021/3/6 16:20
 **/
@RestControllerAdvice
public class GlobalException {


    /**
     * 拦截异常
     *
     * @param e
     * @return
     * @author guos
     * @date 2021/3/6 16:20
     **/
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        return Result.fail(e.getMessage());
    }
}
