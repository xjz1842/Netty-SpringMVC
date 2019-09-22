package com.netty.mvc.annotiaon;

/**
 * @Author: zxj
 * @Date: 2019-09-09 17:10
 * @desc
 */
public enum  RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private RequestMethod() {
    }
}
