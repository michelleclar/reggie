package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carl.reggie.common.R;
import com.carl.reggie.dto.DishDto;
import com.carl.reggie.entity.Category;
import com.carl.reggie.entity.Dish;
import com.carl.reggie.entity.DishFlavor;
import com.carl.reggie.service.CategoryService;
import com.carl.reggie.service.DishFlavorService;
import com.carl.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: reggie
 * @description:菜品管理
 * @author: Mr.Carl
 * @create: 2022-12-20 20:40
 **/
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    private R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("添加菜品成功");
    }

    @GetMapping("/page")
    private R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if ((category!=null)){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /***
     * create time: 2022/12/21 12:54
     * @Description: 根据id查询对应的口味
     * @param: [java.lang.Long]
     * @return: com.carl.reggie.common.R<com.carl.reggie.dto.DishDto>
     */
    @GetMapping("/{id}")
    private R<DishDto> get(@PathVariable("id") Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /***
     * create time: 2022/12/21 13:08
     * @Description: 修改菜品
     * @param: [com.carl.reggie.dto.DishDto]
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @PutMapping
    private R<String> update(@RequestBody DishDto dishDto){


        dishService.updateWithFlavor(dishDto);


        return R.success("修改成功");
    }

    @PostMapping ("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam("ids") List<Long> ids){
        log.info("status: {},id: {}",status,ids);


        dishService.updateByIds(status,ids);
        return R.success("修改成功");
    }

    @DeleteMapping()
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        log.info(ids.toString());

        dishService.removeWithFlavorByIds(ids);

        return R.success("删除成功");
    }

    /***
     * create time: 2022/12/21 15:57
     * @Description: 根据条件查询对应菜品对象
     * @param: [com.carl.reggie.entity.Dish]
     * @return: com.carl.reggie.common.R<java.util.List<com.carl.reggie.entity.Dish>>
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件,查询状态为1
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            categoryService.getById(categoryId);

            //菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishQueryWrapper = new LambdaQueryWrapper<>();
            dishQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?

            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;

        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        //添加条件,查询状态为1
//        queryWrapper.eq(Dish::getStatus,1);
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }
}
