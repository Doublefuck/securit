package com.mmall.dao;

import com.mmall.model.SysDept;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     * 获取当前用户的所有部门
     * @return
     */
    List<SysDept> getAllDept();

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    /**
     * 批量更新部门等级
     * @param sysDeptList
     */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    int countByNameAndParentId(@Param("parentId")int parentId,@Param("name") String name,@Param("id") Integer id);
}