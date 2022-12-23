package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.common.CustomException;
import com.carl.reggie.dto.SetmealDto;
import com.carl.reggie.entity.Setmeal;
import com.carl.reggie.entity.SetmealDish;
import com.carl.reggie.mapper.SetmealMapper;
import com.carl.reggie.service.SetmealDishService;
import com.carl.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-20 17:33
 **/
@Service
@Slf4j
public class SetmealServiceImpl
        extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /***
     * create time: 2022/12/21 16:24
     * @Description: 新增套餐(菜品)
     * @param: [com.carl.reggie.dto.SetmealDto]
     * @return: void
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息,操作setmeal,执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息,操作setmeal_dish执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /***
     * create time: 2022/12/21 17:20
     * @Description: 删除套餐和菜品关联数据
     * @param: [java.util.List<java.lang.Long>]
     * @return: void
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态 select count(*) from setmeal where id in (1.2.3) and status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if (count>0){
            //如果不能删除,抛出一个业务异常
            throw new CustomException("套餐正在售卖中不能删除");
        }
        //如果可以删除,先删除套餐表中的数据 setmeal
        this.removeByIds(ids);

        //删除关系表中的数据 setmeal_dish delete from where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

    @Override
    public void updateByIds(Integer status, List<Long> ids) {
        List<Setmeal> setmeals = this.listByIds(ids);

        for (Setmeal setmeal : setmeals) {
            setmeal.setStatus(status);
            this.updateById(setmeal);
        }

    }
}
