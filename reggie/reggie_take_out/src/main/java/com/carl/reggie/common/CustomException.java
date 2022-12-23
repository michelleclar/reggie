package com.carl.reggie.common;

/**
 * @program: reggie
 * @description: 自定义业务异常
 * @author: Mr.Carl
 * @create: 2022-12-20 17:48
 **/
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
