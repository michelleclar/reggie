package com.carl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carl.reggie.entity.Employee;
import com.carl.reggie.mapper.EmployeeMapper;
import com.carl.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-19 13:19
 **/
@Service
public class EmployeeServiceImpl
        extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {


}
