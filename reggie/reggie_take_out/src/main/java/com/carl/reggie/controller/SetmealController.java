package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carl.reggie.common.R;
import com.carl.reggie.dto.SetmealDto;
import com.carl.reggie.entity.Category;
import com.carl.reggie.entity.Setmeal;
import com.carl.reggie.entity.SetmealDish;
import com.carl.reggie.service.CategoryService;
import com.carl.reggie.service.SetmealDishService;
import com.carl.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-21 14:40
 **/
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /***
     * create time: 2022/12/21 16:21
     * @Description: 新增套餐
     * @param: [com.carl.reggie.dto.SetmealDto]
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }



    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(name != null,Setmeal::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        //查询出第一次分页数据
        List<Setmeal> records = pageInfo.getRecords();
        //为第一次数据中空字段赋值
        List<SetmealDto> list=records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item,setmealDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if ((category!=null)){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /***
     * create time: 2022/12/21 17:18
     * @Description: 删除套餐
     * @param: [java.util.List<java.lang.Long>]
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    private R<String> delete(@RequestParam("ids") List<Long> ids){

        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }


    @PostMapping ("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam("ids") List<Long> ids){
        log.info("status: {},id: {}",status,ids);


        setmealService.updateByIds(status,ids);

        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> update(@PathVariable Long id){


        //回显套餐
        Setmeal setmeal = setmealService.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        //回显菜品
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        //回显套餐类型
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);

        //封装数据
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);
        setmealDto.setCategoryName(category.getName());

        return R.success(setmealDto);
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);


        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
