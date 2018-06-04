package com.ski.ms.lib.exception;

/**
 * Created by czx on 2018/4/24.
 */
public class ValidateCodeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
