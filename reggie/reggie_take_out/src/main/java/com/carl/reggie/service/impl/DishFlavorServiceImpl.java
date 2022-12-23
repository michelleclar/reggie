package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.entity.DishFlavor;
import com.carl.reggie.mapper.DishFlavorMapper;
import com.carl.reggie.service.DishFlavorService;
import com.carl.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-20 20:38
 **/
@Service
@Slf4j
public class DishFlavorServiceImpl
                extends ServiceImpl<DishFlavorMapper, DishFlavor>
                implements DishFlavorService {
}
