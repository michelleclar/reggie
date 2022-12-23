package com.carl.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carl.reggie.dto.SetmealDto;
import com.carl.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐(菜品)
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐和菜品关联数据
    public void removeWithDish(List<Long> ids);

    //修改套餐状态
    public void updateByIds(Integer status,List<Long> ids);
}
