package com.mmall.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.exception.ParamException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * 校验工具，主要是进行参数的校验
 * 该方法对对象中参数的校验是基于参数的注解进行的
 * 注解依赖validation-api和hibernate-validator
 * Created by Administrator on 2018/2/26 0026.
 */
public class BeanValidator {

    //自定义validator工厂，用于创建Validator对象
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    //key代表字段，value代表错误信息
    //返回错误字段+错误信息
    private static <T>Map<String,String> validate(T t,Class... groups){
        Validator validator = validatorFactory.getValidator(); //获取校验对象工具
        Set validateResult = validator.validate(t,groups);

        if(validateResult.isEmpty()){ //参数无错误
            return Collections.emptyMap();
        }else{
            LinkedHashMap errors = Maps.newLinkedHashMap();
            //遍历validateResult
            Iterator iter = validateResult.iterator();
            while(iter.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) iter.next();
                //错误字段+错误信息
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String,String> validateList(Collection<?> collection){
        //guava校验集合是否为空
        Preconditions.checkNotNull(collection);

        Iterator iterator = collection.iterator();
        Map errors;
        do{
            if(!iterator.hasNext()){
                return Collections.emptyMap(); //返回空map
            }
            Object obj = iterator.next();
            errors = validate(obj,new Class[0]);
        }while(errors.isEmpty());
        return errors;
    }

    public static Map<String,String> validateObject(Object first,Object... objects){
        if(objects != null && objects.length > 0){
            return validateList(Lists.asList(first,objects)); //将数组转成集合
        }else{
            return validate(first,new Class[0]);
        }
    }

    /**
     * 当执行该方法进行参数验证时，默认抛出ParamException异常信息
     * @param obj
     * @throws ParamException
     */
    public static void check(Object obj) throws ParamException{
        Map<String,String> map = BeanValidator.validateObject(obj);
        if(MapUtils.isNotEmpty(map)){
            throw new ParamException(map.toString());
        }
    }

}
