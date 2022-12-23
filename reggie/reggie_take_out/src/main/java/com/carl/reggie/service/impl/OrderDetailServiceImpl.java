package com.carl.reggie.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.entity.OrderDetail;
import com.carl.reggie.mapper.OrderDetailMapper;
import com.carl.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}