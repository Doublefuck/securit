package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */
@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    /**
     * 新增权限点
     * @param aclParam
     */
    public void save(AclParam aclParam){
        BeanValidator.check(aclParam);
        if(checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())){
            throw new ParamException("当前权限模块下存在相同名称的权限点");
        }
        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).aclModuleId((aclParam.getAclModuleId())).
                url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).
                seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        sysAcl.setCode(generaterCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperatorTime(new Date());
        sysAcl.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.insertSelective(sysAcl);
    }

    /**
     * 更新权限点
     * @param aclParam
     */
    public void update(AclParam aclParam){
        BeanValidator.check(aclParam);
        if(checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())){
            throw new ParamException("当前权限模块下存在相同名称的权限点");
        }

        SysAcl before = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId((aclParam.getAclModuleId())).
                url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).
                seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperatorTime(new Date());
        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 根据权限模块id获取权限点并分页
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(aclList).total(aclList.size()).build();
        } else {
            return PageResult.<SysAcl>builder().build();
        }
    }

    /**
     * 检查当前权限模块下是否存在相同权限名称
     * @param aclModuleId
     * @param name
     * @param id
     * @return
     */
    private boolean checkExist(Integer aclModuleId, String name, Integer id){
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    /**
     * 生成权限码code值
     * 使用日期+随机数
     * @return
     */
    public String generaterCode(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date() + "_" + (int) (Math.random() * 100));
    }
}
