package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carl.reggie.common.R;
import com.carl.reggie.entity.Employee;
import com.carl.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-19 13:21
 **/
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /***
     * create time: 2022/12/19 15:26
     * @Description: 员工登录
     * 请求方式:post
     * http://localhost:8080/backend/page/login/login.html
     * /employee/login
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //将页面提交的密码,进行password加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);
        //根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //查询是否为空
        if (emp == null) {
            return R.error("登录失败");
        }

        //密码比对,如果不一致则返回登陆失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        //查看员工状态, 0:禁用 1:启用
        if (emp.getStatus() == 0) {
            return R.error("账户已禁用");
        }

        //至此登录成功,将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /***
     * create time: 2022/12/19 16:09
     * @Description: 退出
     * 请求方式:post
     * http://localhost:8080/backend/page/login/logout.html
     * /employee/logout
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /***
     * create time: 2022/12/19 18:08
     * @Description: 新增员工
     * http://localhost:8080/backend/index.html
     * /employee
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //设置初始密码,使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获得当前用户的ID
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /***
     * create time: 2022/12/19 19:05
     * @Description:
     * http://localhost:8080/employee/page?page=1&pageSize=10
     * /employee/page
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo =new Page<>(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /***
     * create time: 2022/12/19 20:57
     * @Description: 更据id修改员工信息
     * @param:
     * /employee
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("employee: {}",employee);

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    /***
     * create time: 2022/12/19 21:53
     * @Description: 根据员工id查询员工信息
     * 3@param: [java.lang.Long]
     * 23@return: com.carl.reggie.common.R<com.carl.reggie.entity.Employee>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        log.info("根据员工id查询员工信息");
        Employee emp = employeeService.getById(id);

        if(emp != null){
            return R.success(emp);
        }
        return R.error("没有查询到员工信息");

    }
}
