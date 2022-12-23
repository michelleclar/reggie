package com.carl.reggie.dto;

import com.carl.reggie.entity.OrderDetail;
import com.carl.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-22 19:49
 **/
@Data
public class OrderDto extends Orders {


    private List<OrderDetail> orderDetails;

}
