package com.netty.mvc.server;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author: zxj
 * @Date: 2019-09-16 20:10
 * @desc
 */
public interface WebHandler {

     Object handle(HttpRequest httpRequest);

}
