package com.carl.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.carl.reggie.common.BaseContext;
import com.carl.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: reggie
 * @description: 登录拦截
 * @author: Mr.Carl
 * @create: 2022-12-19 16:41
 **/

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求: {}",request.getRequestURI());

        //定义不需要的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登录
        };

        //判断请求路径是否需要处理
        boolean check = check(urls, requestURI);

        //如果不需要处理,则直接放行
        if(check){
            log.info("本次请求不需要处理: {}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //判断登录状态,如果已经登录,则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户登录,用户ID为: {}",request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("user") != null){
            log.info("用户登录,用户ID为: {}",request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        //如果未登录则返回登录结果,通过输出流方式向客户端响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
    }

    /***
     * create time: 2022/12/19 16:58
     * @Description: 路径匹配,检查本次请求是否需要放行
     * @param: [java.lang.String]
     * @return: boolean
     */
    public boolean check(String[] urls,String requestURI){

        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
