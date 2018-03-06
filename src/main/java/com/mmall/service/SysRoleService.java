package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysRole;
import com.mmall.param.RoleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 新增角色
     * @param roleParam
     */
    public void save(RoleParam roleParam) {
        BeanValidator.check(roleParam);
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole sysRole = SysRole.builder().name(roleParam.getName()).status(roleParam.getStatus()).
                type(roleParam.getType()).remark(roleParam.getRemark()).build();
//        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
//        sysRole.setOperatorTime(new Date());
//        sysRole.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperator("system");
        sysRole.setOperatorTime(new Date());
        sysRole.setOperatorIp("localhost");
        sysRoleMapper.insertSelective(sysRole);
    }

    /**
     * 更新角色
     * @param roleParam
     */
    public void update(RoleParam roleParam) {
        BeanValidator.check(roleParam);
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole before = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");

        SysRole after = SysRole.builder().id(roleParam.getId()).name(roleParam.getName()).status(roleParam.getStatus()).
                type(roleParam.getType()).remark(roleParam.getRemark()).build();
//        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        after.setOperatorTime(new Date());
//        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperator("systems");
        after.setOperatorTime(new Date());
        after.setOperatorIp("localhosts");

        sysRoleMapper.updateByPrimaryKeySelective(after);
    }



    /**
     * 获取所有角色
     * @return
     */
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    /**
     * 检查是否具有相同名称的角色
     * @param name
     * @param id
     * @return
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }
}
