package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dto.DeptLevelDto;
import com.mmall.param.DeptParam;
import com.mmall.service.SysDeptService;
import com.mmall.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page(){
        return new ModelAndView("dept");
    }

    /**
     * 新增部门
     * @param param
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData SaveDept(DeptParam param){
        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * 获取所有部门并组装成树状结构
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        List<DeptLevelDto> deptLevelDtoList = sysTreeService.deptTree();
        return JsonData.success(deptLevelDtoList);
    }

    /**
     * 更新部门信息
     * 在进行更新的时候，需要将当前部门的子部门进行更新操作，用到事务管理
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam deptParam){
        sysDeptService.update(deptParam);
        return JsonData.success();
    }
}
