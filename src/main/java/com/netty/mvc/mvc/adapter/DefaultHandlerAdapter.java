package com.netty.mvc.mvc.adapter;

import com.netty.mvc.server.WebHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zxj
 * @Date: 2019-09-21 12:52
 * @desc
 */
@Configuration
public class DefaultHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return WebHandler.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public Object handle(HttpRequest httpRequest, Object handler) {
        WebHandler webHandler = (WebHandler) handler;
        return webHandler.handle(httpRequest);
    }
}
