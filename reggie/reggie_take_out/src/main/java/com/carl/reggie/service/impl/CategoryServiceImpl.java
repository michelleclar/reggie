package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.common.CustomException;
import com.carl.reggie.entity.Category;
import com.carl.reggie.entity.Dish;
import com.carl.reggie.entity.Setmeal;
import com.carl.reggie.mapper.CategoryMapper;
import com.carl.reggie.service.CategoryService;
import com.carl.reggie.service.DishService;
import com.carl.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-20 15:39
 **/
@Service
public class CategoryServiceImpl
        extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealSevice;

    /***
     * create time: 2022/12/20 17:37
     * @Description: 根据id删除分类
     * @param: [java.lang.Long]
     * @return: void
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品,如果关联,抛出异常
        //select count(*) from dish where category_id = #{categoryId}
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }
        //查询当前分类是否关联了套餐,如果关联,抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealSevice.count();
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }
        //正常删除

        super.removeById(id);
    }
}
