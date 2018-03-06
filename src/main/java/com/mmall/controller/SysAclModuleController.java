package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.model.SysAclModule;
import com.mmall.param.AclModuleParam;
import com.mmall.param.DeptParam;
import com.mmall.service.SysAclModuleService;
import com.mmall.service.SysTreeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限模块
 * Created by Administrator on 2018/3/3 0003.
 */
@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page(){
        return new ModelAndView("acl");
    }

    /**
     * 新增权限模块
     * @param aclModuleParam
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam aclModuleParam){
        sysAclModuleService.save(aclModuleParam);
        return JsonData.success();
    }

    /**
     * 更新权限模块及其子模块
     * @param aclModuleParam
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam aclModuleParam){
        sysAclModuleService.update(aclModuleParam);
        return JsonData.success();
    }

    /**
     * 递归排序组装权限模块树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        return JsonData.success(sysTreeService.aclModuleTree());
    }
}
