package com.mmall.service;

import com.google.common.collect.Lists;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 获取用户或角色的权限
 * Created by Administrator on 2018/3/4 0004.
 */
@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    /**
     * 获取当前用户的权限点列表
     * @return
     */
    public List<SysAcl> getCurrentUserAclList() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    // 获取角色已分配的权限点列表
    public List<SysAcl> getRoleAclList(Integer roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    /**
     * 获取用户的权限点列表
     * @param userId
     * @return
     */
    public List<SysAcl> getUserAclList(Integer userId) {
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }

        // 获取当前用户已经分配的角色id
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        // 根据当前用户的所有角色id查询所有的权限acl的id集合
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        // 根据acl的id集合获取所有的权限对象SysAcl
        return sysAclMapper.getByIdList(userAclIdList);

    }

    public boolean isSuperAdmin() {
        return true;
    }

}
