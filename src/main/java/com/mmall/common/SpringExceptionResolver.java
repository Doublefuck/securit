package com.mmall.common;

import com.mmall.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理类
 * 当遇到异常时，都会进入该类中进行异常信息的判断处理
 * 异常信息包括指定异常信息和默认异常信息
 * Created by Administrator on 2018/2/26 0026.
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {

        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error"; //默认异常信息
        JsonData result;

        // .json , .page  请求后缀
        //要求项目中所有请求json数据的请求，都以.json结尾
        if(url.endsWith(".json")){ //以.json结尾的请求
            if(ex instanceof SecurityException || ex instanceof ParamException){
                result = JsonData.fail(ex.getMessage());
            }else{
                log.error("unknown json exception,url:" + url,ex);
                result = JsonData.fail(defaultMsg); //默认异常信息
            }
            mv = new ModelAndView("jsonView",result.toMap());
        } else if(url.endsWith(".page")){ //以.page结尾的请求
            log.error("unknown page exception,url:" + url,ex);
            result = JsonData.fail(defaultMsg); //默认异常信息
            mv = new ModelAndView("exception",result.toMap());
        } else{
            log.error("unknown exception,url:" + url,ex);
            result = JsonData.fail(defaultMsg); //默认异常信息
            mv = new ModelAndView("jsonView",result.toMap());
        }

        return mv;
    }
}
