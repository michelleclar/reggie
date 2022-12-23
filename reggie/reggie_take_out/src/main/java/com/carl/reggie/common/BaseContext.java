package com.carl.reggie.common;

/**
 * @program: reggie
 * @description: 基于ThreadLocal封装工具类, 用户保存和获取当前登录用户id
 * @author: Mr.Carl
 * @create: 2022-12-20 14:46
 **/
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
