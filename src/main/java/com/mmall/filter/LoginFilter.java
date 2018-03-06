package com.mmall.filter;

import com.mmall.common.JsonData;
import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验用户是否登录的拦截器
 * Created by Administrator on 2018/3/3 0003.
 */
@Slf4j
public class LoginFilter implements Filter {


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // 核心方法
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String servletPath = req.getServletPath(); // 获取请求路径

        SysUser sysUser = (SysUser) req.getSession().getAttribute("user");
        if(sysUser == null){
            String path = "/signin.jsp";
            res.sendRedirect(path); // 重定向到登录页面
            return;
        }
        // 添加到ThreadLocal中
        RequestHolder.add(sysUser);
        RequestHolder.add(req);

        filterChain.doFilter(servletRequest,servletResponse); // 过滤链
        return;
    }

    public void destroy() {

    }
}
