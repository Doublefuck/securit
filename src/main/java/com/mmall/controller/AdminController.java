package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2018/3/1 0001.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * 登陆成功后跳转的页面
     * @return
     */
    @RequestMapping("/index.page")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }
}
