

package com.foodsecurity.xupdate.entity;

/**
 * 版本更新检查返回的结果
 *
 * 0:无版本更新
 * 1:有版本更新，不需要强制升级
 * 2:有版本更新，需要强制升级
 *
 * @author zhujianwei134
 * @date 2019/4/27
 */
public class ApkVersionResult {
    /**
     * 0: 无版本更新
     */
    public final static int NO_NEW_VERSION = 0;
    /**
     * 1: 有版本更新，不需要强制升级
     */
    public final static int HAVE_NEW_VERSION = 1;
    /**
     * 2: 有版本更新，需要强制升级
     */
    public final static int HAVE_NEW_VERSION_FORCED_UPLOAD = 2;
    /**
     * 请求返回码
     */
    private int code;
    /**
     * 请求信息
     */
    private String message;

    private VersionEntity data;

    public int getCode() {
        return code;
    }

    public ApkVersionResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApkVersionResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApkVersionResult setData(VersionEntity data) {
        this.data = data;
        return this;
    }

    public VersionEntity getData() {
        return data;
    }
}
