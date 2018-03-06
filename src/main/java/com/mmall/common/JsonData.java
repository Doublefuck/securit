package com.mmall.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2018/2/26 0026.
 */
@Setter
@Getter
public class JsonData {

    private boolean ret;
    private String msg;
    private Object data;

    public JsonData(boolean ret){
        this.ret = ret;
    }

    public static JsonData success(Object obj,String msg){
        JsonData jsonData = new JsonData(true);
        jsonData.data = obj;
        jsonData.msg = msg;
        return jsonData;
    }

    public static JsonData success(Object obj){
        JsonData jsonData = new JsonData(true);
        jsonData.data = obj;
        return jsonData;
    }

    public static JsonData success(){
        JsonData jsonData = new JsonData(true);
        return jsonData;
    }

    /**
     * msg存储失败、异常原因
     * @param msg
     * @return
     */
    public static JsonData fail(String msg){
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("ret",ret); //结果：成功/失败
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }

}
