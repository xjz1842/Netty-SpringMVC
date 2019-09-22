package com.netty.mvc.mvc.adapter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author: zxj
 * @Date: 2019-09-16 20:21
 * @desc
 */
public interface HandlerAdapter {

   boolean support(Object handler);

   Object handle(HttpRequest request, Object handler);
}
