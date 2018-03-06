package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.param.AclModuleParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 权限模块
 * Created by Administrator on 2018/3/3 0003.
 */
@Service
public class SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    /**
     * 新增权限模块
     * @param aclModuleParam
     */
    public void save(AclModuleParam aclModuleParam){
        BeanValidator.check(aclModuleParam); //校验传入的权限模块参数

        if(checkExist(aclModuleParam.getParentId(),aclModuleParam.getName(),aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        // 组装权限模块对象
        SysAclModule sysAclModule = SysAclModule.builder().name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperatorTime(new Date());

        sysAclModuleMapper.insertSelective(sysAclModule); // 添加权限模块到数据库
    }

    /**
     * 更新权限模块
     * 进行当前模块和子模块的更新操作
     * @param aclModuleParam
     */
    public void update(AclModuleParam aclModuleParam){
        BeanValidator.check(aclModuleParam); //校验传入的权限模块参数

        if(checkExist(aclModuleParam.getParentId(),aclModuleParam.getName(),aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId()); // 获取更新前的对象
        Preconditions.checkNotNull(before, "待更新的权限模块不存在"); //权限模块校验

        SysAclModule after = SysAclModule.builder().id(aclModuleParam.getId()).name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperatorTime(new Date());

        updateWithChild(before,after);

    }

    /**
     * 更新当前权限模块及其子权限模块信息
     * 使用事务管理
     * @param before
     * @param after
     */
    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after){
        String newLevelPrefix = after.getLevel(); //更新后部门的等级
        String oldLevelPrefix  = before.getLevel(); //更新前部门等级

        if(!after.getLevel().equals(before.getLevel())){
            //获取当前权限模块的子权限模块
            List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel()); //获取当前部门及子部门
            if(CollectionUtils.isNotEmpty(sysAclModuleList)){
                for(SysAclModule sysAclModule : sysAclModuleList){
                    String level = sysAclModule.getLevel();
                    if(level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        sysAclModule.setLevel(level);
                    }
                }
                //批量更新权限模块级别
                sysAclModuleMapper.batchUpdateLevel(sysAclModuleList);
            }
        }

        sysAclModuleMapper.updateByPrimaryKey(after); //更新当前权限模块
    }

    private boolean checkExist(Integer parentId,String aclModuleName,Integer aclModuleId){
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId){
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }

}
