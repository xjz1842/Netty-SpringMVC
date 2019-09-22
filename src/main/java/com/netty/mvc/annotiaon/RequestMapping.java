package com.netty.mvc.annotiaon;

import java.lang.annotation.*;

/**
 * @Author: zxj
 * @Date: 2019-09-09 16:59
 * @desc
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    //配置路径
   String value();

   //配置请求参数
   RequestMethod[] method() default {} ;

}
