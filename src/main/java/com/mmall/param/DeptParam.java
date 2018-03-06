package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 部门参数类
 * Created by Administrator on 2018/2/27 0027.
 */
@Getter
@Setter
@ToString
public class DeptParam {

    private Integer id;

    @NotNull(message = "部门名称不可以为空")
    @Length(max = 15,min = 2,message = "部门名称需要在2到15的字之间")
    private String name;

    private Integer parentId;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 150,message = "备注长度不能超过150个字")
    private String remark;

}
