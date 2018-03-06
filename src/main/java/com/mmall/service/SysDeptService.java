package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
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
 * Created by Administrator on 2018/2/27 0027.
 */
@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    /**
     * 新增部门
     * @param deptParam
     */
    public void save(DeptParam deptParam){
        BeanValidator.check(deptParam); //校验传入的部门参数

        if(checkExist(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        //创建SysDept对象
        SysDept dept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        //组装部门层级关系
        dept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())); // 最后一次操作人员的ip
        dept.setOperatorTime(new Date());

        sysDeptMapper.insertSelective(dept); //添加到数据库
    }

    /**
     * 更新部门
     * @param deptParam
     */
    public void update(DeptParam deptParam){
        BeanValidator.check(deptParam); //校验传入的部门参数

        if(checkExist(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(beforeDept,"待更新的部门不存在");
        if(checkExist(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){

        }
        SysDept afterDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        afterDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        afterDept.setOperator(RequestHolder.getCurrentUser().getUsername()); //TODO
        afterDept.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())); // 最后一次操作人员的ip
        afterDept.setOperatorTime(new Date());

        updateWithChild(beforeDept,afterDept);
    }

    /**
     * 更新当前部门及其子部门信息
     * 使用事务管理
     * @param before
     * @param after
     */
    @Transactional
    public void updateWithChild(SysDept before,SysDept after){

        String newLevelPrefix = after.getLevel(); //更新后部门的等级
        String oldLevelPrefix  = before.getLevel(); //更新前部门等级

        if(!after.getLevel().equals(before.getLevel())){
            //获取当前部门的子部门
            List<SysDept> sysDeptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel()); //获取当前部门及子部门
            if(CollectionUtils.isNotEmpty(sysDeptList)){
                for(SysDept sysDept : sysDeptList){
                    String level = sysDept.getLevel();
                    if(level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        sysDept.setLevel(level);
                    }
                }
            //批量更新部门级别
            sysDeptMapper.batchUpdateLevel(sysDeptList);
            }
        }

        sysDeptMapper.updateByPrimaryKey(after); //更新当前部门
    }

    /**
     * 同级部门下面不能有相同名称的部门名称
     * @param parentId
     * @param deptName
     * @param deptId
     * @return
     */
    private boolean checkExist(Integer parentId,String deptName,Integer deptId){
        return sysDeptMapper.countByNameAndParentId(parentId,deptName,deptId) > 0;
    }

    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if(dept == null){
            return null;
        }
        return dept.getLevel();
    }
}
