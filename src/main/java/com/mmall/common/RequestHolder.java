package com.mmall.common;

import com.mmall.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 将request中的信息放入到ThreadLocal中
 * ThreadLocL相当于一个Map，其中的key对应的是当前线程
 * Created by Administrator on 2018/3/3 0003.
 */
public class RequestHolder {

    // 将用户信息存入到ThreadLocal中
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

    //将请求存入到ThreadLocal中
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    // 存放
    public static void add(SysUser sysUser){
        userHolder.set(sysUser); // 放入到ThreadLocal中
    }

    public static void add(HttpServletRequest request){
        requestHolder.set(request); // 放入到ThreadLocal中
    }

    // 获取
    public static SysUser getCurrentUser(){
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest(){
        return requestHolder.get();
    }

    // 销毁
    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }


}
