package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2019/5/11 14:25
 */
public class BaseResponse<T> {
    /**
     * 请求返回码
     */
    private int code;
    /**
     * 请求信息
     */
    private String message;

    private T data;

    public int getCode() {
        return code;
    }

    public BaseResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public BaseResponse setData(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return data;
    }

    public boolean isOk() {
        return 200 == code;
    }
}
