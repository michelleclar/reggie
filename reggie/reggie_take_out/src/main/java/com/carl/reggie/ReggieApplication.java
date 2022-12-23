package com.carl.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: reggie
 * @description: 启动
 * @author: Mr.Carl
 * @create: 2022-12-19 12:35
 **/
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement//开启事物支持
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
    }
}
