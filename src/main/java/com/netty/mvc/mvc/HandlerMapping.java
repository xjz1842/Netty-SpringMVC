package com.netty.mvc.mvc;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author: zxj
 * @Date: 2019-09-05 11:10
 * @desc
 */
public interface HandlerMapping {

    Object getWebHandler(HttpRequest httpRequest);

}

