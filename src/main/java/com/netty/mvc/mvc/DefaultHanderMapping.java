package com.netty.mvc.mvc;

import com.netty.mvc.server.handler.DefaultWebHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zxj
 * @Date: 2019-09-21 09:56
 * @desc
 */
@Configuration
public class DefaultHanderMapping implements HandlerMapping {

    @Autowired
    DefaultWebHandler defaultWebHandler;

    @Override
    public Object getWebHandler(HttpRequest httpRequest) {
        //支持其他方式

        return defaultWebHandler;
    }

}
