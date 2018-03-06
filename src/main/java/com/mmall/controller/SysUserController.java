package com.mmall.controller;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 后台管理用户
 * Created by Administrator on 2018/2/28 0028.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 新增用户的接口
     * @param userParam
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    private JsonData saveUser(UserParam userParam){
        sysUserService.save(userParam);
        return JsonData.success();
    }

    /**
     * 更新用户的接口
     * @param userParam
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    private JsonData updateUser(UserParam userParam){
        sysUserService.update(userParam);
        return JsonData.success();
    }

    /**
     * 根据部门id获取部门下的所有用户
     * @param deptId
     * @param pageQuery
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    private JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery){
        PageResult<SysUser> pageResult = sysUserService.getPageByDeptId(deptId,pageQuery);
        return JsonData.success(pageResult);
    }
}
