package com.mmall.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 部门层级类
 * Created by Administrator on 2018/2/27 0027.
 */
public class LevelUtil {

    public static final String SEPARATOR = "."; //分隔符

    public static final String ROOT = "0"; //部门

    public static String calculateLevel(String parentLevel,int parentId){
        if(StringUtils.isBlank(parentLevel)){
            return ROOT; //最父级部门
        }else{
            //层级.部门id
            return StringUtils.join(parentLevel,SEPARATOR,parentId);
        }
    }

}
