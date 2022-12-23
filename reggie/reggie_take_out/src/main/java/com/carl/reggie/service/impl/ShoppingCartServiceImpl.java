package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.entity.ShoppingCart;
import com.carl.reggie.mapper.ShoppingCartMapper;
import com.carl.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-22 09:32
 **/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
