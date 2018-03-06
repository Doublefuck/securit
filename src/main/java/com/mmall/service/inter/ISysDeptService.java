package com.mmall.service.inter;

import com.mmall.param.DeptParam;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
public interface ISysDeptService {

    /**
     * 新增部门
     * @param deptParam
     */
    void save(DeptParam deptParam);
}
