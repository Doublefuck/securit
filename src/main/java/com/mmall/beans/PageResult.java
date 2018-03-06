package com.mmall.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 封装数据，展示分页后的结果
 * Created by Administrator on 2018/3/3 0003.
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total;

}
