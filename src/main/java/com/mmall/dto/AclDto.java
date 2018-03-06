package com.mmall.dto;

import com.mmall.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * 权限名称传输对象
 * Created by Administrator on 2018/3/4 0004.
 */
@Setter
@Getter
@ToString
public class AclDto extends SysAcl {

    private boolean checked = false; // 权限点默认不被选中

    private boolean hasAcl = false; // 默认不被允许操作权限点

    public static AclDto adapt(SysAcl sysAcl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(sysAcl, aclDto);
        return aclDto;
    }
}
