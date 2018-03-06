package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/1 0001.
 */
@Service
@Slf4j
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    public SysUser findByKeyword(String keyword){
        return sysUserMapper.findByKeyword(keyword);
    }

    /**
     * 新增用户
     * @param userParam
     */
    public void save(UserParam userParam){
        BeanValidator.check(userParam); //参数校验

        //校验电话
        if(checkTelephoneExist(userParam.getTelephone(),userParam.getId())){
            throw new ParamException("电话号码已被占用");
        }
        //校验邮箱
        if(checkEmailExist(userParam.getEmail(),userParam.getId())){
            throw new ParamException("邮箱已被占用");
        }

        String password = "123456"; // TODO
        //md5密码加密
        String encryptedPassword = MD5Util.encrypt(password);
        //构建user对象
        SysUser sysUser = SysUser.builder().username(userParam.getUsername()).
                telephone(userParam.getTelephone()).email(userParam.getEmail()).
                password(encryptedPassword).deptId(userParam.getDeptId()).status(userParam.getStatus()).
                remark(userParam.getRemark()).build();
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())); // 最后一次操作人员的ip
        sysUser.setOperatorTime(new Date());

        //TODO : ssendEmail

        //添加数据到数据库
        sysUserMapper.insertSelective(sysUser);
    }

    /**
     * 更新用户
     * @param userParam
     */
    public void update(UserParam userParam){
        BeanValidator.check(userParam); //参数校验

        //校验电话
        if(checkTelephoneExist(userParam.getTelephone(),userParam.getId())){
            throw new ParamException("电话号码已被占用");
        }
        //校验邮箱
        if(checkEmailExist(userParam.getEmail(),userParam.getId())){
            throw new ParamException("邮箱已被占用");
        }

        //更新前的用户信息校验
        SysUser before = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");
        //更新后的用户信息（不更新密码）
        SysUser after = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).
                telephone(userParam.getTelephone()).email(userParam.getEmail()).
                deptId(userParam.getDeptId()).status(userParam.getStatus()).
                remark(userParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername()); //TODO
        after.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())); // 最后一次操作人员的ip
        after.setOperatorTime(new Date());

        sysUserMapper.updateByPrimaryKeySelective(after); //更新信息到数据库
    }

    /**
     * 根据userId校验用户邮箱是否存在
     * @param email
     * @param userId
     * @return
     */
    public boolean checkEmailExist(String email,Integer userId){
        return sysUserMapper.countByEmail(email,userId) > 0;
    }

    /**
     * 根据userId校验号码是否存在
     * @param telephone
     * @param userId
     * @return
     */
    public boolean checkTelephoneExist(String telephone,Integer userId){
        return sysUserMapper.countByTelephone(telephone,userId) > 0;
    }

    /**
     * 根据部门编号查询该部门下的用过户并分页展示
     * @param deptId
     * @param pageQuery
     * @return
     */
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery){
        BeanValidator.check(pageQuery); //分页参数校验
        int count = sysUserMapper.countByDeptId(deptId);
        if(count > 0){
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId,pageQuery);
            log.info(list.size() + "-------------------------------------->");
            return  PageResult.<SysUser>builder().total(count).data(list).build();
        }else{
            log.info("------------------------>" + count);
        }
        return PageResult.<SysUser>builder().build();
    }

}
