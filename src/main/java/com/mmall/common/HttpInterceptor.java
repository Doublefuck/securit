package com.mmall.common;

import com.mmall.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 全局http请求拦截器，拦截url请求地址
 * 可以在请求中添加或隐藏指定参数
 * 主要为了输出请求+参数，但是注意敏感信息的显示
 * 可以实现请求时间的计算
 * Created by Administrator on 2018/2/27 0027.
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "requestStartTime"; //请求完成所花费的时间

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start,url:{},params:{}",url, JsonMapper.obj2String(parameterMap));

        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME,start);

        return true;
    }

    /**
     * 正常返回时调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();

        long start = (Long) request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();

        removeThreadLocalInfo();

        log.info("request finished,url:{},params:{},cost:{}",url, JsonMapper.obj2String(parameterMap),end - start);
    }

    /**
     * 任何情况下返回都会调用
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();

        //removeThreadLocalInfo();

        log.info("request totally finished,url:{},params:{}",url, JsonMapper.obj2String(parameterMap));
    }

    public static void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
