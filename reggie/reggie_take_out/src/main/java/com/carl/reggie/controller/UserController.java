package com.carl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.carl.reggie.common.R;

import com.carl.reggie.entity.User;
import com.carl.reggie.service.UserService;
import com.carl.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @program: reggie
 * @description:
 * @author: Mr.Carl
 * @create: 2022-12-21 20:29
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpSession session,@RequestBody User user){
        log.info("user: {}",user);
        //获取手机号
        String phone = user.getPhone();
        //生成随机验证码4位
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code: {}",code);
            //调用阿里云发送短信

            //邮件发送         sendEmail("wb2197792192@163.com",ValidateCodeUtils.generateValidateCode(4).toString());
//            EmailSender.sendEmail(phone,ValidateCodeUtils.generateValidateCode(4).toString());
            //需要将生成的验证码保存起来
            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }


        return R.success("手机验证码短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info("map: {}"+map);

        //获取手机号
        String phone =  map.get("phone").toString();
        //获取验证码
        String code =  map.get("code").toString();

        //从session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //进行比较
        if (codeInSession != null && codeInSession.equals(code)){
            //判断当前用户是否为新用户,如果是就自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if (user == null){
                //自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }



        return R.error("登录失败");
    }

    /***
     * create time: 2022/12/22 21:56
     * @Description: 退出登录
     * @param:
     * @return:
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){

        //清理Session中保存的当前登录用户的id
        request.getSession().removeAttribute("user");

        return R.success("退出成功");
    }


}
