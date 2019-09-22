package com.netty.mvc.test;

import com.netty.mvc.annotiaon.Controller;
import com.netty.mvc.annotiaon.RequestMapping;
import com.netty.mvc.annotiaon.RequestMethod;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zxj
 * @Date: 2019-09-21 11:28
 * @desc
 */
@Controller
@RequestMapping(value = "/test")
@Configuration
public class TestController {

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String test(){
        return "Hi Netty SpringMVC";
    }

}
