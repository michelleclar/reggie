package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.dto.DishDto;
import com.carl.reggie.entity.Dish;
import com.carl.reggie.entity.DishFlavor;
import com.carl.reggie.entity.Setmeal;
import com.carl.reggie.mapper.DishMapper;
import com.carl.reggie.service.DishFlavorService;
import com.carl.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-20 17:32
 **/
@Service
@Slf4j
public class DishServiceImpl
        extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional//开启事物
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到菜品表
        this.save(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    /***
     * create time: 2022/12/21 12:57
     * @Description: 根据id查询菜品信息和对应的口味信息
     * @param: [java.lang.Long]
     * @return: com.carl.reggie.dto.DishDto
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息,dish
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询当前对应的口味信息,从dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish
        this.updateById(dishDto);

        //清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);



        //添加提交数据dish_flavor
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void removeWithFlavor(Long id) {

        //清理当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);

        this.removeById(id);
    }

    @Override
    @Transactional
    public void updateByIds(Integer status, List<Long> ids) {
        List<Dish> setmeals = this.listByIds(ids);

        for (Dish dish : setmeals) {
            dish.setStatus(status);
            this.updateById(dish);
        }

    }

    @Override
    public void removeWithFlavorByIds(List<Long> ids) {

        for (Long id : ids) {
            this.removeWithFlavor(id);
        }

    }
}
