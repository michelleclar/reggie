package com.carl.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carl.reggie.dto.DishDto;
import com.carl.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {


    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表,:dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息,更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

    //删除当前菜品信息和对应口味数据
    public void removeWithFlavor(Long id);

    //修改菜品状态
    public void updateByIds(Integer status, List<Long> ids);

    void removeWithFlavorByIds(List<Long> ids);
}
