package com.foodsecurity.xupdate.entity;

import java.util.List;

/**
 * @author zhujianwei
 * @date 2019/4/27 23:33
 */
public class BundleVersionResult {
    /**
     * 请求返回码
     */
    private int code;
    /**
     * 请求信息
     */
    private String message;

    private List<VersionEntity> data;

    public int getCode() {
        return code;
    }

    public BundleVersionResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BundleVersionResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public BundleVersionResult setData(List<VersionEntity> data) {
        this.data = data;
        return this;
    }

    public List<VersionEntity> getData() {
        return data;
    }
}
