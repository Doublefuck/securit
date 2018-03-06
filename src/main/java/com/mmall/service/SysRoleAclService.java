package com.mmall.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.model.SysRoleAcl;
import com.mmall.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 角色权限
 * Created by Administrator on 2018/3/5 0005.
 */
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {

        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdList.removeAll(aclIdList);
            if (CollectionUtils.isEmpty(originAclIdList)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
    }

    /**
     * 批量更新角色权限
     * 实际操作是先删除指定roleId的权限，再插入新的角色权限
     * 使用事务控制
     * @param roleId
     * @param aclIdList
     */
    @Transactional
    public void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if(CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> sysRoleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).
                    operator(RequestHolder.getCurrentUser().getUsername()).
                    operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).
                    operatorTime(new Date()).build();
            sysRoleAclList.add(sysRoleAcl);
        }
        // 批量插入
        sysRoleAclMapper.batchInsert(sysRoleAclList);
    }
}
