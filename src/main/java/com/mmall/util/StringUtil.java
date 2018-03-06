package com.mmall.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5 0005.
 */
public class StringUtil {

    public static List<Integer> splitToListInteger(String str) {

        // 将以","分割的字符串转换成String类型的List集合
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);

        List<Integer> list = Lists.newArrayList();
        for (String s : stringList) {
            Integer i = Integer.parseInt(s);
            list.add(i);
        }
        return list;
    }

}
