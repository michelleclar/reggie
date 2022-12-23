package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carl.reggie.common.BaseContext;
import com.carl.reggie.common.R;
import com.carl.reggie.dto.OrderDto;
import com.carl.reggie.entity.*;
import com.carl.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {

        log.info("page = {},pageSize = {}", page, pageSize);

        //获取用户id来查询订单id
        Long currentId = BaseContext.getCurrentId();

        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPageInfo = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        queryWrapper.orderByAsc(Orders::getOrderTime);
        queryWrapper.eq(Orders::getUserId, currentId);

        //执行分页查询
        orderService.page(pageInfo, queryWrapper);

        //拷贝
        BeanUtils.copyProperties(pageInfo, orderDtoPageInfo,"records");


        List<Orders> records = pageInfo.getRecords();

        //订单详情构造器


        List<OrderDto> userOrderDtos = records.stream().map((item) -> {
            LambdaQueryWrapper<OrderDetail> ordersDetailQueryWrapper = new LambdaQueryWrapper<>();
            OrderDto userOrderDto = new OrderDto();

            BeanUtils.copyProperties(item,userOrderDto);

            Long orderId = item.getId();
            ordersDetailQueryWrapper.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> list = orderDetailService.list(ordersDetailQueryWrapper);
            userOrderDto.setOrderDetails(list);

            return userOrderDto;
        }).collect(Collectors.toList());

        //设置显示数据
        orderDtoPageInfo.setRecords(userOrderDtos);

        return R.success(orderDtoPageInfo);
    }
//    public R<Page> userPage(int page, int pageSize) {
//
//        log.info("page = {},pageSize = {}", page, pageSize);
//
//        //获取用户id来查询订单id
//        Long currentId = BaseContext.getCurrentId();
//
//        //构造分页构造器
//        Page<Orders> pageInfo = new Page<>(page, pageSize);
//        Page<OrderDto> orderDtoPageInfo = new Page<>();
//
//        //构造条件构造器
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//
//        //添加排序条件
//        queryWrapper.orderByAsc(Orders::getOrderTime);
//        queryWrapper.eq(Orders::getUserId, currentId);
//
//        //执行分页查询
//        orderService.page(pageInfo, queryWrapper);
//
//        //拷贝
//        BeanUtils.copyProperties(pageInfo, orderDtoPageInfo, "records");
//
//
//        List<Orders> records = pageInfo.getRecords();
//
//        //订单详情构造器
//        LambdaQueryWrapper<OrderDetail> ordersDetailQueryWrapper = new LambdaQueryWrapper<>();
//
//        List<OrderDto> orderDtos = records.stream().map((item) -> {
//            OrderDto orderDto = new OrderDto();
//            //拷贝订单
//            BeanUtils.copyProperties(item, orderDto,"records");
//            Long orderId = item.getId();
//
//            //订单详情查询
//            ordersDetailQueryWrapper.eq(OrderDetail::getOrderId, orderId);
//            List<OrderDetail> orderDetailList = orderDetailService.list(ordersDetailQueryWrapper);
//
//            for (OrderDetail detail : orderDetailList) {
//                Long dishId = detail.getDishId();
//                if (dishId != null) {
//                    orderDto.setDish(dishService.getById(dishId));
//                }else {
//                    LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//                    setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,detail.getSetmealId());
//                    List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
//                    orderDto.setSetmealDishList(list);
//                }
//            }
//
//            orderDto.setDetails(orderDetailList);
//            return orderDto;
//        }).collect(Collectors.toList());
//
//        //设置显示数据
//        orderDtoPageInfo.setRecords(orderDtos);
//
//        return R.success(orderDtoPageInfo);
//    }


    //page=1&pageSize=10&number=123&beginTime=2022-12-14%2000%3A00%3A00&endTime=2022-12-22%2023%3A59%3A59
    @GetMapping("/page")
    public R<Page> page(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "number", required = false) Integer number,
                        @RequestParam(value = "beginTime", required = false) String beginTime,
                        @RequestParam(value = "endTime", required = false) String endTime) {

        log.info("page = {},pageSize = {}", page, pageSize);

        //获取所有订单

        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrderDto> orderDtoPageInfo = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        queryWrapper.orderByAsc(Orders::getCheckoutTime);
        queryWrapper.like(number != null, Orders::getNumber, number);
        if (beginTime != null && endTime != null) {
            queryWrapper.ge(Orders::getCheckoutTime, beginTime);
            queryWrapper.le(Orders::getCheckoutTime, endTime);
        }


        //执行分页查询
        orderService.page(pageInfo, queryWrapper);

        //拷贝
        BeanUtils.copyProperties(pageInfo, orderDtoPageInfo, "records");


        List<Orders> records = pageInfo.getRecords();

        //订单详情构造器
        LambdaQueryWrapper<OrderDetail> ordersDetailQueryWrapper = new LambdaQueryWrapper<>();

        List<OrderDto> orderDtos = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            //拷贝订单
            BeanUtils.copyProperties(item, orderDto);
            Long orderId = item.getId();

            //订单详情查询
            ordersDetailQueryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(ordersDetailQueryWrapper);

            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //设置显示数据
        orderDtoPageInfo.setRecords(orderDtos);

        return R.success(orderDtoPageInfo);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        Orders service = orderService.getById(orders);

        service.setStatus(orders.getStatus());

        orderService.updateById(service);

        return R.success("派送成功");
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){

//        orderService.againAdd(orders);

        return R.success("添加成功");
    }
}