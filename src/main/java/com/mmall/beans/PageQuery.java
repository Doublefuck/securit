package com.mmall.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * Created by Administrator on 2018/3/3 0003.
 */
public class PageQuery {



    @Setter
    @Getter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo = 1;

    @Setter
    @Getter
    @Min(value = 1,message = "每页展示的数量不合法")
    private int pageSize = 10;

    @Setter private int offset; // 偏移量

    public int getOffset(){
        return (pageNo - 1) * pageSize;
    }

}
