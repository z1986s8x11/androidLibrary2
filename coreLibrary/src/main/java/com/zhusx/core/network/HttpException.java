package com.zhusx.core.network;

/**
* Author        zhusx
* Email         327270607@qq.com
* Created       2016/10/12 16:46
*/
public class HttpException extends Exception {
    private static final long serialVersionUID = 2339162990765061626L;
    public static final int ERROR_CODE_CANCEL = 19860811;
    private String message;
    private int errorCode = -1;

    public int _getErrorCode() {
        return errorCode;
    }

    public HttpException(String message) {
        this.message = message;
    }

    public HttpException(int code, String message) {
        this.message = message;
        this.errorCode = code;
    }

    public String _getErrorMessage() {
        return message;
    }
}
