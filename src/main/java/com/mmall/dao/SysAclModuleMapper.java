package com.mmall.dao;

import com.mmall.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parentId")int parentId, @Param("name") String name, @Param("id") Integer id);

    /**
     * 递归获取权限模块
     * @param level
     * @return
     */
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    /**
     * 批量更新部门等级
     * @param sysAclModuleList
     */
    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);

    List<SysAclModule> getAllAclModule();
}