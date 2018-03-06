package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
@Setter
@Getter
@ToString
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept sysDept){
        DeptLevelDto deptLevelDto = new DeptLevelDto();

        //spring bean中的方法，将第一个类拷贝成第二个类
        BeanUtils.copyProperties(sysDept,deptLevelDto);

        return deptLevelDto;
    }

}
