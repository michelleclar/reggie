package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carl.reggie.common.R;
import com.carl.reggie.entity.Category;
import com.carl.reggie.entity.Employee;
import com.carl.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: reggie
 * @description:分类管理
 * @author: Mr.Carl
 * @create: 2022-12-20 15:41
 **/
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /***
     * create time: 2022/12/20 16:24
     * @Description: 新增分类
     * http://localhost:8080/category
     * /category
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category {}",category);
        categoryService.save(category);

        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        log.info("page = {},pageSize = {}", page, pageSize);

        //构造分页构造器

        Page<Category> pageInfo =new Page<>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("ids: {}",ids);

//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }

    /***
     * create time: 2022/12/20 18:04
     * @Description: 根据id修改分类信息
     * @param: [javax.servlet.http.HttpServletRequest, com.carl.reggie.entity.Category]
     * @return: com.carl.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("category: {}",category);

        categoryService.updateById(category);

        return R.success("员工信息修改成功");
    }

    /***
     * create time: 2022/12/20 20:54
     * @Description: 根据条件查询分类数据
     * http://localhost:8080/category/list
     * @return: com.carl.reggie.common.R<java.util.List<com.carl.reggie.entity.Category>>
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }

}
