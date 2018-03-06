package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/3 0003.
 */
@Getter
@Setter
public class AclModuleLevelDto extends SysAclModule {

    private List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();

    private List<AclDto> aclDtoList = Lists.newArrayList();

    /**
     * 根据SysAclModule返回一个AclModuleLevelDto对象
     * @param sysAclModule
     * @return
     */
    public static AclModuleLevelDto adapt(SysAclModule sysAclModule){
        AclModuleLevelDto aclModuleLevelDto = new AclModuleLevelDto();
        BeanUtils.copyProperties(sysAclModule,aclModuleLevelDto);
        return aclModuleLevelDto;
    }

}
