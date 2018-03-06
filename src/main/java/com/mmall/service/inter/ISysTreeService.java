package com.mmall.service.inter;

import com.mmall.dto.DeptLevelDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
public interface ISysTreeService {

    /**
     * 组装部门树状结构
     * @return
     */
    public List<DeptLevelDto> deptTree();

}
