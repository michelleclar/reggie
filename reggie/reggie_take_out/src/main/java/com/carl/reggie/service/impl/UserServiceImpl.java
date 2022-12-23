package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.entity.User;
import com.carl.reggie.mapper.UserMapper;
import com.carl.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-21 20:28
 **/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
