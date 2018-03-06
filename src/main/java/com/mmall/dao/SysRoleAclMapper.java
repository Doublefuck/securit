package com.mmall.dao;

import com.mmall.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /**
     * 传入多个角色id，查询它们共同的权限id集合
     * @param roleIdList
     * @return
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    // 根据roleId删除角色权限
    void deleteByRoleId(@Param("roleId") int roleId);

    // 批量插入新的角色权限
    void batchInsert(@Param("roleAclList") List<SysRoleAcl> sysRoleAclList);
}