package com.carl.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @program: reggie
 * @description:自定义元数据处理对象
 * @author: Mr.Carl
 * @create: 2022-12-20 10:13
 **/
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;

    /***
     * create time: 2022/12/20 11:05
     * @Description: 插入时自动填充
     * @param: [org.apache.ibatis.reflection.MetaObject]
     * @return: void
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        Object employee = request.getSession().getAttribute("employee");
        System.out.println(employee);
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /***
     * create time: 2022/12/20 11:05
     * @Description: 更新时自动填充
     * @param: [org.apache.ibatis.reflection.MetaObject]
     * @return: void
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
