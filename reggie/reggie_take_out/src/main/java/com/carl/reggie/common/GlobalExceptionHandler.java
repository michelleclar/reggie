package com.carl.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: reggie
 * @description: 全局异常处理
 * @author: Mr.Carl
 * @create: 2022-12-19 18:34
 **/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /***
     * create time: 2022/12/19 18:38
     * @Description: 异常处理方法
     * @param: []
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){

        log.error(exception.getMessage());
        String message = exception.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String empName = split[2];
            return R.error(empName+"已经存在");
        }

        return R.error("未知错误");
    }

    /***
     * create time: 2022/12/20 17:53
     * @Description: 自定义异常处理
     * @param: [com.carl.reggie.common.CustomException]
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){

        log.error(exception.getMessage());

        return R.error(exception.getMessage());
    }

}
