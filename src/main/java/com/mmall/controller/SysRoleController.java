package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.model.SysRole;
import com.mmall.param.RoleParam;
import com.mmall.service.SysRoleAclService;
import com.mmall.service.SysRoleService;
import com.mmall.service.SysTreeService;
import com.mmall.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 * Created by Administrator on 2018/3/4 0004.
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysTreeService sysTreeService;

    @Resource
    private SysRoleAclService sysRoleAclService;

    /**
     * 新增角色
     * @param roleParam
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveRole(RoleParam roleParam) {
        sysRoleService.save(roleParam);
        return JsonData.success();
    }

    /**
     * 更新角色
     * @param roleParam
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(RoleParam roleParam) {
        sysRoleService.update(roleParam);
        return JsonData.success();
    }

    /**
     * 获取所有角色列表并分页
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list(){
        return JsonData.success(sysRoleService.getAll());
    }

    /**
     * 取出当前角色的权限树
     * @param roleId
     * @return
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") Integer roleId) {
        sysTreeService.roleTree(roleId);
        return JsonData.success();
    }

    /**
     * 批量更新角色权限
     * @param roleId
     * @param aclIds
     * @return
     */
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") Integer roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInteger(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }
}
