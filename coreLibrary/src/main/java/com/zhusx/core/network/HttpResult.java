package com.zhusx.core.network;

import com.zhusx.core.interfaces.IHttpResult;

public class HttpResult<M> implements IHttpResult<M> {
    private boolean isSuccess;
    private String message;
    private M data;
    private int errorCode = -1;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }
}
