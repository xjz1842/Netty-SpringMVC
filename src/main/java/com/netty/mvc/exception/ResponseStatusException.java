package com.netty.mvc.exception;

/**
 * @Author: zxj
 * @Date: 2019-09-17 21:31
 * @desc
 */
public class ResponseStatusException extends RuntimeException{

    public ResponseStatusException() {
    }

    public ResponseStatusException(String message) {
        super(message);
    }

    public ResponseStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseStatusException(Throwable cause) {
        super(cause);
    }

    public ResponseStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
