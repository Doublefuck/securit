package com.mmall.controller;

import com.mmall.beans.PageQuery;
import com.mmall.common.JsonData;
import com.mmall.param.AclParam;
import com.mmall.service.SysAclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/3/3 0003.
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    @Resource
    private SysAclService sysAclService;

    /**
     * 新增权限点
     * @param aclParam
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclParam aclParam){
        sysAclService.save(aclParam);
        return JsonData.success();
    }

    /**
     * 更新权限点
     * @param aclParam
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(AclParam aclParam){
        sysAclService.update(aclParam);
        return JsonData.success();
    }

    /**
     * 获取当前权限模块下的权限点并分页
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery){
        sysAclService.getPageByAclModuleId(aclModuleId, pageQuery);
        return JsonData.success();
    }
}
